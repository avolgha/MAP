package dev.marius.map.spigot.events;

import dev.marius.map.spigot.MAPlugin;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (MAPlugin.getConfiguration().lobby.spawnLocation != null) {
            event.getPlayer().teleport(MAPlugin.getConfiguration().lobby.spawnLocation);
        }

        if (MAPlugin.getConfiguration().lobby.autoSurvival) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
}
