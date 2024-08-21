package dev.marius.map.spigot.events;

import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.commands.VanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.*;

public class VanishListener implements Listener {
    private final MAPlugin instance;

    public VanishListener(MAPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("map.bypass.vanish")) return;

        for (Player vanished : VanishCommand.IN_VANISH) {
            event.getPlayer().hidePlayer(instance, vanished);
        }
    }

    @EventHandler
    public void onQuit(@NotNull final PlayerQuitEvent event) {
        VanishCommand.IN_VANISH.remove(event.getPlayer());
    }
}
