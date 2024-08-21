package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.FinePosition;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class TeleportToCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("tpto")
                .then(Commands.literal("player")
                        .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.tpto.player")))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> {
                                    PlayerSelectorArgumentResolver resolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    List<Player> targets = resolver.resolve(context.getSource());
                                    if (targets.size() > 1) {
                                        this.message(Objects.requireNonNull(context.getSource().getExecutor()), "Found two many target players.");
                                        return 2;
                                    }
                                    Objects.requireNonNull((Player) context.getSource().getExecutor()).teleport(targets.getFirst(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                                    return SUCCESS;
                                }))
                    )
                .then(Commands.literal("coordinate")
                        .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.tpto.coordinate")))
                        .then(Commands.argument("coordinate", ArgumentTypes.finePosition())
                                .executes(context -> {
                                    Player player = Objects.requireNonNull((Player) context.getSource().getExecutor());
                                    FinePositionResolver resolver = context.getArgument("coordinate", FinePositionResolver.class);
                                    FinePosition position = resolver.resolve(context.getSource());

                                    player.teleport(position.toLocation(player.getWorld()));
                                    return SUCCESS;
                                })))
                .then(Commands.literal("world")
                        .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.tpto.world")))
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .executes(context -> {
                                    World world = context.getArgument("world", World.class);
                                    Objects.requireNonNull((Player) context.getSource().getExecutor()).teleport(world.getSpawnLocation());
                                    return SUCCESS;
                                })))
                .build();
    }
}
