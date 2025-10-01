package magicbees.tileentity;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.Optional;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.apiculture.genetics.BeeGenome;
import magicbees.bees.BeeManager;
import magicbees.bees.BeeSpecies;
import magicbees.main.Config;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumicenergistics.api.storage.IAspectStorage;

@Optional.InterfaceList({
        @Optional.Interface(iface = "thaumicenergistics.api.storage.IAspectStorage", modid = "thaumicenergistics") })
public class TileEntityApimancersDrainerCommon extends TileEntity
        implements IEssentiaTransport, IAspectContainer, IAspectStorage {

    public Aspect aspect;
    public AspectList essentia = new AspectList();

    public final int maxAmount = Config.drainerCapacity;

    protected int increment = 0;

    public void setAspect(Aspect sAspect) {
        aspect = sAspect;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        // If there's no stored aspect we shouldn't even check for the above block.
        // We also don't need to run all the logic if the cabinet is full.
        if (aspect == null || essentia.visSize() == maxAmount) return;

        if (increment >= Config.drainerTimeBetween) {
            increment = 0;
            try {
                TileEntity above = worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
                IBeeHousing beeHousing = beeHousing(above);
                if (beeHousing != null) {
                    // This performs all the checks to see if the bee is a living queen and if the species conditions
                    // are met.
                    if (!canWork(beeHousing, above)) return;
                    // The beeRoot.isMember call will treat null the same as EnumBeeType.NONE which leads getSpecies to
                    // return null.
                    ItemStack queenStack = getQueen(beeHousing, above);
                    IAlleleBeeSpecies queenSpecies = BeeGenome.getSpecies(queenStack);
                    if (queenSpecies == null) return;
                    if (BeeManager.beeRoot.getType(queenStack) != EnumBeeType.QUEEN) return;
                    if (Objects.equals(queenSpecies.getUID(), BeeSpecies.TC_ESSENTIA.getSpecies().getUID())) {
                        IBeeModifier modifier = BeeManager.beeRoot.createBeeHousingModifier(beeHousing);
                        IBee queen = BeeManager.beeRoot.getMember(queenStack);
                        int amount = Config.drainerAmount * getProductionMultiplier(modifier, queen, above);
                        addToContainer(aspect, amount);
                        drainQueen(beeHousing, modifier, queen);
                    }
                }
            } catch (Exception ignored) {}
        }

        increment++;
    }

    protected IBeeHousing beeHousing(TileEntity above) {
        return above instanceof IBeeHousing ? (IBeeHousing) above : null;
    }

    protected boolean canWork(IBeeHousing beeHousing, TileEntity above) {
        IBeekeepingLogic beekeepingLogic = beeHousing.getBeekeepingLogic();
        return beekeepingLogic.canWork();
    }

    protected ItemStack getQueen(IBeeHousing beeHousing, TileEntity te) {
        return beeHousing.getBeeInventory().getQueen();
    }

    protected int getProductionMultiplier(IBeeModifier modifier, IBee queen, TileEntity te) {
        IBeekeepingMode mode = BeeManager.beeRoot.getBeekeepingMode(te.getWorldObj());

        IBeeGenome genome = queen.getGenome();
        float genomeSpeed = genome.getSpeed();

        float productionMultiplier = modifier.getProductionModifier(genome, 1.0F);
        productionMultiplier += mode != null ? mode.getBeeModifier().getProductionModifier(genome, productionMultiplier)
                : 0.0F;
        productionMultiplier += genomeSpeed;

        float minimum = Math.max(productionMultiplier, 1.0F);
        return (int) Math.ceil(minimum);
    }

    private void drainQueen(IBeeHousing housing, IBeeModifier modifier, IBee queen) {
        float lifespanModifier = modifier.getLifespanModifier(queen.getGenome(), queen.getMate(), 1.0f);
        queen.age(housing.getWorld(), lifespanModifier);
        // Write the changed queen back into the item stack.
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        queen.writeToNBT(nbttagcompound);
        housing.getBeeInventory().getQueen().setTagCompound(nbttagcompound);
    }

    @Override
    @Optional.Method(modid = "thaumicenergistics")
    public int getContainerCapacity() {
        return maxAmount;
    }

    @Override
    @Optional.Method(modid = "thaumicenergistics")
    public boolean doesShareCapacity() {
        return true;
    }

    public AspectList getAspects() {
        return this.essentia;
    }

    @Override
    public void setAspects(AspectList aspects) {
        this.essentia = aspects;
    }

    @Override
    public boolean doesContainerAccept(Aspect tag) {
        return false;
    }

    @Override
    public int addToContainer(Aspect tag, int am) {
        int toAdd = Math.min(maxAmount - essentia.visSize(), am);
        if (aspect.equals(tag) && toAdd > 0) {
            essentia.add(aspect, toAdd);
            markDirty();
            return am - toAdd;
        }
        markDirty();
        return am;
    }

    @Override
    public boolean takeFromContainer(Aspect tag, int amount) {
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        if (this.essentia.getAmount(tag) >= amount) {
            this.essentia.remove(tag, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList ot) {
        // TODO Auto-generated method stub
        boolean hasIt = true;
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        for (Aspect next : ot.aspects.keySet()) {
            if (this.essentia.getAmount(next) < ot.getAmount(next)) hasIt = false;
        }
        if (hasIt) {
            for (Aspect next : ot.aspects.keySet()) {
                essentia.remove(next, ot.getAmount(next));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        return this.essentia.getAmount(tag) >= amount;
    }

    @Override
    public boolean doesContainerContain(AspectList ot) {
        boolean hasIt = true;
        for (Aspect next : ot.aspects.keySet()) {
            if (this.essentia.getAmount(next) < ot.getAmount(next)) hasIt = false;
        }
        return hasIt;
    }

    @Override
    public int containerContains(Aspect tag) {
        return this.essentia.getAmount(tag);
    }

    @Override
    public boolean isConnectable(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    @Override
    public boolean canInputFrom(ForgeDirection face) {
        return false;
    }

    @Override
    public boolean canOutputTo(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
        if (face != ForgeDirection.UP) {
            if (!this.worldObj.isRemote) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            int total = this.essentia.getAmount(aspect);
            this.essentia.remove(aspect, amount); // if amount >= total then it will also remove aspect.
            if (amount > total) return total;
            return amount;
        }
        return 0;
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {}

    @Override
    public int getMinimumSuction() {
        return -1;
    }

    @Override
    public int getSuctionAmount(ForgeDirection face) {
        return -1;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }

    @Override
    public Aspect getSuctionType(ForgeDirection face) {
        return null;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection face) {
        return this.essentia.size() > 0
                ? this.essentia.getAspects()[this.worldObj.rand.nextInt(this.essentia.getAspects().length)]
                : null;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection face) {
        return this.essentia.visSize();
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
        return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
    }

    /* Saving and loading */
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        this.essentia.writeToNBT(compound);
        if (aspect != null) compound.setString("aspect", aspect.getTag());
        compound.setInteger("increment", increment);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.essentia.readFromNBT(compound);
        if (this.essentia.visSize() > this.maxAmount) {
            this.essentia = new AspectList();
        }
        if (compound.hasKey("aspect")) aspect = Aspect.getAspect(compound.getString("aspect"));
        increment = compound.getInteger("increment");
    }
}
