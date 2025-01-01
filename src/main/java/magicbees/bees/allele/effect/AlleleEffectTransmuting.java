package magicbees.bees.allele.effect;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;

public class AlleleEffectTransmuting extends AlleleEffect {

    private TransmutationEffectController transmutationController;

    public AlleleEffectTransmuting(String id, boolean isDominant, TransmutationEffectController effectController,
            int timeoutBeeTicks) {
        super(id, isDominant, timeoutBeeTicks);
        this.transmutationController = effectController;
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (storedData == null || !(storedData instanceof magicbees.bees.allele.effect.EffectData)) {
            storedData = new magicbees.bees.allele.effect.EffectData(1, 0, 0);
        }
        return storedData;
    }

    @Override
    protected IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);

        // Get random coords within territory
        int[] randomCoords = new int[3];
        for (int i = 0; i < 3; i++) {
            int range = (int) (beeModifier.getTerritoryModifier(genome, 1f) * genome.getTerritory()[i]);
            if (range > 0) {
                randomCoords[i] = world.rand.nextInt(range) - range / 2;
            }
        }

        int xCoord = coords.posX + randomCoords[0];
        int yCoord = coords.posY + randomCoords[1];
        int zCoord = coords.posZ + randomCoords[2];

        BiomeGenBase biome = world.getBiomeGenForCoords(xCoord, zCoord);
        transmutationController.attemptTransmutations(
                world,
                biome,
                new ItemStack(
                        world.getBlock(xCoord, yCoord, zCoord),
                        1,
                        world.getBlockMetadata(xCoord, yCoord, zCoord)),
                xCoord,
                yCoord,
                zCoord);

        storedData.setInteger(0, 0);

        return storedData;
    }
}
