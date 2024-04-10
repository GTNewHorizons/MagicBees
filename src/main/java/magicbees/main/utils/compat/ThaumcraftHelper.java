package magicbees.main.utils.compat;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import magicbees.api.MagicBeesAPI;
import magicbees.item.types.PollenType;
import magicbees.item.types.PropolisType;
import magicbees.item.types.ResourceType;
import magicbees.item.types.WaxType;
import magicbees.main.CommonProxy;
import magicbees.main.Config;
import magicbees.main.utils.BlockInterface;
import magicbees.main.utils.ItemInterface;
import magicbees.main.utils.LocalizationManager;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;

public class ThaumcraftHelper implements IModHelper {

    public static final String Name = "Thaumcraft";
    public static Block plant;
    public static Block candle;
    public static Block crystal;
    public static Block marker;
    public static Block jar;
    public static Block log;
    public static Block leaf;
    public static Block warded;
    public static Block wooden;
    public static Block metal;
    public static Block airy;
    public static Block fluxGas;
    public static Block fluxGoo;
    public static Item filledJar;
    public static Item miscResource;
    public static Item shard;
    public static Item golem;
    public static Item nuggetMetal;
    public static Item nuggetChicken;
    public static Item nuggetBeef;
    public static Item nuggetPork;
    public static Item zombieBrain;
    public static ResearchItem bloodFramePage;
    public static ResearchPage bloodFrame1;
    public static ResearchPage bloodFrame2;
    public static ResearchItem frenzyFramePage;
    public static ResearchPage frenzyFrame1;
    public static ResearchPage frenzyFrame2;
    public static Class<? extends TileEntity> nodeClass;
    public static Object bloodFrame;
    public static Object frenziedFrame;
    private static boolean isThaumcraftPresent = false;
    private static Object aspectTime;
    private static Object frameMagic;
    private static Object thaumScoop;
    private static Object thaumGrafter;
    private static Object singularityA;
    private static Object singularityB;
    private static Object essenceLife;
    private static Object essenceDeath;
    private static Object essenceArmor;
    private static Object essenceUnstableA;
    private static Object essenceUnstableB;
    private static Object essenceTime;
    private static Object essenceOblivion;
    private static Object visAuraProvider;
    private static Object voidScoop;
    private static Object voidGrafter;
    private static InfusionRecipe apimancersDrainer;

    public static boolean isActive() {
        return isThaumcraftPresent;
    }

    private static void getBlocks() {
        plant = BlockInterface.getBlock(Name, "blockCustomPlant");
        candle = BlockInterface.getBlock(Name, "blockCandle");
        crystal = BlockInterface.getBlock(Name, "blockCrystal");
        marker = BlockInterface.getBlock(Name, "blockMarker");
        jar = BlockInterface.getBlock(Name, "blockJar");
        log = BlockInterface.getBlock(Name, "blockMagicalLog");
        leaf = BlockInterface.getBlock(Name, "blockMagicalLeaves");
        warded = BlockInterface.getBlock(Name, "blockWarded");
        wooden = BlockInterface.getBlock(Name, "blockWoodenDevice");
        metal = BlockInterface.getBlock(Name, "blockMetalDevice");
        airy = BlockInterface.getBlock(Name, "blockAiry");
    }

    private static void getItems() {
        filledJar = ItemInterface.getItem(Name, "BlockJarFilledItem");
        miscResource = ItemInterface.getItem(Name, "ItemResource");
        // Config.tcEssentiaBottle = ItemInterface.getItem(Name,
        // "BlockJarFilledItem");
        shard = ItemInterface.getItem(Name, "ItemShard");
        golem = ItemInterface.getItem(Name, "ItemGolemPlacer");
        // Config.tcWispEssence = ItemApi.getItem("itemWispEssence",
        // 0).getItem();
        nuggetMetal = ItemInterface.getItem(Name, "ItemNugget");
        shard = ItemInterface.getItem(Name, "ItemShard");
        nuggetChicken = ItemInterface.getItem(Name, "ItemNuggetChicken");
        nuggetBeef = ItemInterface.getItem(Name, "ItemNuggetBeef");
        nuggetPork = ItemInterface.getItem(Name, "ItemNuggetPork");
        zombieBrain = ItemInterface.getItem(Name, "ItemZombieBrain");
    }

    private static void setupCrafting() {
        ItemStack input, output;

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_AIR);
        output = new ItemStack(shard, 1, ShardType.AIR.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_WATER);
        output = new ItemStack(shard, 1, ShardType.WATER.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_FIRE);
        output = new ItemStack(shard, 1, ShardType.FIRE.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_EARTH);
        output = new ItemStack(shard, 1, ShardType.EARTH.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_ORDER);
        output = new ItemStack(shard, 1, ShardType.ORDER.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        input = Config.miscResources.getStackForType(ResourceType.TC_DUST_CHAOS);
        output = new ItemStack(shard, 1, ShardType.CHAOS.ordinal());
        GameRegistry.addShapelessRecipe(output, input, input, input, input);

        thaumScoop = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_Scoop",
                new ItemStack(Config.thaumiumScoop),
                new AspectList().add(Aspect.ORDER, 2),
                "sWs",
                "sTs",
                " T ",
                's',
                Items.stick,
                'W',
                Blocks.wool,
                'T',
                new ItemStack(miscResource, 1, MiscResource.THAUMIUM.ordinal()));

        thaumGrafter = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_Grafter",
                new ItemStack(Config.thaumiumGrafter),
                new AspectList().add(Aspect.ORDER, 5),
                "  T",
                " s ",
                "s  ",
                's',
                Items.stick,
                'T',
                new ItemStack(miscResource, 1, MiscResource.THAUMIUM.ordinal()));

        frameMagic = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_FrameMagic",
                new ItemStack(Config.hiveFrameMagic),
                new AspectList().add(Aspect.ORDER, 5).add(Aspect.AIR, 2).add(Aspect.EARTH, 2),
                "sss",
                "sCs",
                "sss",
                's',
                Items.stick,
                'C',
                new ItemStack(miscResource, 1, MiscResource.ENCHANTED_FABRIC.ordinal()));

        visAuraProvider = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_VisAuraProvider",
                new ItemStack(Config.visAuraProvider),
                new AspectList().add(Aspect.ORDER, 60).add(Aspect.AIR, 60).add(Aspect.ENTROPY, 60)
                        .add(Aspect.WATER, 60),
                "ngn",
                "gvg",
                "npn",
                'n',
                Items.gold_nugget,
                'g',
                new ItemStack(wooden, 1, WoodenDeviceType.PLANKS_GREATWOOD.ordinal()),
                'v',
                new ItemStack(metal, 1, MetalDeviceType.VIS_RELAY.ordinal()),
                'p',
                Config.pollen.getStackForType(PollenType.UNUSUAL));

        essenceLife = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceLife",
                Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE),
                new ItemStack(Blocks.red_flower),
                new AspectList().add(Aspect.EXCHANGE, 4).add(Aspect.PLANT, 4));

        essenceDeath = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceDeath",
                Config.miscResources.getStackForType(ResourceType.ESSENCE_SHALLOW_GRAVE),
                new ItemStack(Items.rotten_flesh),
                new AspectList().add(Aspect.DEATH, 4).add(Aspect.ENTROPY, 4));

        essenceArmor = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceArmor",
                Config.miscResources.getStackForType(ResourceType.ESSENCE_EVERLASTING_DURABILITY),
                new ItemStack(Items.leather),
                new AspectList().add(Aspect.ARMOR, 4).add(Aspect.MAGIC, 2));

        output = Config.miscResources.getStackForType(ResourceType.ESSENCE_FICKLE_PERMANENCE);
        essenceUnstableA = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceUnstable",
                output,
                Config.propolis.getStackForType(PropolisType.UNSTABLE),
                new AspectList().add(Aspect.ENTROPY, 2).add(Aspect.EXCHANGE, 1));

        essenceUnstableB = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceUnstable",
                output,
                new ItemStack(Blocks.netherrack),
                new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.EXCHANGE, 5));

        essenceTime = ThaumcraftApi.addCrucibleRecipe(
                "MB_EssenceTime",
                Config.miscResources.getStackForType(ResourceType.ESSENCE_LOST_TIME),
                new ItemStack(Items.clock),
                new AspectList().add(Aspect.ORDER, 10).add(Aspect.VOID, 10).add(Aspect.TRAP, 4));

        input = new ItemStack(Items.ender_pearl);
        singularityA = ThaumcraftApi.addInfusionCraftingRecipe(
                "MB_DimensionalSingularity",
                Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY, 3),
                5,
                new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.EXCHANGE, 20),
                new ItemStack(Blocks.gold_block),
                new ItemStack[] { input, input });

        ItemStack in2 = new ItemStack(Items.diamond);
        singularityB = ThaumcraftApi.addInfusionCraftingRecipe(
                "MB_DimensionalSingularity",
                Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY, 3),
                6,
                new AspectList().add(Aspect.ELDRITCH, 10).add(Aspect.EXCHANGE, 20).add(Aspect.VOID, 15),
                Config.propolis.getStackForType(PropolisType.UNSTABLE),
                new ItemStack[] { input, input, in2 });

        essenceOblivion = ThaumcraftApi.addShapelessArcaneCraftingRecipe(
                "MB_EssenceOblivion",
                Config.miscResources.getStackForType(ResourceType.ESSENCE_SCORNFUL_OBLIVION),
                new AspectList().add(Aspect.ENTROPY, 25).add(Aspect.AIR, 40).add(Aspect.ORDER, 15),
                Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY),
                Blocks.dragon_egg);

        voidScoop = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_ScoopVoid",
                new ItemStack(Config.voidScoop),
                new AspectList().add(Aspect.ORDER, 2),
                "sWs",
                "sTs",
                " T ",
                's',
                Items.stick,
                'W',
                Blocks.wool,
                'T',
                new ItemStack(miscResource, 1, MiscResource.VOID_INGOT.ordinal()));

        voidGrafter = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_GrafterVoid",
                new ItemStack(Config.voidGrafter),
                new AspectList().add(Aspect.ORDER, 5),
                "  T",
                " s ",
                "s  ",
                's',
                Items.stick,
                'T',
                new ItemStack(miscResource, 1, MiscResource.VOID_INGOT.ordinal()));

        ItemStack centrifuge = new ItemStack(ConfigBlocks.blockTube);
        centrifuge.setItemDamage(2);
        apimancersDrainer = ThaumcraftApi.addInfusionCraftingRecipe(
                "MB_ApimancersDrainer",
                new ItemStack(Config.apimancersDrainer),
                6,
                new AspectList().add(Aspect.MAGIC, 40).add(Aspect.HARVEST, 20).add(Aspect.EXCHANGE, 20)
                        .add(Aspect.ELDRITCH, 20),
                new ItemStack(ConfigBlocks.blockEssentiaReservoir),
                new ItemStack[] { centrifuge, Config.pollen.getStackForType(PollenType.UNUSUAL),
                        Config.pollen.getStackForType(PollenType.PHASED),
                        Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY),
                        Config.pollen.getStackForType(PollenType.UNUSUAL),
                        Config.pollen.getStackForType(PollenType.PHASED) });
    }

    private static void setupResearch() {

        ArrayList<Object> list;
        ItemStack input;
        IRecipe recipe;
        String category = "MAGICBEES";
        ResearchCategories.registerCategory(
                category,
                new ResourceLocation(CommonProxy.DOMAIN, CommonProxy.ITEM_TEXTURE + "beeInfusion.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));

        new ResearchItem(
                "MB_Root",
                category,
                new AspectList(),
                0,
                0,
                0,
                Config.miscResources.getStackForType(ResourceType.RESEARCH_BEEINFUSION)).setRound().setStub()
                        .setAutoUnlock().setPages(getResearchPage("MB_Root.1"), getResearchPage("MB_Root.2"))
                        .registerResearchItem();

        list = new ArrayList<Object>(4);
        list.add(Config.miscResources.getStackForType(ResourceType.LORE_FRAGMENT));
        list.add(Config.miscResources.getStackForType(ResourceType.LORE_FRAGMENT));
        list.add(Config.miscResources.getStackForType(ResourceType.LORE_FRAGMENT));
        list.add(Config.miscResources.getStackForType(ResourceType.LORE_FRAGMENT));
        recipe = new ShapelessRecipes(new ItemStack(miscResource, 1, MiscResource.KNOWLEDGE_FRAGMENT.ordinal()), list);

        new ResearchItem(
                "MB_LoreFragment",
                category,
                new AspectList(),
                0,
                -3,
                1,
                Config.miscResources.getStackForType(ResourceType.LORE_FRAGMENT))
                        .setPages(getResearchPage("MB_LoreFragment.1"), new ResearchPage(recipe)).setParents("MB_Root")
                        .setStub().setAutoUnlock().setRound().registerResearchItem();

        new ResearchItem(
                "MB_Scoop",
                category,
                new AspectList().add(Aspect.TOOL, 1).add(Aspect.MAGIC, 1).add(Aspect.AIR, 1),
                -2,
                -3,
                1,
                new ItemStack(Config.thaumiumScoop))
                        .setPages(getResearchPage("MB_Scoop.1"), new ResearchPage((IArcaneRecipe) thaumScoop))
                        .setParentsHidden("THAUMIUM").registerResearchItem();

        new ResearchItem(
                "MB_Grafter",
                category,
                new AspectList().add(Aspect.TOOL, 1).add(Aspect.TREE, 1).add(Aspect.GREED, 1),
                -2,
                -1,
                2,
                new ItemStack(Config.thaumiumGrafter))
                        .setPages(getResearchPage("MB_Grafter.1"), new ResearchPage((IArcaneRecipe) thaumGrafter))
                        .setParents("MB_Scoop").registerResearchItem();

        // Void tool insert
        new ResearchItem(
                "MB_ScoopVoid",
                category,
                new AspectList().add(Aspect.TOOL, 1).add(Aspect.ELDRITCH, 1).add(Aspect.AIR, 1),
                -4,
                -4,
                1,
                new ItemStack(Config.voidScoop))
                        .setPages(getResearchPage("MB_ScoopVoid.1"), new ResearchPage((IArcaneRecipe) voidScoop))
                        .setParents("MB_Scoop").setParentsHidden("VOIDMETAL").setSecondary().setConcealed()
                        .registerResearchItem();

        new ResearchItem(
                "MB_GrafterVoid",
                category,
                new AspectList().add(Aspect.TOOL, 1).add(Aspect.TREE, 1).add(Aspect.ELDRITCH, 1),
                -4,
                -2,
                2,
                new ItemStack(Config.voidGrafter))
                        .setPages(getResearchPage("MB_GrafterVoid.1"), new ResearchPage((IArcaneRecipe) voidGrafter))
                        .setParents("MB_Grafter").setParentsHidden("VOIDMETAL").setSecondary().setConcealed()
                        .registerResearchItem();

        new ResearchItem(
                "MB_FrameMagic",
                category,
                new AspectList().add(Aspect.TOOL, 1).add(Aspect.ARMOR, 1),
                -2,
                1,
                1,
                new ItemStack(Config.hiveFrameMagic))
                        .setPages(getResearchPage("MB_FrameMagic.1"), new ResearchPage((IArcaneRecipe) frameMagic))
                        .registerResearchItem();

        input = new ItemStack(Config.hiveFrameMagic);
        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE));
        list.add(input);
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameGentle), list);

        new ResearchItem(
                "MB_EssenceLife",
                category,
                new AspectList().add(Aspect.LIFE, 1).add(Aspect.MAGIC, 1),
                2,
                -1,
                1,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE))
                        .setPages(
                                getResearchPage("MB_EssenceLife.1"),
                                new ResearchPage((CrucibleRecipe) essenceLife),
                                new ResearchPage(recipe))
                        .setParentsHidden("CRUCIBLE").registerResearchItem();

        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_EVERLASTING_DURABILITY));
        list.add(input);
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameResilient), list);

        new ResearchItem(
                "MB_EssenceArmor",
                category,
                new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MAGIC, 1),
                5,
                0,
                2,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_EVERLASTING_DURABILITY))
                        .setPages(
                                getResearchPage("MB_EssenceArmor.1"),
                                new ResearchPage((CrucibleRecipe) essenceArmor),
                                new ResearchPage(recipe))
                        .setParents("MB_EssenceLife").registerResearchItem();

        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_FICKLE_PERMANENCE));
        list.add(input);
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameMetabolic), list);

        new ResearchItem(
                "MB_EssenceUnstable",
                category,
                new AspectList().add(Aspect.ENTROPY, 1).add(Aspect.ORDER, 1),
                3,
                1,
                2,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_FICKLE_PERMANENCE))
                        .setPages(
                                getResearchPage("MB_EssenceUnstable.1"),
                                new ResearchPage((CrucibleRecipe) essenceUnstableA),
                                new ResearchPage((CrucibleRecipe) essenceUnstableB),
                                new ResearchPage(recipe))
                        .setParents("MB_EssenceLife").setConcealed().registerResearchItem();

        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_SHALLOW_GRAVE));
        list.add(input);
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameNecrotic), list);

        new ResearchItem(
                "MB_EssenceDeath",
                category,
                new AspectList().add(Aspect.DEATH, 1).add(Aspect.MAGIC, 1),
                2,
                3,
                1,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_SHALLOW_GRAVE))
                        .setPages(
                                getResearchPage("MB_EssenceDeath.1"),
                                new ResearchPage((CrucibleRecipe) essenceDeath),
                                new ResearchPage(recipe))
                        .setParents("MB_EssenceUnstable").setConcealed().registerResearchItem();

        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_LOST_TIME));
        list.add(input);
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameTemporal), list);

        new ResearchItem(
                "MB_EssenceTime",
                category,
                new AspectList().add((Aspect) aspectTime, 1).add(Aspect.MAGIC, 1),
                0,
                2,
                2,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_LOST_TIME))
                        .setPages(
                                getResearchPage("MB_EssenceTime.1"),
                                new ResearchPage((CrucibleRecipe) essenceTime),
                                new ResearchPage(recipe))
                        .setParents("MB_EssenceUnstable").setConcealed().registerResearchItem();

        new ResearchItem(
                "MB_DimensionalSingularity",
                category,
                new AspectList().add(Aspect.ELDRITCH, 1).add((Aspect) aspectTime, 1).add(Aspect.VOID, 1),
                -1,
                4,
                3,
                Config.miscResources.getStackForType(ResourceType.DIMENSIONAL_SINGULARITY))
                        .setPages(
                                getResearchPage("MB_DimensionalSingularity.1"),
                                new ResearchPage((InfusionRecipe) singularityA),
                                new ResearchPage((InfusionRecipe) singularityB))
                        .setParents("MB_FrameMagic", "MB_EssenceTime", "MB_EssenceDeath").setConcealed().setSpecial()
                        .registerResearchItem();

        list = new ArrayList<Object>(2);
        list.add(Config.miscResources.getStackForType(ResourceType.ESSENCE_SCORNFUL_OBLIVION));
        list.add(ItemInterface.getItemStack("frameProven"));
        recipe = new ShapelessRecipes(new ItemStack(Config.hiveFrameOblivion), list);

        new ResearchItem(
                "MB_EssenceOblivion",
                category,
                new AspectList().add(Aspect.VOID, 1).add(Aspect.HUNGER, 1).add((Aspect) aspectTime, 1),
                -3,
                3,
                3,
                Config.miscResources.getStackForType(ResourceType.ESSENCE_SCORNFUL_OBLIVION))
                        .setPages(
                                getResearchPage("MB_EssenceOblivion.1"),
                                new ResearchPage((IArcaneRecipe) essenceOblivion),
                                new ResearchPage(recipe))
                        .setParents("MB_DimensionalSingularity").setConcealed().registerResearchItem();

        new ResearchItem(
                "MB_VisAuraProvider",
                category,
                new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ENERGY, 1).add(Aspect.AURA, 1),
                -5,
                5,
                3,
                new ItemStack(Config.visAuraProvider))
                        .setPages(
                                getResearchPage("MB_VisAuraProvider.1"),
                                new ResearchPage((IArcaneRecipe) visAuraProvider))
                        .setParentsHidden("VISPOWER").registerResearchItem();

        new ResearchItem(
                "MB_ApimancersDrainer",
                category,
                new AspectList().add(Aspect.ELDRITCH, 1).add(Aspect.HARVEST, 1).add(Aspect.MAGIC, 1)
                        .add((Aspect) aspectTime, 1),
                -3,
                5,
                4,
                new ItemStack(Config.apimancersDrainer))
                        .setPages(getResearchPage("MB_ApimancersDrainer.1"), new ResearchPage(apimancersDrainer))
                        .setParentsHidden("VOIDMETAL", "ESSENTIARESERVOIR").setParents("MB_EssenceUnstable")
                        .setConcealed().registerResearchItem();

    }

    private static ResearchPage getResearchPage(String ident) {
        return new ResearchPage(LocalizationManager.getLocalizedString("tc.research_page." + ident));
    }

    private static void setupItemAspects() {
        ItemStack item;
        AspectList list;

        ThaumcraftApi.registerObjectTag(
                new ItemStack(Items.clock),
                new AspectList(new ItemStack(Items.clock)).add((Aspect) aspectTime, 4));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Items.repeater),
                new AspectList(new ItemStack(Items.repeater)).add((Aspect) aspectTime, 2));

        list = new AspectList(new ItemStack(Blocks.log));
        for (int i = 1; i <= 8; i++) {
            item = ItemInterface.getItemStack("log" + i);
            if (item != null) {
                ThaumcraftApi.registerObjectTag(item, list);
            }
        }

        list = new AspectList(new ItemStack(Blocks.planks));
        for (int i = 1; i <= 2; i++) {
            item = ItemInterface.getItemStack("planks" + i);
            if (item != null) {
                ThaumcraftApi.registerObjectTag(item, list);
            }
        }

        list = new AspectList().add(Aspect.ORDER, 5).add(Aspect.ARMOR, 2).add(Aspect.BEAST, 1);
        item = new ItemStack(ForestryHelper.hiveBlock, 1, 32767);
        ThaumcraftApi.registerObjectTag(item, list);
        ThaumcraftApi.registerObjectTag(new ItemStack(Config.hive, 1, 32767), list);
        if (ExtraBeesHelper.isActive())
            ThaumcraftApi.registerObjectTag(new ItemStack(ExtraBeesHelper.hiveBlock, 1, 32767), list);

        list = new AspectList().add(Aspect.LIGHT, 1);
        item = new ItemStack(BlockInterface.getBlock(ForestryHelper.Name, "candle"));
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList().add(Aspect.DARKNESS, 1);
        item = new ItemStack(BlockInterface.getBlock(ForestryHelper.Name, "stump"));
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList(new ItemStack(Blocks.glass)).add(Aspect.SENSES, 1);
        item = new ItemStack(BlockInterface.getBlock(ForestryHelper.Name, "glass"));
        ThaumcraftApi.registerObjectTag(item, list);

        list = new AspectList().add(Aspect.EARTH, 2).add(Aspect.WATER, 2);
        item = new ItemStack(BlockInterface.getBlock(ForestryHelper.Name, "soil"));
        ThaumcraftApi.registerObjectTag(item, new int[] { 1 }, list);

        list = new AspectList(new ItemStack(Blocks.leaves));
        item = ItemInterface.getItemStack(ForestryHelper.Name, "candle");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList(new ItemStack(Blocks.sapling));
        item = ItemInterface.getItemStack(ForestryHelper.Name, "candle");
        ThaumcraftApi.registerObjectTag(item, list);

        list = new AspectList() /* .add(Aspect.SEED, 1) */.add(Aspect.PLANT, 1);
        item = ItemInterface.getItemStack(ForestryHelper.Name, "candle");
        ThaumcraftApi.registerObjectTag(item, list);

        list = new AspectList().add(Aspect.CRYSTAL, 1).add(Aspect.EARTH, 2);
        item = ItemInterface.getItemStack(ForestryHelper.Name, "candle");
        ThaumcraftApi.registerObjectTag(
                new ItemStack(item.getItem(), 1, ForestryHelper.BlockResource.APATITE.ordinal()),
                list.copy());
        item = ItemInterface.getItemStack(ForestryHelper.Name, "apatite");
        ThaumcraftApi.registerObjectTag(
                new ItemStack(item.getItem(), 1, item.getItemDamage()),
                list.copy().add(Aspect.CRYSTAL, 2));

        list = new AspectList().add(Aspect.MOTION, 2).add(Aspect.FLIGHT, 1);
        item = ItemInterface.getItemStack("beeDroneGE");
        ThaumcraftApi.registerObjectTag(item, list);
        list = list.copy().add(Aspect.GREED, 2).add(Aspect.EXCHANGE, 1);
        item = ItemInterface.getItemStack("beePrincessGE");
        ThaumcraftApi.registerObjectTag(item, list);
        item = ItemInterface.getItemStack("beeQueenGE");
        ThaumcraftApi.registerObjectTag(item, list);
        list = new AspectList().add(Aspect.LIFE, 2).add(Aspect.EXCHANGE, 5);
        item = ItemInterface.getItemStack("beeLarvaeGE");
        ThaumcraftApi.registerObjectTag(item, list);

        item = ItemInterface.getItemStack("scoop");
        list = new AspectList(new ItemStack(item.getItem(), 1, 0)).add(Aspect.TOOL, 2);
        ThaumcraftApi.registerComplexObjectTag(item, list);

        item = ItemInterface.getItemStack("grafter");
        list = new AspectList(new ItemStack(item.getItem(), 1, item.getItemDamage())).add(Aspect.TOOL, 2);
        ThaumcraftApi.registerComplexObjectTag(item, list);

        item = ItemInterface.getItemStack("grafterProven");
        list = list.copy().add(Aspect.TOOL, 2).add(Aspect.EXCHANGE, 3);
        ThaumcraftApi.registerObjectTag(new ItemStack(item.getItem(), 1, item.getItemDamage()), list);

        list = new AspectList().add(Aspect.TRAP, 2);
        ThaumcraftApi.registerObjectTag("beeComb", list);

        list = new AspectList().add(Aspect.SLIME, 1);
        item = ItemInterface.getItemStack("propolis");
        ThaumcraftApi
                .registerObjectTag(new ItemStack(item.getItem(), 1, ForestryHelper.Propolis.NORMAL.ordinal()), list);
        ThaumcraftApi.registerObjectTag(
                new ItemStack(item.getItem(), 1, ForestryHelper.Propolis.SILKY.ordinal()),
                list.copy().add(Aspect.SLIME, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(item.getItem(), 1, ForestryHelper.Propolis.PULSATING.ordinal()),
                list.copy().add(Aspect.ELDRITCH, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.UNSTABLE.ordinal()),
                list.copy().add(Aspect.ENTROPY, 1).add(Aspect.EXCHANGE, 1));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.AIR.ordinal()),
                list.copy().add(Aspect.AIR, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.WATER.ordinal()),
                list.copy().add(Aspect.WATER, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.FIRE.ordinal()),
                list.copy().add(Aspect.FIRE, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.EARTH.ordinal()),
                list.copy().add(Aspect.EARTH, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.ORDER.ordinal()),
                list.copy().add(Aspect.ORDER, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.propolis, 1, PropolisType.CHAOS.ordinal()),
                list.copy().add(Aspect.ENTROPY, 2));

        list = new AspectList().add(Aspect.PLANT, 2);
        item = ItemInterface.getItemStack("pollen");
        ThaumcraftApi.registerObjectTag(new ItemStack(item.getItem(), 1, ForestryHelper.Pollen.NORMAL.ordinal()), list);
        ThaumcraftApi.registerObjectTag(
                new ItemStack(item.getItem(), 1, ForestryHelper.Pollen.CRYSTALLINE.ordinal()),
                list.copy());
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.pollen, 1, PollenType.UNUSUAL.ordinal()),
                list.copy().add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.pollen, 1, PollenType.PHASED.ordinal()),
                list.copy().add((Aspect) aspectTime, 2));

        list = new AspectList().add(Aspect.ORDER, 1);
        item = ItemInterface.getItemStack("beeswax");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list.copy().add(Aspect.ORDER, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.wax, 1, WaxType.MAGIC.ordinal()),
                list.copy().add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.wax, 1, WaxType.SOUL.ordinal()),
                list.copy().add(Aspect.SOUL, 2));
        ThaumcraftApi.registerObjectTag(
                new ItemStack(Config.wax, 1, WaxType.AMNESIC.ordinal()),
                list.copy().add(Aspect.MIND, 2));

        list = new AspectList().add(Aspect.EXCHANGE, 2).add(Aspect.LIFE, 2);
        item = ItemInterface.getItemStack("honeyDrop");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList().add(Aspect.LIFE, 2).add(Aspect.ENERGY, 2);
        item = ItemInterface.getItemStack("honeydew");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList().add(Aspect.GREED, 1).add(Aspect.LIFE, 4);
        item = ItemInterface.getItemStack("royalJelly");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        list = new AspectList().add(Aspect.FIRE, 1).add(Aspect.TRAP, 2);
        item = ItemInterface.getItemStack("phosphor");
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, list);

        item = Config.miscResources.getStackForType(ResourceType.ESSENCE_FALSE_LIFE);
        ThaumcraftApi.registerObjectTag(item, new int[] { item.getItemDamage() }, new AspectList().add(Aspect.LIFE, 6));

        item = Config.miscResources.getStackForType(ResourceType.ESSENCE_SHALLOW_GRAVE);
        ThaumcraftApi
                .registerObjectTag(item, new int[] { item.getItemDamage() }, new AspectList().add(Aspect.DEATH, 6));

        item = Config.miscResources.getStackForType(ResourceType.ESSENCE_EVERLASTING_DURABILITY);
        ThaumcraftApi
                .registerObjectTag(item, new int[] { item.getItemDamage() }, new AspectList().add(Aspect.ARMOR, 6));

        item = Config.miscResources.getStackForType(ResourceType.ESSENCE_LOST_TIME);
        ThaumcraftApi.registerObjectTag(
                item,
                new int[] { item.getItemDamage() },
                new AspectList().add(Aspect.VOID, 6).add((Aspect) aspectTime, 8));

    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.thaumcraftActive) {
            isThaumcraftPresent = true;
            aspectTime = new Aspect(
                    "tempus",
                    0xB68CFF,
                    new Aspect[] { Aspect.VOID, Aspect.ORDER },
                    new ResourceLocation(CommonProxy.DOMAIN, CommonProxy.TEXTURE + "aspects/tempus.png"),
                    1);
            MagicBeesAPI.thaumcraftAspectTempus = aspectTime;
        } else {
            // Switch off TC-dependant items.
            ResourceType.LORE_FRAGMENT.setHidden();
            ResourceType.TC_DUST_AIR.setHidden();
            ResourceType.TC_DUST_CHAOS.setHidden();
            ResourceType.TC_DUST_EARTH.setHidden();
            ResourceType.TC_DUST_FIRE.setHidden();
            ResourceType.TC_DUST_ORDER.setHidden();
            ResourceType.TC_DUST_WATER.setHidden();
        }
    }

    public void init() {
        if (isActive()) {
            getBlocks();
            getItems();
        }
    }

    public void postInit() {
        if (isActive()) {
            setupItemAspects();
            setupCrafting();
            setupResearch();
        }
    }

    public enum MiscResource {
        ALUMENTUM,
        NITOR,
        THAUMIUM,
        QUICKSILVER,
        MAGIC_TALLOW,
        BRAIN_DEPRECATED,
        AMBER,
        ENCHANTED_FABRIC,
        VIS_FILTER,
        KNOWLEDGE_FRAGMENT,
        MIRRORED_GLASS,
        TAINTED_GOO,
        TAINTED_TENDRIL,
        JAR_LABEL,
        SALIS,
        CHARM,
        VOID_INGOT,
        VOID_SEED,
        COIN,
    }

    public enum NuggetType {
        IRON,
        COPPER,
        TIN,
        SILVER,
        LEAD,
        QUICKSILVER,
        THAUMIUM,
        VOID_METAL,
        _8,
        _9,
        _10,
        _11,
        _12,
        _13,
        _14,
        _15,
        NATIVE_IRON,
        NATIVE_COPPER,
        NATIVE_TIN,
        NATIVE_SILVER,
        NATIVE_LEAD,
        NATIVE_CINNABAR,
        _22,
        _23,
        _24,
        _25,
        _26,
        _27,
        _28,
        _29,
        _30,
        NATIVE_GOLD,
    }

    public enum ShardType {
        AIR,
        FIRE,
        WATER,
        EARTH,
        ORDER,
        CHAOS,
        BALANCED,
    }

    public enum MetalDeviceType {
        CRUCIBLE,
        ALEMBIC,
        VIS_CHARGE_RELAY,
        ADVANCED_ALCHEMICAL_CONSTRUCT,
        _4,
        ITEM_GRATE,
        _6,
        ARCANE_LAMP,
        LAMP_OF_GROWTH,
        ALCHEMICAL_CONSTRUCT,
        THAUMATORIUM,
        _11,
        MNEMONIC_MATRIX,
        LAMP_OF_FERTILITY,
        VIS_RELAY,
    }

    public enum WoodenDeviceType {
        BELLOWS,
        EAR,
        PRESSURE_PLATE,
        PRESSURE_PLATE_B,
        BORE_BASE,
        BORE,
        PLANKS_GREATWOOD,
        PLANKS_SILVERWOOD,
        BANNER,
    }

    public enum AiryBlockType {
        NODE,
        NITOR,
        _2,
        _3,
        WARDING_STONE_FENCE,
        ENERGIZED_NODE,
    }

    public enum Entity {

        BRAINY_ZOMBIE("entBrainyZombie", "EntityBrainyZombie"),
        GIANT_BRAINY_ZOMBIE("entGiantBrainyZombie", "EntityGiantBrainyZombie"),
        WISP("entWisp", "EntityWisp"),
        FIREBAT("entFirebat", "EntityFireBat"),;

        private static final String packageName = "thaumcraft.common.entities.monster.";

        public String entityID;
        private final String className;

        Entity(String id, String clazz) {
            this.entityID = id;
            this.className = clazz;
        }

        public String getClassName() {
            return packageName + this.className;
        }
    }

    public enum BlockPlant {
        GREATWOOD_SAPLING,
        SILVERWOOD_SAPLING,
        SHIMMERLEAF,
        CINDERPEARL,
        PURIFYING_PLANT,
        VISHROOM,
    }

    public enum TreeType {
        GREATWOOD,
        SILVERWOOD,
    }
}
