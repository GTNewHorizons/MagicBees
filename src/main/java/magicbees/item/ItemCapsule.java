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
import magicbees.item.types.CapsuleType;
import magicbees.item.types.FluidType;
import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;

public class ItemCapsule extends Item {

    private CapsuleType capsuleType;

    public ItemCapsule(CapsuleType type, int maxStackSize) {
        super();
        this.capsuleType = type;
        this.setCreativeTab(TabMagicBees.tabMagicBees);
        this.setHasSubtypes(true);
        this.setMaxStackSize(maxStackSize);
        this.setUnlocalizedName("capsule." + type.toString().toLowerCase());
        GameRegistry.registerItem(this, "capsule." + type.toString().toLowerCase());
    }

    public CapsuleType getType() {
        return this.capsuleType;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return String.format(
                this.capsuleType.getLocalizedName(),
                FluidType.VALUES[itemStack.getItemDamage()].getDisplayName());
    }

    public ItemStack getCapsuleForLiquid(FluidType l) {
        return new ItemStack(this, 1, l.ordinal());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
        for (FluidType l : FluidType.VALUES) {
            if (l.available) {
                itemList.add(new ItemStack(this, 1, l.ordinal()));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(
                CommonProxy.DOMAIN + ":capsule"
                        + this.capsuleType.getName().substring(0, 1).toUpperCase()
                        + this.capsuleType.getName().substring(1));
        for (FluidType t : FluidType.VALUES) {
            if (t != FluidType.EMPTY) {
                t.liquidIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":liquids/" + t.liquidID);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int metadata, int pass) {
        IIcon i = this.itemIcon;
        if (metadata != 0 && pass == 0) {
            i = FluidType.VALUES[metadata % FluidType.VALUES.length].liquidIcon;
        }
        return i;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return (metadata == 0) ? 1 : 2;
    }
}
