package dev.marius.map.spigot.inventories.adminenchant;

import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.inventories.BaseInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AESpecificInventory extends BaseInventory {
    public static final NamespacedKey levelKey = new NamespacedKey(MAPlugin.instance, "level");;
    private static final TextComponent baseInventoryName = Component.text("Admin Enchant")
            .decorate(TextDecoration.BOLD)
            .color(NamedTextColor.GOLD);

    public final AEMainInventory parent;
    public final ItemStack item;
    public final Enchantment enchantment;

    public AESpecificInventory(AEMainInventory parent, ItemStack item, @NotNull Enchantment enchantment) {
        super(9, 5, baseInventoryName);

        if (!enchantment.canEnchantItem(item)) {
            throw new RuntimeException("Enchantment is not applicable to item.");
        }

        this.parent = parent;
        this.item = item;
        this.enchantment = enchantment;

        this.fillInventory();
    }

    @Override
    public void fillInventory() {
        this.fillBorder();
        this.addCloseButton();

        ItemStack arrow = ItemStack.of(Material.ARROW);
        arrow.editMeta(meta -> meta.displayName(Component.text("Back")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)));
        this.inventory.setItem(this.width * (this.height - 1), arrow);

        ItemStack enchantmentItem = this.parent.getEnchantmentItem(this.enchantment);
        this.inventory.setItem(Math.floorDiv(this.width, 2), enchantmentItem);

        ItemStack removeItem = ItemStack.of(Material.REDSTONE);
        removeItem.editMeta(meta -> meta.displayName(Component.text("Remove Enchantment")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false)));
        this.inventory.setItem(19, removeItem);

        int currentLevel = this.item.getEnchantmentLevel(this.enchantment);
        for (int i = 1; i <= this.enchantment.getMaxLevel(); i++) {
            ItemStack item;
            if (currentLevel == i) {
                item = ItemStack.of(Material.RED_CONCRETE);
                final String roman = this.parent.getRoman(i);
                item.editMeta(meta -> meta.displayName(Component.text(this.parent.formatKey(enchantment.key()) + " " + roman)
                        .color(NamedTextColor.LIGHT_PURPLE)
                        .decorate(TextDecoration.BOLD)
                        .decoration(TextDecoration.ITALIC, false)));
            } else {
                item = this.parent.getEnchantmentItem(this.enchantment, i);
                final int level = i;
                item.editMeta(meta -> {
                    meta.lore(List.of(Component.text("Click to apply!")
                            .color(NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)));
                    meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
                });
            }

            this.inventory.setItem(19 + i, item);
        }
    }
}
