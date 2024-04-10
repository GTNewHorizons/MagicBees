package magicbees.main.utils.compat.baubles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;

import forestry.api.apiculture.DefaultBeeListener;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IErrorLogic;
import forestry.api.core.IErrorState;
import magicbees.bees.BeeManager;

// Class that simulates a BeeHouse using both player data and info from InventoryBeeRing
public class RingHousing implements IBeeHousing {

    private final EntityPlayer player;
    private final BiomeGenBase biome;
    private final IBeeHousingInventory inventory;
    private final IBeekeepingLogic beekeepingLogic;

    private static final IErrorLogic errorLogic = new RingHousing.RingErrorLogic();
    private static final Iterable<IBeeListener> beeListeners = ImmutableSet.of(new DefaultBeeListener());
    private static final Iterable<IBeeModifier> beeModifiers = ImmutableSet.of(new RingHousing.RingHousingModifier());

    public RingHousing(EntityPlayer player, InventoryBeeRing inventoryBeeRing) {
        this.player = player;
        this.biome = player.getEntityWorld().getBiomeGenForCoords((int) player.posX, (int) player.posY);
        this.inventory = new RingBeeHousingInventory(inventoryBeeRing);
        this.beekeepingLogic = BeeManager.beeRoot.createBeekeepingLogic(this);
    }

    @Override
    public Iterable<IBeeModifier> getBeeModifiers() {
        return beeModifiers;
    }

    @Override
    public Iterable<IBeeListener> getBeeListeners() {
        return beeListeners;
    }

    @Override
    public IBeeHousingInventory getBeeInventory() {
        return inventory;
    }

    @Override
    public IBeekeepingLogic getBeekeepingLogic() {
        return beekeepingLogic;
    }

    @Override
    public int getBlockLightValue() {
        return player.getEntityWorld()
                .getBlockLightValue((int) player.posX, ((int) player.posY) + 1, (int) player.posZ);
    }

    @Override
    public boolean canBlockSeeTheSky() {
        return player.getEntityWorld().canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public World getWorld() {
        return player.getEntityWorld();
    }

    @Override
    public GameProfile getOwner() {
        return player.getGameProfile();
    }

    @Override
    public Vec3 getBeeFXCoordinates() {
        return Vec3.createVectorHelper(player.posX, player.posY + 0.5, player.posZ);
    }

    @Override
    public BiomeGenBase getBiome() {
        return this.biome;
    }

    @Override
    public EnumTemperature getTemperature() {
        return EnumTemperature.getFromBiome(biome, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public EnumHumidity getHumidity() {
        return EnumHumidity.getFromValue(biome.rainfall);
    }

    @Override
    public IErrorLogic getErrorLogic() {
        return errorLogic;
    }

    @Override
    public ChunkCoordinates getCoordinates() {
        return player.getPlayerCoordinates();
    }

    private static class RingBeeHousingInventory implements IBeeHousingInventory {

        private final InventoryBeeRing inventoryBeeRing;

        public RingBeeHousingInventory(InventoryBeeRing IBR) {
            this.inventoryBeeRing = IBR;
        }

        @Override
        public ItemStack getQueen() {
            return inventoryBeeRing.getQueen();
        }

        @Override
        public ItemStack getDrone() {
            return null;
        }

        @Override
        public void setQueen(ItemStack itemStack) {
            inventoryBeeRing.setQueen(itemStack);
        }

        @Override
        public void setDrone(ItemStack itemstack) {}

        @Override
        public boolean addProduct(ItemStack product, boolean all) {
            return false;
        }
    }

    private static class RingErrorLogic implements IErrorLogic {

        @Override
        public boolean setCondition(boolean condition, IErrorState errorState) {
            return condition;
        }

        @Override
        public boolean contains(IErrorState state) {
            return false;
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public void clearErrors() {}

        @Override
        public void writeData(DataOutputStream data) throws IOException {}

        @Override
        public void readData(DataInputStream data) throws IOException {}

        @Override
        public ImmutableSet<IErrorState> getErrorStates() {
            return ImmutableSet.of();
        }
    }

    private static class RingHousingModifier implements IBeeModifier {

        @Override
        public float getTerritoryModifier(IBeeGenome genome, float currentModifier) {
            return 0.9f;
        }

        @Override
        public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 0f;
        }

        @Override
        public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 0f;
        }

        @Override
        public float getProductionModifier(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public float getFloweringModifier(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
            return 0f;
        }

        @Override
        public boolean isSealed() {
            return true;
        }

        @Override
        public boolean isSelfLighted() {
            return true;
        }

        @Override
        public boolean isSunlightSimulated() {
            return true;
        }

        @Override
        public boolean isHellish() {
            return false;
        }
    }
}
