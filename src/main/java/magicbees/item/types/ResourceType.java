package magicbees.item.types;

import magicbees.main.utils.LocalizationManager;

public enum ResourceType {

    LORE_FRAGMENT("fragment", true),
    AROMATIC_LUMP("lump", true),
    EXTENDED_FERTILIZER("fertilizer", true),
    SKULL_CHIP("skullChip", true),
    SKULL_FRAGMENT("skullFragment", true),
    DRAGON_DUST("dragonDust", true),
    DRAGON_CHUNK("dragonChunk", true),
    ESSENCE_FALSE_LIFE("essenceLife", true), //
    ESSENCE_SHALLOW_GRAVE("essenceDeath", true), //
    ESSENCE_LOST_TIME("essenceTime", true),
    ESSENCE_EVERLASTING_DURABILITY("essenceDurability", true), //
    ESSENCE_SCORNFUL_OBLIVION("essenceOblivion", true),
    ESSENCE_FICKLE_PERMANENCE("essenceMutable", true), //
    z_2("lump", false), // Using lump for the name to avoid missing texture warnings.
    z_3("lump", false),
    z_4("lump", false),
    z_5("lump", false),
    DIMENSIONAL_SINGULARITY("dimensionalSingularity", true),
    TC_DUST_AIR("TCairDust", true),
    TC_DUST_WATER("TCwaterDust", true),
    TC_DUST_FIRE("TCfireDust", true),
    TC_DUST_EARTH("TCearthDust", true),
    TC_DUST_ORDER("TCorderDust", true),
    TC_DUST_CHAOS("TCchaosDust", true),

    // Dummy items for Thaumanomicon research icons.
    // These won't ever actually exist in-game, and so they can be moved around.
    RESEARCH_STARTNODE("startNode", false),
    RESEARCH_BEEINFUSION("beeInfusion", false);

    /**
     * Cached values() array for frequent read-only operations, the array should NOT be mutated.
     */
    public static final ResourceType[] VALUES = values();
    private final String name;
    public boolean showInList;

    ResourceType(String n, boolean show) {
        this.name = n;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
        return this.name;
    }

    public String getLocalizedName() {
        return LocalizationManager.getLocalizedString("resource." + this.name);
    }
}
