package xyz.iffyspeak.servernicknames.Util;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
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

        public static void fullPlayerlistUpdate()
        {
            MinecraftServer server = Server.getServer();
            Utilities.Server.NMP.updateTabList(server);
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

        public static class NMP {
            public static void updateTabListForPlayer(ServerPlayer targetPlayer, String displayName) {
                MinecraftServer server = Utilities.Server.getServer();
                if (server != null) {
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        // Remove the old player info
                        player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, targetPlayer));

                        // Add the updated player info
                        player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, targetPlayer));

                        // Update the display name for the player
                        player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, targetPlayer));
                    }
                }
            }

            public static void updateTabList(MinecraftServer server) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    // Get the current nickname and player name
                    String nickname = ServerNicknamesConfig.getNickname(player.getUUID());
                    String playerName = player.getName().getString();
                    String displayName = nickname != null ? nickname + " (" + playerName + ")" : playerName;

                    // Send packets to all players to update the tab list
                    updateTabListForPlayer(player, displayName);
                }
            }
        }
    }
}
