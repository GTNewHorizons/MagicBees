package magicbees.item.types;

import net.minecraft.util.IIcon;

import magicbees.main.utils.LocalizationManager;

public enum FluidType {
    // Incidentally, Item meta is the liquid ID.

    // Args are: forgeLiquidID
    EMPTY(""),

    // Vanilla
    WATER("water"),
    LAVA("lava"),

    // Forestry
    BIOMASS("biomass"),
    ETHANOL("bioethanol"),

    // Buildcraft
    OIL("oil"),
    FUEL("fuel"),

    // More Forestry
    SEEDOIL("seedoil"),
    HONEY("honey"),
    JUICE("juice"),
    CRUSHEDICE("ice"),
    MILK("milk"),

    // ExtraBees liquids
    ACID("acid"),
    POISON("poison"),
    LIQUIDNITROGEN("liquidnitrogen"),
    DNA("liquiddna"),

    // Railcraft
    CREOSOTEOIL("creosote"),
    STEAM("steam"),

    // Thermal Foundation
    REDSTONE("redstone"),
    GLOWSTONE("glowstone"),
    ENDER("ender"),
    PYROTHEUM("pyrotheum"),
    CRYOTHEUM("cryotheum"),
    MANA("mana"),
    COAL("coal"),;

    /**
     * Cached values() array for frequent read-only operations, the array should NOT be mutated.
     */
    public static final FluidType[] VALUES = values();
    public final String liquidID;
    public int iconIdx;
    public boolean available = false;
    public IIcon liquidIcon;

    FluidType(String l) {
        this.liquidID = l;
    }

    public String getDisplayName() {
        return LocalizationManager.getLocalizedString("liquid." + liquidID);
    }
}
