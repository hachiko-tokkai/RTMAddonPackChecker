package jp.rtm.addonpackchecker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import jp.rtm.addonpackchecker.network.ManifestMessage;
import jp.rtm.addonpackchecker.network.ManifestMessageHandler;
import jp.rtm.addonpackchecker.network.ManifestRequestMessage;
import jp.rtm.addonpackchecker.network.ManifestRequestMessageHandler;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

@Mod(
        modid = RTMAddonPackChecker.MOD_ID,
        name = RTMAddonPackChecker.NAME,
        version = RTMAddonPackChecker.VERSION,
        dependencies = "required-after:RTM"
)
public final class RTMAddonPackChecker {
    public static final String MOD_ID = "rtmaddonpackchecker";
    public static final String NAME = "RTM Addon Pack Checker";
    public static final String VERSION = "1.0.0";
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("rtmpackcheck");

    private static CheckerConfig config;
    private static File gameDirectory;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        gameDirectory = event.getModConfigurationDirectory().getParentFile();
        config = CheckerConfig.load(new Configuration(event.getSuggestedConfigurationFile()));

        NETWORK.registerMessage(ManifestRequestMessageHandler.class, ManifestRequestMessage.class, 0, Side.CLIENT);
        NETWORK.registerMessage(ManifestMessageHandler.class, ManifestMessage.class, 1, Side.SERVER);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new LoginEventHandler());
    }

    @NetworkCheckHandler
    public boolean checkRemoteModList(Map<String, String> remoteVersions, Side remoteSide) {
        return remoteVersions.containsKey(MOD_ID);
    }

    public static CheckerConfig getConfig() {
        return config;
    }

    public static File getModsDirectory() {
        return new File(gameDirectory, "mods");
    }
}
