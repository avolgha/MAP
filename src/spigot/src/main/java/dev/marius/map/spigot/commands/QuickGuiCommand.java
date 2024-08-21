package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.commands.argument.InventoryTypeArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class QuickGuiCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("quickgui")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.quickgui")))
                .then(Commands.argument("gui", new InventoryTypeArgumentType())
                        .executes(context -> {
                            InventoryType type = context.getArgument("gui", InventoryType.class);
                            Objects.requireNonNull((Player) context.getSource().getExecutor()).openInventory(Bukkit.createInventory(null, type));
                            return SUCCESS;
                        }))
                .build();
    }
}
