package xyz.iffyspeak.servernicknames.Event;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ServerNicknames.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeBusEventManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent _e)
    {
        Utilities.Server.setServer(_e.getServer());
        LOGGER.info("Loading nicknames config");
        File configFile = new File(_e.getServer().getServerDirectory(), "config/nicknames.toml");
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

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent _e)
    {
        Utilities.Players.fullPlayerlistUpdate();
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent _e)
    {
        Utilities.Players.fullPlayerlistUpdate();
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent _e)
    {
        ServerPlayer player = _e.getPlayer();
        String nickname = ServerNicknamesConfig.getNickname(player.getUUID());
        String playername = player.getName().getString();
        String message = _e.getMessage().getString();

        String formattedMessage;
        if (nickname != null && !nickname.isEmpty())
        {
            formattedMessage = nickname + " (" + playername + ") : " + message;
        } else {
            formattedMessage = playername + " : " + message;
        }

        MutableComponent formattedComponent = Component.literal(formattedMessage);

        _e.setCanceled(true);

        for (ServerPlayer op : player.getServer().getPlayerList().getPlayers())
        {
            op.sendSystemMessage(formattedComponent);
        }
    }
}
