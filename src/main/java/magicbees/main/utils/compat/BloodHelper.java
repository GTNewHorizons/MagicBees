package magicbees.main.utils.compat;

import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.Config;
import magicbees.main.utils.compat.bloodmagic.ItemBloodFrame;
import magicbees.main.utils.compat.bloodmagic.ItemFrenziedFrame;

public class BloodHelper implements IModHelper {

    @SideOnly(Side.CLIENT)
    public static IIcon subtileIcons[];

    private static boolean isBloodMagicActive = false;
    public static final String Name = "AWWayofTime";

    public static boolean isActive() {
        return isBloodMagicActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name)) {
            isBloodMagicActive = true;
        }
    }

    public void init() {}

    public void postInit() {}

    public static void getBlocks() {}

    public static void getItems() {
        Config.hiveFrameBlood = new ItemBloodFrame();
        GameRegistry
                .registerItem(Config.hiveFrameBlood, Config.hiveFrameBlood.getUnlocalizedName(), CommonProxy.DOMAIN);

        Config.hiveFrameFrenzy = new ItemFrenziedFrame();
        GameRegistry
                .registerItem(Config.hiveFrameFrenzy, Config.hiveFrameFrenzy.getUnlocalizedName(), CommonProxy.DOMAIN);
    }

    public static void doBloodMagicModuleConfigs(Configuration configuration) {
        Property p;
    }

}
