package magicbees.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.client.gui.UIScreens;
import magicbees.main.CommonProxy;
import magicbees.main.MagicBees;
import magicbees.main.utils.TabMagicBees;
import magicbees.tileentity.TileEntityMagicApiary;

public class BlockMagicApiary extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockMagicApiary() {
        super(Material.wood);
        this.setCreativeTab(TabMagicBees.tabMagicBees);
        this.setBlockName("magicApiary");
        this.setHardness(1f);
        this.setResistance(1.5f);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        boolean activate = false;

        if (!player.isSneaking()) {
            if (!world.isRemote) {
                player.openGui(MagicBees.object, UIScreens.THAUMIC_APIARY.ordinal(), world, x, y, z);
            }
            activate = true;
        }

        return activate;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (side == 0) {
            return icons[0];
        } else if (side == 1) {
            return icons[1];
        } else if (side == 2 || side == 3) {
            return icons[2];
        } else {
            return icons[3];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[4];

        icons[0] = register.registerIcon(CommonProxy.DOMAIN + ":thaumicapiary.0");
        icons[1] = register.registerIcon(CommonProxy.DOMAIN + ":thaumicapiary.1");
        icons[2] = register.registerIcon(CommonProxy.DOMAIN + ":thaumicapiary.2");
        icons[3] = register.registerIcon(CommonProxy.DOMAIN + ":thaumicapiary.4");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityMagicApiary();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IInventory) {
            IInventory inventory = (IInventory) te;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack itemStack = inventory.getStackInSlotOnClosing(i);

                if (itemStack != null) {
                    float spawnX = x + world.rand.nextFloat();
                    float spawnY = y + world.rand.nextFloat();
                    float spawnZ = z + world.rand.nextFloat();

                    EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, itemStack);

                    float mult = 0.05F;

                    droppedItem.motionX = (-0.5F + world.rand.nextFloat()) * mult;
                    droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
                    droppedItem.motionZ = (-0.5F + world.rand.nextFloat()) * mult;

                    world.spawnEntityInWorld(droppedItem);
                }
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }
}
