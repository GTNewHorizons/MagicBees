package magicbees.main.utils.compat.botania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import magicbees.bees.BeeManager;
import magicbees.main.utils.compat.BotaniaHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileFunctional;

public class SubTileHibeescus extends SubTileFunctional {

    public static final String NAME = "hibeescus";
    private final int MANA_PER_OPERATION = 10000;
    // Ticks per second * seconds per minute * real-time minutes
    private final int OPERATION_TICKS_TIME = 20 * 60 * 15;
    private final double RANGE = 0.75;

    private ItemStack beeSlot;
    private long operationTicksRemaining;
    private float manaCostRollover;

    @Override
    public int getColor() {
        return 0xF94F4F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (supertile.getWorldObj().isRemote) {
            return;
        }

        if (beeSlot != null) {
            progressOperation();
        } else if (beeSlot == null && isTimeToPickUpItem() && redstoneSignal == 0) {
            findBeeItemToHold();
        }
    }

    protected boolean isTimeToPickUpItem() {
        return (this.supertile.getWorldObj().getTotalWorldTime() ^ supertile.xCoord ^ supertile.zCoord) % 11 == 0;
    }

    private void progressOperation() {
        if (0 < operationTicksRemaining && 0 < this.mana) {
            operationTicksRemaining--;
            manaCostRollover += getManaPerOpTick();
            if (1f <= manaCostRollover) {
                int amount = (int) manaCostRollover;
                mana -= amount;
                manaCostRollover -= amount;
            }
        } else if (operationTicksRemaining <= 0) {
            IBee bee = BeeManager.beeRoot.getMember(beeSlot);
            bee.setIsNatural(true);
            EnumBeeType beeType = EnumBeeType.QUEEN;
            if (bee.getMate() == null) {
                beeType = EnumBeeType.PRINCESS;
            }
            ItemStack outputStack = BeeManager.beeRoot.getMemberStack(bee, beeType.ordinal());
            EntityItem entity = dropItemStackInRange(outputStack);

            supertile.getWorldObj().spawnEntityInWorld(entity);
            beeSlot = null;
        }
    }

    protected EntityItem dropItemStackInRange(ItemStack outputStack) {
        Random r = supertile.getWorldObj().rand;
        EntityItem entity = new EntityItem(
                supertile.getWorldObj(),
                supertile.xCoord - RANGE + r.nextInt((int) (RANGE * 2 + 1)),
                supertile.yCoord + 1,
                supertile.zCoord - RANGE + r.nextInt((int) (RANGE * 2 + 1)),
                outputStack);
        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;
        return entity;
    }

    private void findBeeItemToHold() {
        @SuppressWarnings("unchecked")
        List<EntityItem> items = supertile.getWorldObj()
                .getEntitiesWithinAABB(EntityItem.class, getSearchBoundingBox());

        for (EntityItem itemEntity : items) {
            ItemStack item = itemEntity.getEntityItem();
            if (!itemEntity.isDead && isItemPrincessOrQueen(item)) {
                beeSlot = itemEntity.getEntityItem().copy();
                beeSlot.stackSize = 1;
                operationTicksRemaining = (long) (OPERATION_TICKS_TIME * BotaniaHelper.hibeescusTicksMultiplier)
                        + supertile.getWorldObj().rand.nextInt(200);
                manaCostRollover = 0f;

                // Princesses and Queens don't stack, but YOU NEVER KNOW.
                item.stackSize -= 1;
                if (item.stackSize <= 0) {
                    itemEntity.setDead();
                }

                // Leave for loop
                break;
            }
        }
    }

    private boolean isItemPrincessOrQueen(ItemStack stack) {
        boolean isBee = BeeManager.beeRoot.isMember(stack, EnumBeeType.PRINCESS.ordinal())
                || BeeManager.beeRoot.isMember(stack, EnumBeeType.QUEEN.ordinal());
        if (isBee) {
            IBee bee = BeeManager.beeRoot.getMember(stack);
            // Only process Ignoble bees.
            if (!bee.isNatural()) {
                return true;
            }
        }
        return false;
    }

    private AxisAlignedBB getSearchBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
                supertile.xCoord - RANGE,
                supertile.yCoord,
                supertile.zCoord - RANGE,
                supertile.xCoord + RANGE + 1,
                supertile.yCoord + 1,
                supertile.zCoord + RANGE + 1);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);
        if (cmp.hasKey("slot")) {
            NBTTagCompound itemTag = (NBTTagCompound) cmp.getTag("slot");
            beeSlot = ItemStack.loadItemStackFromNBT(itemTag);
        }
        operationTicksRemaining = cmp.getLong("operationTicks");
        manaCostRollover = cmp.getFloat("manaRollover");
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);
        if (beeSlot != null) {
            NBTTagCompound itemTag = new NBTTagCompound();
            beeSlot.writeToNBT(itemTag);
            cmp.setTag("slot", itemTag);
        }
        cmp.setLong("operationTicks", operationTicksRemaining);
        cmp.setFloat("manaRollover", manaCostRollover);
    }

    @Override
    public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
        if (beeSlot != null) {
            list.add(beeSlot);
        }
        return super.getDrops(list);
    }

    @Override
    public int getMaxMana() {
        return getFinalOperationManaCost() / 20;
    }

    protected int getFinalOperationManaCost() {
        return (int) (MANA_PER_OPERATION * BotaniaHelper.hibeescusManaCostMultiplier);
    }

    protected float getManaPerOpTick() {
        return (float) getFinalOperationManaCost() / OPERATION_TICKS_TIME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {
        return BotaniaAPI.getSignatureForName(NAME).getIconForStack(null);
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }
}
