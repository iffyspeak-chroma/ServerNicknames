package xyz.iffyspeak.servernicknames.Command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import xyz.iffyspeak.servernicknames.Util.ServerNicknamesConfig;

import java.util.regex.Pattern;

public class NicknameCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Define the regex pattern for allowed characters
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9 ]+$");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("nickname")
                        .then(Commands.argument("nickname", StringArgumentType.greedyString())
                                .executes(context -> setNickname(context, StringArgumentType.getString(context, "nickname"))))
                        .then(Commands.literal("clear")
                                .executes(context -> clearNickname(context)))
        );
    }

    private static int setNickname(CommandContext<CommandSourceStack> context, String nickname) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        // Validate nickname with regex
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            player.sendSystemMessage(Component.literal("Invalid nickname! Only letters and numbers are allowed."));
            return Command.SINGLE_SUCCESS;
        }

        // Set the nickname
        ServerNicknamesConfig.setNickname(player.getUUID(), nickname);

        // Notify the player
        player.sendSystemMessage(Component.literal("Your nickname has been changed to: " + nickname));
        LOGGER.info("Player " + player.getName().getString() + " changed their nickname to " + nickname);

        return Command.SINGLE_SUCCESS;
    }

    private static int clearNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();

        // Clear the nickname in your config or data storage
        ServerNicknamesConfig.setNickname(player.getUUID(), null);

        // Notify the player
        player.sendSystemMessage(Component.literal("Your nickname has been cleared."));

        LOGGER.info("Player " + player.getName().getString() + " cleared their nickname");

        return Command.SINGLE_SUCCESS;
    }
}
