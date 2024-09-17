package xyz.iffyspeak.servernicknames;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.UUID;

public class Utilities {
    public static class Players {
        public static String getUsername(UUID playerUUID, MinecraftServer server)
        {
            PlayerList playerlist = server.getPlayerList();

            ServerPlayer player = playerlist.getPlayer(playerUUID);

            return player != null ? player.getName().getString() : "NOTHINGRETURNED";
        }
    }

    public static class Server {
        private static MinecraftServer _server;

        public static void setServer(MinecraftServer server)
        {
            _server = server;
        }

        public static MinecraftServer getServer()
        {
            return _server;
        }
    }
}
