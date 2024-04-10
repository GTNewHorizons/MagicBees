package magicbees.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.DefaultBeeModifier;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.apiculture.IHiveFrame;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.ForestryAPI;
import forestry.api.core.IErrorLogic;
import forestry.apiculture.ApiaryBeeListener;
import forestry.apiculture.IApiary;
import forestry.apiculture.inventory.IApiaryInventory;
import magicbees.api.bees.IMagicApiaryAuraProvider;
import magicbees.bees.AuraCharge;
import magicbees.bees.BeeManager;
import magicbees.bees.MagicApiaryInventory;
import magicbees.main.CommonProxy;
import magicbees.main.utils.ChunkCoords;
import magicbees.main.utils.net.EventAuraChargeUpdate;
import magicbees.main.utils.net.NetworkEventHandler;

public class TileEntityMagicApiary extends TileEntity implements ISidedInventory, IApiary, ITileEntityAuraCharged {

    // Constants
    private static final int AURAPROVIDER_SEARCH_RADIUS = 6;
    public static final String tileEntityName = CommonProxy.DOMAIN + ".magicApiary";

    private GameProfile ownerProfile;
    private IMagicApiaryAuraProvider auraProvider;
    private ChunkCoords auraProviderPosition;
    private BiomeGenBase biome;
    private int breedingProgressPercent = 0;

    private final IBeekeepingLogic beeLogic = BeeManager.beeRoot.createBeekeepingLogic(this);
    private final IBeeListener beeListener = new ApiaryBeeListener(this);
    private final IBeeModifier beeModifier = new MagicApiaryBeeModifier(this);
    private final MagicApiaryInventory inventory = new MagicApiaryInventory(this);
    private final IErrorLogic errorLogic = ForestryAPI.errorStateRegistry.createErrorLogic();
    private final AuraCharges auraCharges = new AuraCharges();

    @Override
    public Iterable<IBeeModifier> getBeeModifiers() {
        List<IBeeModifier> beeModifiers = new ArrayList<IBeeModifier>();

        beeModifiers.add(beeModifier);

        for (IHiveFrame frame : inventory.getFrames()) {
            beeModifiers.add(frame.getBeeModifier());
        }

        return beeModifiers;
    }

    @Override
    public Iterable<IBeeListener> getBeeListeners() {
        return Collections.singleton(beeListener);
    }

    @Override
    public IBeeHousingInventory getBeeInventory() {
        return inventory;
    }

    @Override
    public IApiaryInventory getApiaryInventory() {
        return inventory;
    }

    @Override
    public IBeekeepingLogic getBeekeepingLogic() {
        return beeLogic;
    }

    @Override
    public IErrorLogic getErrorLogic() {
        return errorLogic;
    }

    @Override
    public GameProfile getOwner() {
        return this.ownerProfile;
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public ChunkCoordinates getCoordinates() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    @Override
    public Vec3 getBeeFXCoordinates() {
        return Vec3.createVectorHelper(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
    }

    @Override
    public BiomeGenBase getBiome() {
        if (biome == null) {
            biome = worldObj.getBiomeGenForCoordsBody(xCoord, zCoord);
        }
        return biome;
    }

    @Override
    public EnumTemperature getTemperature() {
        return EnumTemperature.getFromBiome(getBiome(), xCoord, yCoord, zCoord);
    }

    @Override
    public EnumHumidity getHumidity() {
        return EnumHumidity.getFromValue(getExactHumidity());
    }

    @Override
    public int getBlockLightValue() {
        return worldObj.getBlockLightValue(xCoord, yCoord + 1, zCoord);
    }

    @Override
    public boolean canBlockSeeTheSky() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        ItemStack itemStack = getStackInSlot(i);

        if (itemStack != null) {
            if (itemStack.stackSize <= j) {
                setInventorySlotContents(i, null);
            } else {
                itemStack = itemStack.splitStack(j);
                markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        ItemStack item = getStackInSlot(i);
        setInventorySlotContents(i, null);
        return item;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.setInventorySlotContents(i, itemStack);
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return tileEntityName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return entityPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    public int getHealthScaled(int i) {
        return (breedingProgressPercent * i) / 100;
    }

    /* Saving and loading */
    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        inventory.writeToNBT(compound);
        beeLogic.writeToNBT(compound);
        ChunkCoords.writeToNBT(auraProviderPosition, compound);
        auraCharges.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.readFromNBT(compound);
        beeLogic.readFromNBT(compound);
        auraProviderPosition = ChunkCoords.readFromNBT(compound);
        auraCharges.readFromNBT(compound);
    }

    @Override
    public Packet getDescriptionPacket() {
        beeLogic.syncToClient();
        EventAuraChargeUpdate event = new EventAuraChargeUpdate(new ChunkCoords(this), auraCharges);
        return event.getPacket();
    }

    public float getExactHumidity() {
        return getBiome().rainfall;
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            updateClientSide();
        } else {
            updateServerSide();
        }
    }

    public void updateClientSide() {
        if (beeLogic.canDoBeeFX() && worldObj.getTotalWorldTime() % 10 == 0) {
            beeLogic.doBeeFX();
        }
    }

    public void updateServerSide() {
        if (this.auraProvider == null) {
            findAuraProvider();
        } else {
            updateAuraProvider();
        }
        tickCharges();

        if (beeLogic.canWork()) {
            beeLogic.doWork();
        }
    }

    public void getGUINetworkData(int i, int j) {
        switch (i) {
            case 0:
                breedingProgressPercent = j;
                break;
        }
    }

    public void sendGUINetworkData(Container container, ICrafting iCrafting) {
        iCrafting.sendProgressBarUpdate(container, 0, beeLogic.getBeeProgressPercent());
    }

    public boolean isProductionBoosted() {
        return auraCharges.isActive(AuraCharge.PRODUCTION);
    }

    public boolean isDeathRateBoosted() {
        return auraCharges.isActive(AuraCharge.DEATH);
    }

    public boolean isMutationBoosted() {
        return auraCharges.isActive(AuraCharge.MUTATION);
    }

    private void updateAuraProvider() {
        if (worldObj.getTotalWorldTime() % 10 != 0) {
            return;
        }
        if (getAuraProvider(auraProviderPosition) == null) {
            this.auraProvider = null;
            this.auraProviderPosition = null;
            return;
        }

        boolean auraChargesChanged = false;
        for (AuraCharge charge : AuraCharge.values()) {
            if (!auraCharges.isActive(charge) && auraProvider.getCharge(charge.type)) {
                auraCharges.start(charge, worldObj);
                auraChargesChanged = true;
            }
        }

        if (auraChargesChanged) {
            NetworkEventHandler.getInstance().sendAuraChargeUpdate(this, auraCharges);
        }
    }

    private void tickCharges() {
        boolean auraChargesChanged = false;

        for (AuraCharge charge : AuraCharge.values()) {
            if (auraCharges.isActive(charge) && auraCharges.isExpired(charge, worldObj)
                    && (auraProvider == null || !auraProvider.getCharge(charge.type))) {
                auraCharges.stop(charge);
                auraChargesChanged = true;
            }
        }

        if (auraChargesChanged) {
            NetworkEventHandler.getInstance().sendAuraChargeUpdate(this, auraCharges);
        }
    }

    private void findAuraProvider() {
        if (worldObj.getTotalWorldTime() % 5 != 0) {
            return;
        }

        if (this.auraProviderPosition == null) {
            List<Chunk> chunks = getChunksInSearchRange();
            for (Chunk chunk : chunks) {
                if (searchChunkForBooster(chunk)) {
                    break;
                }
            }
        } else {
            this.auraProvider = getAuraProvider(auraProviderPosition);
            if (auraProvider == null) {
                this.auraProviderPosition = null;
            }
        }
    }

    private List<Chunk> getChunksInSearchRange() {
        List<Chunk> chunks = new ArrayList<Chunk>(4);
        chunks.add(
                worldObj.getChunkFromBlockCoords(
                        xCoord - AURAPROVIDER_SEARCH_RADIUS,
                        zCoord - AURAPROVIDER_SEARCH_RADIUS));
        Chunk chunk = worldObj
                .getChunkFromBlockCoords(xCoord + AURAPROVIDER_SEARCH_RADIUS, zCoord - AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        chunk = worldObj
                .getChunkFromBlockCoords(xCoord - AURAPROVIDER_SEARCH_RADIUS, zCoord + AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        chunk = worldObj
                .getChunkFromBlockCoords(xCoord + AURAPROVIDER_SEARCH_RADIUS, zCoord + AURAPROVIDER_SEARCH_RADIUS);
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        }
        return chunks;
    }

    @SuppressWarnings("unchecked")
    private boolean searchChunkForBooster(Chunk chunk) {
        Vec3 apiaryPos = Vec3.createVectorHelper(xCoord, yCoord, zCoord);
        for (Map.Entry<ChunkPosition, TileEntity> entry : ((Map<ChunkPosition, TileEntity>) chunk.chunkTileEntityMap)
                .entrySet()) {
            TileEntity entity = entry.getValue();
            if (entity instanceof IMagicApiaryAuraProvider) {
                Vec3 tePos = Vec3.createVectorHelper(entity.xCoord, entity.yCoord, entity.zCoord);
                Vec3 result = apiaryPos.subtract(tePos);
                if (result.lengthVector() <= AURAPROVIDER_SEARCH_RADIUS) {
                    saveAuraProviderPosition(entity.xCoord, entity.yCoord, entity.zCoord);
                    this.auraProvider = (IMagicApiaryAuraProvider) entity;
                    return true;
                }
            }
        }
        return false;
    }

    private void saveAuraProviderPosition(int x, int y, int z) {
        auraProviderPosition = new ChunkCoords(worldObj.provider.dimensionId, x, y, z);
    }

    private IMagicApiaryAuraProvider getAuraProvider(ChunkCoords coords) {
        return getAuraProvider(coords.x, coords.y, coords.z);
    }

    private IMagicApiaryAuraProvider getAuraProvider(int x, int y, int z) {
        Chunk chunk = worldObj.getChunkFromBlockCoords(x, z);
        x %= 16;
        z %= 16;
        if (x < 0) {
            x += 16;
        }
        if (z < 0) {
            z += 16;
        }
        ChunkPosition cPos = new ChunkPosition(x, y, z);
        TileEntity entity = (TileEntity) chunk.chunkTileEntityMap.get(cPos);
        if (!(entity instanceof IMagicApiaryAuraProvider)) {
            return null;
        }
        return (IMagicApiaryAuraProvider) entity;
    }

    @Override
    public AuraCharges getAuraCharges() {
        return auraCharges;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i) {
        return inventory.getAccessibleSlotsFromSide(i);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i1) {
        return inventory.canInsertItem(i, itemStack, i1);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i1) {
        return inventory.canExtractItem(i, itemStack, i1);
    }

    private static class MagicApiaryBeeModifier extends DefaultBeeModifier {

        private final TileEntityMagicApiary magicApiary;

        public MagicApiaryBeeModifier(TileEntityMagicApiary magicApiary) {
            this.magicApiary = magicApiary;
        }

        @Override
        public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return magicApiary.isMutationBoosted() ? 2f : 1f;
        }

        @Override
        public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return magicApiary.isDeathRateBoosted() ? 2f : 1f;
        }

        @Override
        public float getProductionModifier(IBeeGenome genome, float currentModifier) {
            return magicApiary.isProductionBoosted() ? 0.8f : -0.1f;
        }

        @Override
        public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
            return 0.8f;
        }
    }
}
