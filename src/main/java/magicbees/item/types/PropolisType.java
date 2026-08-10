package magicbees.item.types;

import magicbees.main.utils.LocalizationManager;

public enum PropolisType {

    UNSTABLE("unstable", 0xEFB492),

    AIR("air", 0xA19E10),
    FIRE("fire", 0x95132F),
    WATER("water", 0x1054A1),
    EARTH("earth", 0x00a000),
    ORDER("dull", 0xDDDDFF),
    CHAOS("magic", 0x555577),;

    public static final PropolisType[] VALUES = values();

    PropolisType(String pName, int overlayColour) {
        this.name = pName;
        this.colour = overlayColour;
    }

    private String name;
    public int colour;

    public String getName() {
        return LocalizationManager.getLocalizedString("propolis." + this.name);
    }
}
