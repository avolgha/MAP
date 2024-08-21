package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class TeleportHereCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("tphere")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.tphere")))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            PlayerSelectorArgumentResolver resolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                            for (Player target : resolver.resolve(context.getSource()))
                                target.teleport(Objects.requireNonNull((Player) context.getSource().getExecutor()), PlayerTeleportEvent.TeleportCause.PLUGIN);
                            return SUCCESS;
                        }))
                .build();
    }
}
