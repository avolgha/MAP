package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class RunAsCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("runas")
                .requires(source -> source.getExecutor() instanceof Player player && player.isOp())
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("command", ArgumentTypes.signedMessage())
                                .executes(context -> {
                                    PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    SignedMessageResolver commandResolver = context.getArgument("command", SignedMessageResolver.class);

                                    String command = commandResolver.content();
                                    List<Player> targets = targetResolver.resolve(context.getSource());

                                    if (targets.size() > 1) {
                                        this.message(Objects.requireNonNull(context.getSource().getExecutor()), "Found too many target players.");
                                        return 2;
                                    }

                                    boolean result = targets.getFirst().performCommand(command);
                                    this.message(Objects.requireNonNull(context.getSource().getExecutor()), Component.join(
                                            JoinConfiguration.spaces(),
                                            Component.text("Command was performed")
                                                    .color(NamedTextColor.GRAY),
                                            Component.text((result ? "" : "un") + "successfully")
                                                    .color(result ? NamedTextColor.GREEN : NamedTextColor.RED)
                                                    .append(Component.text("!")
                                                            .color(NamedTextColor.GRAY))
                                    ));

                                    return SUCCESS;
                                })))
                .build();
    }
}
