package dev.marius.map.spigot.inventories.adminenchant;

import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.inventories.BaseInventory;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AEMainInventory extends BaseInventory {
    public static final NamespacedKey pageArrowKey = new NamespacedKey(MAPlugin.instance, "page-arrow");
    public static final NamespacedKey enchantmentKey = new NamespacedKey(MAPlugin.instance, "enchantment");
    protected static final TextComponent baseInventoryName = Component.text("Admin Enchant")
            .decorate(TextDecoration.BOLD)
            .color(NamedTextColor.GOLD);

    public final ItemStack item;
    public int currentPage;
    private final List<Enchantment> applicable;
    private final int pages;

    public AEMainInventory(@NotNull ItemStack item) {
        super(9, 5, baseInventoryName);
        this.item = item;
        this.applicable = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.ENCHANTMENT)
                .stream()
                .filter(enchantment -> enchantment.canEnchantItem(item))
                .sorted(Comparator.comparing(Keyed::key))
                .toList();
        this.currentPage = 0;
        this.pages = Math.ceilDiv(this.applicable.size(), (this.width - 2) * (this.height - 2));

        this.fillInventory();
    }

    @Override
    public void fillInventory() {
        this.fillInventory(1);
    }

    public void fillInventory(int page) {
        this.currentPage = page;

        this.fillBorder();
        this.addCloseButton();

        if (this.applicable.isEmpty()) {
            ItemStack info = ItemStack.of(Material.DEAD_BUSH);
            info.editMeta(meta -> meta.displayName(Component.text("No Enchantments found.").color(NamedTextColor.RED)));
            this.inventory.setItem(Math.floorDiv(this.width * this.height, 2), info);
            return;
        }

        if (page > 1) {
            ItemStack arrow = ItemStack.of(Material.ARROW);
            arrow.editMeta(meta -> {
                meta.displayName(Component.text("Previous page")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false));
                meta.getPersistentDataContainer().set(pageArrowKey, PersistentDataType.INTEGER, -1);
            });
            this.inventory.setItem(this.width * (this.height - 1), arrow);
        }

        if (page != pages) {
            ItemStack arrow = ItemStack.of(Material.ARROW);
            arrow.editMeta(meta -> {
                meta.displayName(Component.text("Next page")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false));
                meta.getPersistentDataContainer().set(pageArrowKey, PersistentDataType.INTEGER, 1);
            });
            this.inventory.setItem(this.width * this.height - 1, arrow);
        }

        int offset = (page - 1) * (this.width - 2 * this.height - 2);
        for (int y = 1; y <= this.height - 2; y++) {
            for (int x = 1; x <= this.width - 2; x++) {
                int i = (y - 1) * this.width + (x - 1) + offset;
                if (i >= this.applicable.size()) return;

                int pos = y * this.width + x;
                this.inventory.setItem(pos, this.getEnchantmentItem(this.applicable.get(i)));
            }
        }
    }

    protected @NotNull ItemStack getEnchantmentItem(Enchantment enchantment) {
        return this.getEnchantmentItem(enchantment, -1);
    }

    protected @NotNull ItemStack getEnchantmentItem(Enchantment enchantment, int level) {
        // NOTE(avolgha): this could be expanded to be more flexible but minecraft does only allow
        //                enchantments to be between level one and five so it really does not matter.
        boolean showRoman = level != -1;
        String roman = this.getRoman(level);

        ItemStack book = ItemStack.of(Material.ENCHANTED_BOOK);
        book.editMeta(meta -> {
            meta.displayName(Component.text(formatKey(enchantment.key()) + (showRoman ? " " + roman : ""))
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.BOLD)
                    .decoration(TextDecoration.ITALIC, false));
            meta.getPersistentDataContainer().set(enchantmentKey, PersistentDataType.STRING, enchantment.getKey().getKey());
        });
        return book;
    }

    protected String getRoman(int num) {
        switch (num) {
            case -1 -> {
                return null;
            }
            case 1 -> {
                return "I";
            }
            case 2 -> {
                return "II";
            }
            case 3 -> {
                return "III";
            }
            case 4 -> {
                return "IV";
            }
            case 5 -> {
                return "V";
            }
            default -> throw new RuntimeException("0 < enchantment level < 6 :: cannot convert to roman numeral");
        }

    }

    protected String formatKey(@NotNull Key key) {
        return Arrays.stream(key.asMinimalString()
                        .split("_"))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
