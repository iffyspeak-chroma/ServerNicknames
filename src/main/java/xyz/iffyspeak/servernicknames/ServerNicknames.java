package xyz.iffyspeak.servernicknames;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ServerNicknames.MODID)
public class ServerNicknames {
    public static final String MODID = "servernicknames";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ServerNicknames()
    {
        LOGGER.info("Starting ServerNicknames.");
    }
}
