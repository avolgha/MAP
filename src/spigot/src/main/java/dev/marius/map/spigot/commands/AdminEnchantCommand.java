package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.inventories.adminenchant.AEMainInventory;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class AdminEnchantCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("adminenchant")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.adminenchant")))
                .executes(context -> {
                    Player player = Objects.requireNonNull((Player) context.getSource().getExecutor());
                    ItemStack hand = player.getInventory().getItemInMainHand();

                    if (hand.getType() == Material.AIR) {
                        this.message(player, "You need to hold an item in your hand.", NamedTextColor.RED);
                        return SUCCESS;
                    }

                    AEMainInventory inventory = new AEMainInventory(hand);
                    inventory.openInventory(player);

                    return SUCCESS;
                })
                .build();
    }
}
