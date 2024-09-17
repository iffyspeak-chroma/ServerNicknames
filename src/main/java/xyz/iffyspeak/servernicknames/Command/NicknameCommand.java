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

public class NicknameCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

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
        MinecraftServer server = source.getServer();
        ServerPlayer player = source.getPlayerOrException();

        ServerNicknamesConfig.setNickname(player.getUUID(), nickname);

        player.sendSystemMessage(Component.literal("Your nickname has been changed to: " + nickname));

        return Command.SINGLE_SUCCESS;
    }

    private static int clearNickname(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        ServerPlayer player = source.getPlayerOrException();

        // Clear the nickname in your config or data storage
        ServerNicknamesConfig.setNickname(player.getUUID(), null);

        // Notify the player
        player.sendSystemMessage(Component.literal("Your nickname has been cleared."));

        return Command.SINGLE_SUCCESS;
    }

}
