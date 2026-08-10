package magicbees.item.types;

import net.minecraft.item.Item;

import magicbees.main.utils.LocalizationManager;

public enum NuggetType {

    IRON,
    COPPER,
    TIN,
    SILVER,
    LEAD,
    DIAMOND,
    EMERALD,
    APATITE,;

    public static final NuggetType[] VALUES = values();

    private Item targetIngot;
    private boolean active;

    private NuggetType() {
        this.active = true;
    }

    public void setIngotItem(Item target) {
        this.targetIngot = target;
    }

    public Item getIngotItem() {
        return this.targetIngot;
    }

    public void setInactive() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getName() {
        return LocalizationManager.getLocalizedString("nugget." + this.toString().toLowerCase());
    }
}
