package magicbees.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import magicbees.block.types.HiveType;
import magicbees.main.Config;

public class BlockHive extends Block {

    public BlockHive() {
        super(new MaterialHive());
        this.setLightLevel(0.8f);
        setHardness(1f);
        setCreativeTab(Tabs.tabApiculture);
        setBlockName("hive");
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta < 0 || meta > HiveType.VALUES.length) {
            meta = 0;
        }
        return HiveType.getHiveFromMeta(meta).getLightValue();
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List itemsList) {
        // Java bitches about raw types & types not being checked. Sorry, but the API sucks.
        for (HiveType type : HiveType.VALUES) {
            if (type.show || Config.forestryDebugEnabled) {
                itemsList.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return HiveType.getHiveFromMeta(metadata).getDrops(world, x, y, z, fortune);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        HiveType.registerIcons(register);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > HiveType.VALUES.length) {
            meta = 0;
        }
        return HiveType.getHiveFromMeta(meta).getIconForSide(side);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        if (block.isAir(world, x, y, z)) {
            return null;
        }

        Item item = Item.getItemFromBlock(block);
        if (item == null) {
            return null;
        }

        return new ItemStack(item, 1, getDamageValue(world, x, y, z) & 7);
    }
}
