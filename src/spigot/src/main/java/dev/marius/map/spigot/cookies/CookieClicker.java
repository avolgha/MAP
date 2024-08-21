package dev.marius.map.spigot.cookies;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CookieClicker {
    public CookieClicker(@NotNull JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InteractionListener(), plugin);
    }
}
