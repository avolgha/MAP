package dev.marius.map.spigot.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

public class ItemUtility {
    private static final ItemStack placeholderStack;

    static {
        placeholderStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        placeholderStack.editMeta(meta -> meta.displayName(Component.empty()));
    }

    public static @NotNull ItemStack getPlaceholderStack() {
        return placeholderStack;
    }
}
