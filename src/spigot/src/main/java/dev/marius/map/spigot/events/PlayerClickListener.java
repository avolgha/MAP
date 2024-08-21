package dev.marius.map.spigot.events;

import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.inventories.PlayerGuiInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerClickListener implements Listener {
    public static final List<Player> ACCESS_LIST = new ArrayList<>();

    @EventHandler
    public void onPlayerInteractAtEntityEventListener(@NotNull final PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Player target) {
            Player player = event.getPlayer();

            if (player.hasPermission("map.playergui") && ACCESS_LIST.contains(player)) {
                event.setCancelled(true);
                PlayerGuiInventory inventory = new PlayerGuiInventory(target);
                inventory.openInventory(player);
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(@NotNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!player.hasPermission("map.playergui")) return;
        if (!ACCESS_LIST.contains(player)) return;
        if (!(event.getInventory().getHolder(false) instanceof PlayerGuiInventory inventory)) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        if (!(event.getView().title() instanceof TextComponent title)) {
            MAPlugin.instance.getLogger().warning("Could not cast getView().title() to TextComponent. Real class is: " + event.getView().title().getClass().getName());
            return;
        }

        Player target = inventory.getTarget();

        switch (event.getCurrentItem().getType()) {
            case RED_CONCRETE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + target.getName() + " You were banned by an operator");
                event.getWhoClicked().closeInventory();
                break;
            case GREEN_CONCRETE:
                target.kick(Component.text("You were kicked by an operator").color(NamedTextColor.RED), PlayerKickEvent.Cause.TIMEOUT);
                event.getWhoClicked().closeInventory();
                break;
            default:
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0F, 1.0F);
                break;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(@NotNull final PlayerQuitEvent event) {
        ACCESS_LIST.remove(event.getPlayer());
    }
}
