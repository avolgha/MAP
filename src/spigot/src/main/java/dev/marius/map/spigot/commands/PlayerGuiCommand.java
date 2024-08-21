package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.events.PlayerClickListener;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class PlayerGuiCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("playergui")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.playergui")))
                .executes(context -> {
                    Player player = Objects.requireNonNull((Player) context.getSource().getExecutor());

                    if (PlayerClickListener.ACCESS_LIST.contains(player)) {
                        PlayerClickListener.ACCESS_LIST.remove(player);
                        this.message(player, "You have not longer access to player guis");
                    } else {
                        PlayerClickListener.ACCESS_LIST.add(player);
                        this.message(player, "You have now access to player guis");
                    }

                    return SUCCESS;
                })
                .build();
    }
}
