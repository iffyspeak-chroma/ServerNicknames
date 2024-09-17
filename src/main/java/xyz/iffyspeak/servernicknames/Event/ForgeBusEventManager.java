package xyz.iffyspeak.servernicknames.Event;

import com.mojang.logging.LogUtils;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import xyz.iffyspeak.servernicknames.Command.NicknameCommand;
import xyz.iffyspeak.servernicknames.ServerNicknames;
import xyz.iffyspeak.servernicknames.Util.ServerNicknamesConfig;
import xyz.iffyspeak.servernicknames.Util.Utilities;

import java.io.File;

@Mod.EventBusSubscriber(modid = ServerNicknames.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent _e)
    {
        Utilities.Server.setServer(_e.getServer());
        LOGGER.info("Loading nicknames config");
        File configFile = new File(_e.getServer().getServerDirectory(), "config/nicknames.json");
        ServerNicknamesConfig.loadConfig(configFile);
        LOGGER.info("Finished loading.");
        NicknameCommand.register(_e.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent ignore)
    {
        LOGGER.info("Saving nicknames config");
        ServerNicknamesConfig.saveConfig();
        LOGGER.info("Finished saving.");
    }
}
