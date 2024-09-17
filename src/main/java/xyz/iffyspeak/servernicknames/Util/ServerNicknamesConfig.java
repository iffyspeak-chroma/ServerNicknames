package xyz.iffyspeak.servernicknames.Util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerNicknamesConfig {
    private static final List<String> nicklist = new ArrayList<>();
    private static FileConfig config;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerNicknamesConfig.class);

    public static void setNickname(UUID uuid, String nickname) {
        // Remove existing entry if it exists
        nicklist.removeIf(entry -> entry.startsWith(uuid.toString() + ":"));

        if (nickname != null) {
            nicklist.add(uuid.toString() + ":" + nickname);
        }
        saveConfig();
    }

    public static String getNickname(UUID uuid) {
        return nicklist.stream()
                .filter(entry -> entry.startsWith(uuid.toString() + ":"))
                .map(entry -> entry.substring(uuid.toString().length() + 1))
                .findFirst()
                .orElse(Utilities.Players.getUsername(uuid, Utilities.Server.getServer()));
    }

    public static void updatePlayerList(ServerPlayer player) {
        MinecraftServer server = Utilities.Server.getServer();
        if (server != null) {
            String nickname = getNickname(player.getUUID());
            String playerName = player.getName().getString();
            String displayName = nickname != null ? nickname + " (" + playerName + ")" : playerName;
            player.displayClientMessage(Component.literal(displayName), false);
        }
    }

    public static void loadConfig(File file) {
        config = FileConfig.of(file, TomlFormat.instance());
        config.load();

        if (config.contains("nicknames")) {
            // Cast to List<String>
            List<String> loadedNicknames = (List<String>) config.get("nicknames");
            nicklist.clear();
            nicklist.addAll(loadedNicknames);
        } else {
            LOGGER.warn("No nicknames in config. No nicknames will be applied.");
        }
    }

    public static void saveConfig() {
        if (config == null) return;

        try {
            config.set("nicknames", nicklist);
            config.save();
        } catch (Exception e) {
            LOGGER.error("Failed to save config", e);
        }
    }
}
