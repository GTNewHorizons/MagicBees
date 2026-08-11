package magicbees.main.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockUtil {

    public static int getSurroundCount(World world, int x, int y, int z, Block blockType) {
        int surroundCount = 0;

        if (canBlockReplaceAt(world, x + 1, y, z, blockType)) {
            ++surroundCount;
        }

        if (canBlockReplaceAt(world, x - 1, y, z, blockType)) {
            ++surroundCount;
        }

        if (canBlockReplaceAt(world, x, y + 1, z, blockType)) {
            ++surroundCount;
        }

        if (canBlockReplaceAt(world, x, y - 1, z, blockType)) {
            ++surroundCount;
        }

        if (canBlockReplaceAt(world, x, y, z + 1, blockType)) {
            ++surroundCount;
        }

        if (canBlockReplaceAt(world, x, y, z - 1, blockType)) {
            ++surroundCount;
        }

        return surroundCount;
    }

    public static boolean canBlockReplaceAt(World world, int x, int y, int z, Block target) {
        // don't cause new chunks to load
        if (!world.blockExists(x, y, z)) {
            return true;
        }
        Block block = world.getBlock(x, y, z);
        return block.isReplaceableOreGen(world, x, y, z, target);
    }

    public static List<Chunk> getChunksInSearchRange(World world, int xCoord, int zCoord, int searchRadius) {
        List<Chunk> searchChunks = new ArrayList<Chunk>();
        int chunkRange = (int) Math.ceil(searchRadius / 16.0);
        int minChunkX = xCoord / 16 - chunkRange;
        int maxChunkX = xCoord / 16 + chunkRange;
        int minChunkZ = zCoord / 16 - chunkRange;
        int maxChunkZ = zCoord / 16 + chunkRange;

        for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
                if (world.blockExists(chunkX << 4, 0, chunkZ << 4)) {
                    continue;
                }
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
                if (chunk != null) searchChunks.add(chunk);
            }
        }

        return searchChunks;
    }
}
