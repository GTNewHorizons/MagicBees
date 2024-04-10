package magicbees.bees.allele.effect;

import static magicbees.main.utils.compat.thaumcraft.NodeHelper.updateNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import forestry.api.apiculture.IAlleleBeeAcceleratableEffect;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.genetics.IEffectData;
import magicbees.bees.AlleleEffect;
import magicbees.bees.BeeManager;
import magicbees.main.utils.BlockUtil;
import magicbees.main.utils.compat.thaumcraft.NodeHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;

public class AlleleEffectRecharge extends AlleleEffect implements IAlleleBeeAcceleratableEffect {

    public AlleleEffectRecharge(String id, boolean isDominant) {
        super(id, isDominant, 20);
        combinable = true;
    }

    @Override
    public IEffectData validateStorage(IEffectData storedData) {
        if (!(storedData instanceof EffectData)) {
            storedData = new magicbees.bees.allele.effect.EffectData(1, 0, 0);
        }
        return storedData;
    }

    @Override
    protected IEffectData doEffectThrottled(IBeeGenome genome, IEffectData storedData, IBeeHousing housing) {
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
        int range = (int) Math.ceil(genome.getTerritory()[0] * beeModifier.getTerritoryModifier(genome, 1f));
        List<Chunk> chunks = BlockUtil.getChunksInSearchRange(world, coords.posX, coords.posZ, range);

        if (NodeHelper.rechargeNodeInRange(chunks, world, coords.posX, coords.posY, coords.posZ, range)) {
            storedData.setInteger(0, storedData.getInteger(0) - throttle);
        }

        return storedData;
    }

    @Override
    public IEffectData doEffectAccelerated(IBeeGenome genome, IEffectData storedData, IBeeHousing housing, float did) {
        storedData.setInteger(0, 0);
        World world = housing.getWorld();
        ChunkCoordinates coords = housing.getCoordinates();
        IBeeModifier beeModifier = BeeManager.beeRoot.createBeeHousingModifier(housing);
        int range = Math
                .max((int) Math.ceil(genome.getTerritory()[0] * beeModifier.getTerritoryModifier(genome, 1f)), 1);
        List<Chunk> chunks = BlockUtil.getChunksInSearchRange(world, coords.posX, coords.posZ, range);

        int idid = (int) did;

        List<INode> nodes = new ArrayList<>(idid);
        Collections.shuffle(chunks);
        for (Chunk chunk : chunks) {
            List<INode> l = NodeHelper.findNodesInChunkWithinRange(chunk, coords.posX, coords.posY, coords.posZ, range);
            nodes.addAll(l);
            idid -= l.size();
            if (idid <= 0) {
                while (idid < 0) {
                    nodes.remove(0);
                    idid++;
                }
                break;
            }
        }

        if (nodes.isEmpty()) return storedData;

        did /= (float) nodes.size();
        if (did < 1f) did = 1f;

        for (INode node : nodes) {
            AspectList baseAspects = node.getAspectsBase();
            AspectList currentAspects = node.getAspects();

            HashMap<Aspect, Integer> toAdd = new HashMap<>();
            int total = 0;

            for (Map.Entry<Aspect, Integer> aspect : baseAspects.aspects.entrySet()) {
                int diff = aspect.getValue() - currentAspects.getAmount(aspect.getKey());
                if (diff <= 0) continue;
                toAdd.put(aspect.getKey(), diff);
                total += diff;
            }

            if (total == 0) continue;

            int amount = Math.min(total + world.rand.nextInt(total), (int) did);
            if (amount <= 0) amount = 1;

            for (Map.Entry<Aspect, Integer> aspect : toAdd.entrySet()) {
                currentAspects.add(aspect.getKey(), Math.min(aspect.getValue(), amount));
            }

            updateNode(node, world);
        }

        return storedData;
    }
}
