package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class FlyCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("fly")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.fly")))
                .executes(context -> {
                    Player player = Objects.requireNonNull((Player) context.getSource().getExecutor());
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return SUCCESS;

                    if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        this.message(player, "You are not longer allowed to fly!");
                    } else {
                        player.setAllowFlight(true);
                        this.message(player, "You are allowed to fly now!");
                    }

                    return SUCCESS;
                })
                .build();
    }
}
