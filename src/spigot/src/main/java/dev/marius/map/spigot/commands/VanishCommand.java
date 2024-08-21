package dev.marius.map.spigot.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.commands.argument.BooleanArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class VanishCommand extends BaseCommand {
    public static final List<Player> IN_VANISH = new ArrayList<>();

    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("vanish")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.vanish")))
                .executes(context -> vanish(context, false))
                .then(Commands.argument("cool", new BooleanArgumentType())
                        .executes(context -> vanish(context, context.getArgument("cool", Boolean.class))))
                .build();
    }

    public int vanish(@NotNull CommandContext<CommandSourceStack> context, boolean coolVanish) {
        Player player = Objects.requireNonNull((Player) context.getSource().getExecutor());

        if (IN_VANISH.contains(player)) {
            IN_VANISH.remove(player);

            for (Player target : Bukkit.getOnlinePlayers()) target.showPlayer(MAPlugin.instance, player);

            this.message(player, "You are not longer vanished to other players");
        } else {
            if (coolVanish) {
                player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
            }

            IN_VANISH.add(player);

            for (Player target : Bukkit.getOnlinePlayers()) {
                if (!target.hasPermission("map.bypass.vanish")) {
                    target.hidePlayer(MAPlugin.instance, player);
                }
            }

            this.message(player, "You are vanished now to other players (except admins)");
        }

        return SUCCESS;
    }
}
