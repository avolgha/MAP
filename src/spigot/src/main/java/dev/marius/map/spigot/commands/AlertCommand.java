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
public class AlertCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("alert")
                .requires(source -> !(source.getExecutor() instanceof Player player) || (player.isOp() || player.hasPermission("map.alert")))
                .then(Commands.argument("message", ArgumentTypes.signedMessage())
                        .executes(context -> {
                            SignedMessageResolver message = context.getArgument("message", SignedMessageResolver.class);

                            Bukkit.broadcast(Component.join(
                                    JoinConfiguration.spaces(),
                                    Component.newline(),
                                    Component.text("ALERT")
                                            .decorate(TextDecoration.BOLD)
                                            .color(NamedTextColor.RED),
                                    Component.text("»")
                                            .color(NamedTextColor.DARK_GRAY),
                                    Component.text(message.content()),
                                    Component.newline()
                            ));

                            return SUCCESS;
                        }))
                .build();
    }
}
