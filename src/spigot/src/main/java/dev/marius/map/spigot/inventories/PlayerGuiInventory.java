package dev.marius.map.spigot.inventories;

import dev.marius.map.spigot.util.ItemUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;

public class PlayerGuiInventory implements InventoryHolder {
    private static final TextComponent baseInventoryName = Component.text("").append(Component.join(
            JoinConfiguration.spaces(),
            Component.text("»").color(NamedTextColor.DARK_GRAY),
            Component.text("PlayerGUI").decorate(TextDecoration.BOLD).color(NamedTextColor.DARK_BLUE),
            Component.text("-").color(NamedTextColor.DARK_GRAY)
    ));

    private final Inventory inventory;
    private final Player target;

    public PlayerGuiInventory(@NotNull Player target) {
        this.inventory = Bukkit.createInventory(this, 9 * 6, baseInventoryName.append(target.name()));
        this.target = target;
        this.fillInventory();
    }

    public void fillInventory() {
        for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, ItemUtility.getPlaceholderStack());

        inventory.setItem(13, getHeadItem(target));
        inventory.setItem(20, getKickButton());
        inventory.setItem(24, getBanButton());
    }

    public void openInventory(@NotNull Player player) {
        player.openInventory(this.inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Player getTarget() {
        return target;
    }

    private @NotNull ItemStack getBanButton() {
        ItemStack stack = new ItemStack(Material.RED_CONCRETE);
        stack.editMeta(meta -> meta.displayName(Component.text("Ban player").color(NamedTextColor.RED)));
        return stack;
    }

    private @NotNull ItemStack getKickButton() {
        ItemStack stack = new ItemStack(Material.GREEN_CONCRETE);
        stack.editMeta(meta -> meta.displayName(Component.text("Kick player").color(NamedTextColor.GREEN)));
        return stack;
    }

    private @NotNull ItemStack getHeadItem(@NotNull Player target) {
        BiFunction<String, Object, Component> loreSupplier = (s, o) -> Component.join(
                JoinConfiguration.spaces(),
                Component.text("»").color(NamedTextColor.DARK_GRAY),
                Component.text(s).color(NamedTextColor.GREEN).append(Component.text(":").color(NamedTextColor.DARK_GRAY)),
                Component.text(o.toString()).color(NamedTextColor.GRAY)
        );

        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        stack.editMeta(SkullMeta.class, meta -> {
            meta.displayName(Component.join(
                    JoinConfiguration.spaces(),
                    Component.text("»").color(NamedTextColor.DARK_GRAY),
                    target.name(),
                    Component.text("«").color(NamedTextColor.DARK_GRAY)
            ));

            meta.lore(Arrays.asList(
                    loreSupplier.apply("ID", target.getUniqueId()),
                    loreSupplier.apply("Health", String.format("%.2f", target.getHealth() * 1.0F)),
                    loreSupplier.apply("Ping", target.getPing()),
                    loreSupplier.apply("Address", Objects.requireNonNull(target.getAddress()).getHostName()),
                    loreSupplier.apply("Locale", target.locale().toString()),
                    loreSupplier.apply("GameMode", target.getGameMode().name())
            ));
        });

        return stack;
    }
}
