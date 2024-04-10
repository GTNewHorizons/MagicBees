package magicbees.bees;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.apiculture.IHiveFrame;
import forestry.apiculture.inventory.IApiaryInventory;
import magicbees.main.utils.ItemStackUtils;
import magicbees.tileentity.TileEntityMagicApiary;

public class MagicApiaryInventory implements IApiaryInventory {

    public static final int SLOT_QUEEN = 0;
    public static final int SLOT_DRONE = 1;
    public static final int SLOT_FRAME_START = 2;
    public static final int SLOT_FRAME_COUNT = 3;
    public static final int SLOT_PRODUCTS_START = 5;
    public static final int SLOT_PRODUCTS_COUNT = 7;

    private final TileEntityMagicApiary magicApiary;
    private final ItemStack[] items;

    public MagicApiaryInventory(TileEntityMagicApiary magicApiary) {
        this.magicApiary = magicApiary;
        this.items = new ItemStack[12];
    }

    @Override
    public ItemStack getQueen() {
        return magicApiary.getStackInSlot(SLOT_QUEEN);
    }

    @Override
    public ItemStack getDrone() {
        return magicApiary.getStackInSlot(SLOT_DRONE);
    }

    @Override
    public void setQueen(ItemStack itemstack) {
        magicApiary.setInventorySlotContents(SLOT_QUEEN, itemstack);
    }

    @Override
    public void setDrone(ItemStack itemstack) {
        magicApiary.setInventorySlotContents(SLOT_DRONE, itemstack);
    }

    @Override
    public boolean addProduct(ItemStack product, boolean all) {
        int countAdded = ItemStackUtils
                .addItemToInventory(magicApiary, product, SLOT_PRODUCTS_START, SLOT_PRODUCTS_COUNT);

        if (all) {
            return countAdded == product.stackSize;
        }
        return countAdded > 0;
    }

    public int getSizeInventory() {
        return items.length;
    }

    public ItemStack getStackInSlot(int i) {
        return items[i];
    }

    public void setInventorySlotContents(int i, ItemStack itemStack) {
        items[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0 || side == 1) {
            return new int[] { SLOT_QUEEN, SLOT_DRONE };
        }
        int[] slots = new int[SLOT_PRODUCTS_COUNT];
        for (int i = 0, slot = SLOT_PRODUCTS_START; i < SLOT_PRODUCTS_COUNT; ++i, ++slot) {
            slots[i] = slot;
        }
        return slots;
    }

    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        if (slot == SLOT_QUEEN && BeeManager.beeRoot.isMember(itemStack) && !BeeManager.beeRoot.isDrone(itemStack)) {
            return true;
        } else if (slot == SLOT_DRONE && BeeManager.beeRoot.isDrone(itemStack)) {
            return true;
        }
        return slot == SLOT_DRONE && BeeManager.beeRoot.isDrone(itemStack);
    }

    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        return slot >= SLOT_FRAME_START && slot <= SLOT_FRAME_START + 2;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public Collection<IHiveFrame> getFrames() {
        Collection<IHiveFrame> hiveFrames = new ArrayList<IHiveFrame>(SLOT_FRAME_COUNT);
        for (int i = SLOT_FRAME_START; i < SLOT_FRAME_START + SLOT_FRAME_COUNT; i++) {
            ItemStack stackInSlot = magicApiary.getStackInSlot(i);
            if (stackInSlot == null) {
                continue;
            }

            Item itemInSlot = stackInSlot.getItem();
            if (itemInSlot instanceof IHiveFrame) {
                hiveFrames.add((IHiveFrame) itemInSlot);
            }
        }
        return hiveFrames;
    }

    public void wearOutFrames(IBeeHousing beeHousing, int amount) {
        IBeekeepingMode beekeepingMode = BeeManager.beeRoot.getBeekeepingMode(magicApiary.getWorldObj());
        int wear = Math.round(amount * beekeepingMode.getWearModifier());
        for (int i = MagicApiaryInventory.SLOT_FRAME_START; i
                < MagicApiaryInventory.SLOT_FRAME_START + MagicApiaryInventory.SLOT_FRAME_COUNT; i++) {
            ItemStack hiveFrameStack = magicApiary.getStackInSlot(i);
            if (hiveFrameStack == null) {
                continue;
            }
            Item hiveFrameItem = hiveFrameStack.getItem();
            if (!(hiveFrameItem instanceof IHiveFrame)) {
                continue;
            }
            IHiveFrame hiveFrame = (IHiveFrame) hiveFrameItem;
            ItemStack queenStack = magicApiary.getBeeInventory().getQueen();
            IBee queen = BeeManager.beeRoot.getMember(queenStack);
            ItemStack usedFrame = hiveFrame.frameUsed(magicApiary, hiveFrameStack, queen, wear);
            magicApiary.setInventorySlotContents(i, usedFrame);
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        NBTTagList itemsNBT = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = items[i];
            if (itemStack != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte) i);
                itemStack.writeToNBT(item);
                itemsNBT.appendTag(item);
            }
        }
        compound.setTag("Items", itemsNBT);
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList items = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("Slot");
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }
        }
    }
}
