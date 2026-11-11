package magicbees.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import magicbees.item.types.PropolisType;
import magicbees.main.utils.compat.ForestryHelper;

public class ItemPropolis extends Item {

    public ItemPropolis() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("propolis");
        GameRegistry.registerItem(this, "propolis");
    }

    public ItemStack getStackForType(PropolisType type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(PropolisType type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (PropolisType type : PropolisType.VALUES) {
            list.add(this.getStackForType(type));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(ForestryHelper.Name.toLowerCase() + ":propolis.0");
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int colour = 0xffffff;
        int meta = stack.getItemDamage();
        if (meta >= 0 && meta < PropolisType.VALUES.length) {
            colour = PropolisType.VALUES[meta].colour;
        }
        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return PropolisType.VALUES[stack.getItemDamage()].getName();
    }
}
