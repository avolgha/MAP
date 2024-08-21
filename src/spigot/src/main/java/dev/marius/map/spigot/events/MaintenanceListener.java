package dev.marius.map.spigot.events;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import dev.marius.map.spigot.MAPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Objects;

public class MaintenanceListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConnect(PlayerLoginEvent event) {
        if (!MAPlugin.getConfiguration().maintenance.enabled) return;
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("map.bypass.maintenance")) return;

        Component message;
        message = Component.text(Objects.requireNonNullElse(
                MAPlugin.getConfiguration().maintenance.extraMessage,
                "the server is currently in maintenance mode! Come back later."
        )).color(NamedTextColor.GRAY);

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.join(
                JoinConfiguration.newlines(),
                Component.text("MAINTENANCE").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD),
                Component.empty(),
                message
        ));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMotd(PaperServerListPingEvent event) {
        if (!MAPlugin.getConfiguration().maintenance.enabled) return;
        event.setMaxPlayers(0);
        event.setProtocolVersion(-1);
        event.setVersion("Maintenance");
        event.setHidePlayers(true);
    }
}
