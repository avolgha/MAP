package dev.marius.map.spigot.events;

import dev.marius.map.spigot.inventories.adminenchant.AEMainInventory;
import dev.marius.map.spigot.inventories.adminenchant.AESpecificInventory;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdminEnchantListener implements Listener {
    @EventHandler
    public void onInventoryInteract(@NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!player.isOp() || !player.hasPermission("map.adminenchant")) return;

        if (event.getInventory().getHolder(false) instanceof AEMainInventory inventory) {
            this.handleMainInventory(event, player, inventory);
        }

        if (event.getInventory().getHolder(false) instanceof AESpecificInventory inventory) {
            this.handleSpecificInventory(event, player, inventory);
        }
    }

    private void handleSpecificInventory(@NotNull InventoryClickEvent event, Player player, AESpecificInventory inventory) {
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
        } else if (clicked.getType() == Material.ARROW) {
            inventory.parent.openInventory(player);
        } else if (clicked.getType() == Material.REDSTONE) {
            inventory.item.removeEnchantment(inventory.enchantment);
            inventory.parent.openInventory(player);
        } else if (clicked.getType() == Material.ENCHANTED_BOOK) {
            // NOTE(avolgha): enchantments are only listed from slot 19 on.
            if (event.getSlot() < 19) return;

            @SuppressWarnings("DataFlowIssue") int level = clicked.getItemMeta()
                    .getPersistentDataContainer()
                    .get(AESpecificInventory.levelKey, PersistentDataType.INTEGER);
            inventory.item.addEnchantment(inventory.enchantment, level);
            inventory.parent.openInventory(player);
        }
    }

    private void handleMainInventory(@NotNull InventoryClickEvent event, Player player, AEMainInventory inventory) {
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
        } else if (clicked.getType() == Material.ENCHANTED_BOOK) {
            NamespacedKey key = NamespacedKey.fromString(Objects.requireNonNull(clicked.getItemMeta()
                    .getPersistentDataContainer()
                    .get(AEMainInventory.enchantmentKey, PersistentDataType.STRING)));
            assert key != null;
            Enchantment enchantment = RegistryAccess.registryAccess()
                    .getRegistry(RegistryKey.ENCHANTMENT)
                    .get(key);
            assert enchantment != null;

            new AESpecificInventory(inventory, inventory.item, enchantment).openInventory(player);
        } else if (clicked.getType() == Material.ARROW) {
            int change = clicked.getItemMeta().getPersistentDataContainer().getOrDefault(AEMainInventory.pageArrowKey, PersistentDataType.INTEGER, 0);
            int newPage = inventory.currentPage + change;
            player.closeInventory();
            inventory.fillInventory(newPage);
            inventory.openInventory(player);
        }
    }
}
