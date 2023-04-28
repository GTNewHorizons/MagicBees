package magicbees.main.utils.compat;

import magicbees.main.Config;

import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BloodHelper implements IModHelper {

    @SideOnly(Side.CLIENT)
    public static IIcon subtileIcons[];

    private static boolean isBloodMagicActive = false;
    public static final String Name = "bloodmagic";

    public static boolean isActive() {
        return isBloodMagicActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.bloodMagicActive) {
            isBloodMagicActive = true;
        }
    }

    public void init() {}

    public void postInit() {}

    public static void getBlocks() {}

    public static void getItems() {}

    public static void doBloodMagicModuleConfigs(Configuration configuration) {
        Property p;
    }

}
