package magicbees.item.types;

import magicbees.main.utils.LocalizationManager;

public enum DropType {

    ENCHANTED("enchanting", 0x6e1c6d, 0xff8fff),
    INTELLECT("intellect", 0x25914D, 0x18E072),
    DESTABILIZED("destabilized", 0xCC002C, 0x6B0118),
    CARBON("carbon", 0x454545, 0x0F0F0F),
    LUX("lux", 0xF5F3A4, 0xC9C87D),
    ENDEARING("endearing", 0x12E3D9, 0x069E97),;

    public static final DropType[] VALUES = values();

    DropType(String pName, int colourA, int colourB) {
        this.name = pName;
        this.combColour[0] = colourA;
        this.combColour[1] = colourB;
    }

    private final String name;
    public int[] combColour = new int[2];

    public String getName() {
        return LocalizationManager.getLocalizedString("drop." + this.name);
    }
}
