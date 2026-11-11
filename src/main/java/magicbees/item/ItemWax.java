package magicbees.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import magicbees.item.types.WaxType;
import magicbees.main.CommonProxy;

public class ItemWax extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondaryIcon;

    public ItemWax() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("wax");
        GameRegistry.registerItem(this, "wax");
    }

    public ItemStack getStackForType(WaxType type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(WaxType type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(CommonProxy.DOMAIN + ":wax.0");
        this.secondaryIcon = par1IconRegister.registerIcon(CommonProxy.DOMAIN + ":wax.1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return (WaxType.VALUES[meta].sparkly) ? this.itemIcon : this.secondaryIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (WaxType type : WaxType.VALUES) {
            list.add(this.getStackForType(type));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int colour = 0xffffff;
        int meta = stack.getItemDamage();
        if (meta >= 0 && meta < WaxType.VALUES.length) {
            colour = WaxType.VALUES[meta].colour;
        }
        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return WaxType.VALUES[stack.getItemDamage()].getName();
    }
}
