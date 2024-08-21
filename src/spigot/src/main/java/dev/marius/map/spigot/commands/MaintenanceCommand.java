package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.MAPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class MaintenanceCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("maintenance")
                .requires(source -> !(source.getExecutor() instanceof Player player) || (player.isOp() || player.hasPermission("map.maintenance")))
                .executes(context -> {
                    if (MAPlugin.getConfiguration().maintenance.enabled) {
                        MAPlugin.getConfiguration().maintenance.enabled = false;
                        this.message(Objects.requireNonNull(context.getSource().getExecutor()), "Maintenance Mode is now disabled!", NamedTextColor.RED);
                    } else {
                        Component message;
                        message = Component.text(Objects.requireNonNullElse(
                                MAPlugin.getConfiguration().maintenance.extraMessage,
                                "the server is currently in maintenance mode! Come back later."
                        )).color(NamedTextColor.GRAY);
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (online.isOp() || online.hasPermission("map.bypass.maintenance")) continue;
                            online.kick(Component.join(
                                    JoinConfiguration.newlines(),
                                    Component.text("MAINTENANCE").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                                    Component.empty(),
                                    message
                            ));
                        }

                        MAPlugin.getConfiguration().maintenance.enabled = true;
                        this.message(Objects.requireNonNull(context.getSource().getExecutor()), "Maintenance Mode is now enabled!", NamedTextColor.RED);
                    }

                    return SUCCESS;
                })
                .build();
    }
}
