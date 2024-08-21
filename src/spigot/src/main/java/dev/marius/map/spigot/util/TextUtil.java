package dev.marius.map.spigot.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TextUtil {
    public static @NotNull Component colorifyText(@NotNull String map, TextColor... colors) {
        AtomicInteger index = new AtomicInteger();
        return Component.join(
                JoinConfiguration.noSeparators(),
                Arrays.stream(map.split(""))
                        .map(bit -> Component.text(bit)
                                .color(colors[index.getAndIncrement() % colors.length]))
                        .toArray(Component[]::new));
    }
}
