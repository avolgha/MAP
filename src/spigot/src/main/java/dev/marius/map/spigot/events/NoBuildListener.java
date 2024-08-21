package dev.marius.map.spigot.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class NoBuildListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("map.bypass.nobuild")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("map.bypass.nobuild")) return;

        event.setCancelled(true);
    }
}
