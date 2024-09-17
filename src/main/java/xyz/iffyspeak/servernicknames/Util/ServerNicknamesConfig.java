package xyz.iffyspeak.servernicknames.Util;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerNicknamesConfig {
    private static final Map<UUID, String> nicklist = new HashMap<>();
    private static FileConfig config;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void setNickname(UUID uuid, String nickname) {
        if (nickname == null) {
            nicklist.remove(uuid);
        } else {
            nicklist.put(uuid, nickname);
        }
        saveConfig();
    }

    public static String getNickname(UUID uuid) {
        return nicklist.getOrDefault(uuid, Utilities.Players.getUsername(uuid, Utilities.Server.getServer()));
    }

    public static void loadConfig(File file) {
        config = FileConfig.of(file, TomlFormat.instance());
        config.load();

        if (config.contains("nicknames")) {
            Map<String, Object> loadedNicknames = config.get("nicknames");
            for (Map.Entry<String, Object> entry : loadedNicknames.entrySet()) {
                nicklist.put(UUID.fromString(entry.getKey()), (String) entry.getValue());
            }
        } else {
            LOGGER.warn("No nicknames map in config. No nicknames will be applied.");
        }
    }

    public static void saveConfig() {
        if (config == null) return;

        Map<String, String> saveMap = new HashMap<>();
        for (Map.Entry<UUID, String> entry : nicklist.entrySet()) {
            saveMap.put(entry.getKey().toString(), entry.getValue());
        }

        config.set("nicknames", saveMap);
        config.save();
    }
}
