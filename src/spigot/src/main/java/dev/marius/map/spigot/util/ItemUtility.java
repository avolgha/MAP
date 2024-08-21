package dev.marius.map.spigot.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

public class ItemUtility {
    public static @NotNull ItemStack getPlaceholderStack() {
        ItemStack stack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        stack.editMeta(meta -> meta.displayName(Component.empty()));
        return stack;
    }
}
