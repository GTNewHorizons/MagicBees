package magicbees.main.utils.compat;

import cpw.mods.fml.common.Loader;
import magicbees.main.Config;

public class ThaumicHorizonsHelper implements IModHelper {

    private static boolean isTHorizonsActive = false;
    public static final String Name = "ThaumicHorizons";

    public static boolean isActive() {
        return isTHorizonsActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.thaumicHorizonsActive) {
            isTHorizonsActive = true;
        }
    }

    public void init() {}

    public void postInit() {}
}
