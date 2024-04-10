package magicbees.item.types;

import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;
import magicbees.main.utils.LocalizationManager;

public enum HiveFrameType implements IBeeModifier {

    MAGIC("Magic", 240, 1f, 1f, 1f, 2f, 0.6f), // buffed post additive prod change
    RESILIENT("Resilient", 800, 1f, 1f, 1f, 2f, 0.5f), // buffed post additive prod change
    GENTLE("Gentle", 200, 1f, 0.7f, 1.5f, 0.4f, 0.01f), // production was 1.4x, now +0.4
    METABOLIC("Metabolic", 130, 1f, 1.8f, 1f, 0.2f, 1f), // production was 1.2x, now +0.2
    NECROTIC("Necrotic", 280, 1f, 1f, 0.3f, -0.25f, 1.2f), // production was 0.75x, now -0.25
    TEMPORAL("Temporal", 300, 1f, 1f, 2.5f, 0f, 0.8f), // production was 1x, now +0
    OBLIVION("Oblivion", 50, 1f, 1f, 0.0001f, -9001f, 1f);// production was 0.0001x, now -9001

    private final String frameName;
    public final int maxDamage;

    private final float territoryMod;
    private final float mutationMod;
    private final float lifespanMod;
    private final float productionMod;
    private final float floweringMod;
    private final float geneticDecayMod;
    private final boolean isSealed;
    private final boolean isLit;
    private final boolean isSunlit;
    private final boolean isHellish;

    HiveFrameType(String name, int damage, float territory, float mutation, float lifespan, float production,
            float geneticDecay) {
        this(name, damage, territory, mutation, lifespan, production, 1f, geneticDecay, false, false, false, false);
    }

    HiveFrameType(String name, int damage, float territory, float mutation, float lifespan, float production,
            float flowering, float geneticDecay, boolean sealed, boolean lit, boolean sunlit, boolean hellish) {
        this.frameName = name;
        this.maxDamage = damage;

        this.territoryMod = territory;
        this.mutationMod = mutation;
        this.lifespanMod = lifespan;
        this.productionMod = production;
        this.floweringMod = flowering;
        this.geneticDecayMod = geneticDecay;
        this.isSealed = sealed;
        this.isLit = lit;
        this.isSunlit = sunlit;
        this.isHellish = hellish;
    }

    public String getName() {
        return this.frameName;
    }

    public String getLocalizedName() {
        return LocalizationManager.getLocalizedString("frame." + this.frameName);
    }

    @Override
    public float getTerritoryModifier(IBeeGenome genome, float currentModifier) {
        return territoryMod;
    }

    @Override
    public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
        return mutationMod;
    }

    @Override
    public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
        return lifespanMod;
    }

    @Override
    public float getProductionModifier(IBeeGenome genome, float currentModifier) {
        return productionMod;
    }

    @Override
    public float getFloweringModifier(IBeeGenome genome, float currentModifier) {
        return floweringMod;
    }

    @Override
    public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
        return geneticDecayMod;
    }

    @Override
    public boolean isSealed() {
        return isSealed;
    }

    @Override
    public boolean isSelfLighted() {
        return isLit;
    }

    @Override
    public boolean isSunlightSimulated() {
        return isSunlit;
    }

    @Override
    public boolean isHellish() {
        return isHellish;
    }
}
