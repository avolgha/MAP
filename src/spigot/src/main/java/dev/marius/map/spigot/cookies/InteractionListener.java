package dev.marius.map.spigot.cookies;

import dev.marius.map.spigot.MAPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InteractionListener implements Listener {
    public static final TextComponent invName = Component.text("Cookie Clicker").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD);

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Location point = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        if (MAPlugin.getConfiguration().cookieClicker.locations.stream().noneMatch(loc -> loc.getWorld().getName().equals(point.getWorld().getName()) && loc.getBlockX() == point.getBlockX() && loc.getBlockY() == point.getBlockY() && loc.getBlockZ() == point.getBlockZ()))
            return;

        Inventory inv = Bukkit.createInventory(null, 27, invName);
        inv.setItem(13, buildCookieItem(event.getPlayer().getUniqueId()));
        event.getPlayer().openInventory(inv);
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (!event.getView().title().contains(invName)) return;
        event.setCancelled(true);

        if (event.getClick() != ClickType.LEFT) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        if (item.getType() == Material.COOKIE) {
            int newAmount = MAPlugin.getConfiguration().cookieClicker.balance.getOrDefault(event.getWhoClicked().getUniqueId(), 0) + 1;
            MAPlugin.getConfiguration().cookieClicker.balance.put(event.getWhoClicked().getUniqueId(), newAmount);
            Objects.requireNonNull(event.getClickedInventory()).setItem(13, buildCookieItem(newAmount));
        }
    }

    public ItemStack buildCookieItem(UUID uuid) {
        return buildCookieItem(MAPlugin.getConfiguration().cookieClicker.balance.getOrDefault(uuid, 0));
    }

    public ItemStack buildCookieItem(int cookies) {
        ItemStack cookie = ItemStack.of(Material.COOKIE);
        cookie.editMeta(meta -> {
            meta.displayName(Component.text("FREE COOKIE").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text("Click me to receive Cookies!").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text("Your current amount:").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false).appendSpace().append(Component.text(cookies).color(NamedTextColor.RED))
            ));
        });
        return cookie;
    }
}
