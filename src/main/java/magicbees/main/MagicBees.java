package magicbees.main;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import magicbees.bees.BeeManager;
import magicbees.client.gui.GUIHandler;
import magicbees.main.utils.CraftingManager;
import magicbees.main.utils.IMCManager;
import magicbees.main.utils.LogHelper;
import magicbees.main.utils.VersionInfo;
import magicbees.main.utils.compat.ModHelperManager;
import magicbees.main.utils.net.NetworkEventHandler;

@Mod(
        modid = VersionInfo.ModName,
        name = "Magic Bees",
        version = VersionInfo.Version,
        dependencies = VersionInfo.Depends,
        guiFactory = VersionInfo.GUI_FACTORY_CLASS)
public class MagicBees {

    @Mod.Instance(VersionInfo.ModName)
    public static MagicBees object;

    @SidedProxy(serverSide = "magicbees.main.CommonProxy", clientSide = "magicbees.main.ClientProxy")
    public static CommonProxy proxy;

    public GUIHandler guiHandler;
    private Config modConfig;
    public final NetworkEventHandler netHandler = new NetworkEventHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Preinit started");
        this.modConfig = new Config(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(modConfig);

        // Compatibility Helpers setup time.
        ModHelperManager.preInit();

        this.modConfig.setupBlocks();
        this.modConfig.setupItems();

        LogHelper.info("Preinit completed");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModHelperManager.init();
        LogHelper.info("Init completed");

        BeeManager.getBeeRoot();
        BeeManager.setupAlleles();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModHelperManager.postInit();

        BeeManager.lateBeeInit();

        this.guiHandler = new GUIHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler);

        proxy.registerRenderers();

        this.modConfig.saveConfigs();
        this.modConfig.setupThaumicEnergistics();

        CraftingManager.setupCrafting();
        CraftingManager.registerLiquidContainers();

        LogHelper.info("Postinit completed");
    }

    @Mod.EventHandler
    public void handleIMCMessage(IMCEvent event) {
        IMCManager.handle(event);
    }

    @Mod.EventHandler
    public void handleMissingMapping(FMLMissingMappingsEvent event) {
        for (MissingMapping mapping : event.get()) {
            // TODO: ... maybe not this.
            LogHelper.info(String.format("Missing mapping: %s - ignoring.", mapping.name));
            mapping.ignore();
        }
    }
}
