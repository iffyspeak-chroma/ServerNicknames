package xyz.iffyspeak.servernicknames;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;

import java.io.File;

@Mod.EventBusSubscriber(modid = ServerNicknames.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent _e)
    {
        LOGGER.info("Loading nicknames config");
        File configFile = new File(_e.getServer().getServerDirectory(), "config/nicknames.json");
        ServerNicknamesConfig.loadConfig(configFile);
        LOGGER.info("Finished loading.");
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent _e)
    {
        LOGGER.info("Saving nicknames config");
        ServerNicknamesConfig.saveConfig();
        LOGGER.info("Finished saving.");
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent _e)
    {
        // Stuff here
    }
}
