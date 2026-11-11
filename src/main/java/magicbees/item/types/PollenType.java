package magicbees.item.types;

import magicbees.main.utils.LocalizationManager;

public enum PollenType {

    UNUSUAL("unusual", 0xD8417B, 0xA03059),
    PHASED("phased", 0x4974B4, 0x456BA5);

    /**
     * Cached values() array for frequent read-only operations, the array should NOT be mutated.
     */
    public static final PollenType[] VALUES = values();
    private final String name;
    public final int colourA;
    public final int colourB;

    PollenType(String pName, int colourA, int colourB) {
        this.name = pName;
        this.colourA = colourA;
        this.colourB = colourB;
    }

    public String getName() {
        return LocalizationManager.getLocalizedString("pollen." + this.name);
    }
}
