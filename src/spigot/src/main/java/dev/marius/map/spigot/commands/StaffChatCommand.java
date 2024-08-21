package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class StaffChatCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("sc")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.staffchat.send")))
                .then(Commands.argument("message", ArgumentTypes.signedMessage())
                        .executes(context -> {
                            SignedMessageResolver message = context.getArgument("message", SignedMessageResolver.class);

                            Bukkit.getOnlinePlayers().stream()
                                    .filter(target -> target.hasPermission("map.staffchat.receive"))
                                    .forEach(target -> target.sendMessage(Component.join(
                                            JoinConfiguration.spaces(),
                                            Component.text("STAFF")
                                                    .color(NamedTextColor.GOLD)
                                                    .decorate(TextDecoration.BOLD),
                                            target.displayName(),
                                            Component.text("»").color(NamedTextColor.DARK_GRAY),
                                            Component.text(message.content()).color(NamedTextColor.WHITE)))
                                    );

                            return SUCCESS;
                        }))
                .build();
    }
}
