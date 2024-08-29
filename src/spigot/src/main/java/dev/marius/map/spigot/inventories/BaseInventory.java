package dev.marius.map.spigot.inventories;

import dev.marius.map.spigot.util.ItemUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BaseInventory implements InventoryHolder {
    public final int width;
    public final int height;

    protected final Inventory inventory;

    public BaseInventory(int width, int height, Component inventoryName) {
        this.width = width;
        this.height = height;
        this.inventory = Bukkit.createInventory(this, this.width * this.height, inventoryName);
    }

    public abstract void fillInventory();

    public void openInventory(@NotNull Player player) {
        player.openInventory(this.inventory);
    }

    protected void addCloseButton() {
        this.inventory.setItem(this.width * (this.height - 1) + Math.floorDiv(this.width, 2), this.getCloseButton());
    }

    protected ItemStack getCloseButton() {
        ItemStack close = ItemStack.of(Material.BARRIER);
        close.editMeta(meta -> meta.displayName(Component.text("Close")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)));
        return close;
    }

    protected void fillBorder() {
        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.height; j++)
                if (i == 0 || i == this.width - 1 || j == 0 || j == this.height - 1)
                    this.inventory.setItem(j * this.width + i, ItemUtility.getPlaceholderStack());
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }
}
