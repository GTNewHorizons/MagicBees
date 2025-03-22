package magicbees.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;

public class BlockEnchantedEarth extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon top;

    public BlockEnchantedEarth() {
        super(Material.ground);
        setCreativeTab(TabMagicBees.tabMagicBees);
        setBlockName("enchantedEarth");
        setBlockTextureName(CommonProxy.DOMAIN + ":enchantedEarth");
        setTickRandomly(true);
        setStepSound(soundTypeGravel);
        setHardness(0.1F);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        for (int height = y + 1; height <= 256; ++height) {
            Block b = world.getBlock(x, height, z);
            if (b == this) {
                continue;
            } else {
                if (b.getTickRandomly()) {
                    world.scheduleBlockUpdate(x, height, z, b, 0);
                }
                break;
            }
        }
        if (y < 256 && random.nextInt(2) == 0) {
            int newX = x + (random.nextInt(3) - 1);
            int newZ = z + (random.nextInt(3) - 1);
            int newY = y + 1;
            Block b = world.getBlock(newX, newY, newZ);
            if (b.getTickRandomly()) {
                world.scheduleBlockUpdate(newX, newY, newZ, b, random.nextInt(5) + 1);
            }
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
            IPlantable plantable) {
        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return top;
        }
        return super.getIcon(side, meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        top = iconRegister.registerIcon(textureName + "_top");
        super.registerBlockIcons(iconRegister);
    }
}
