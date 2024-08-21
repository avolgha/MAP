package dev.marius.map.spigot;

import org.bukkit.Location;

import java.util.*;

public class Config {
    public LobbyConfig lobby = new LobbyConfig();
    public CookieClickerConfig cookieClicker = new CookieClickerConfig();
    public MaintenanceConfig maintenance = new MaintenanceConfig();
    public String autorunScript = "";

    public static class LobbyConfig {
        public boolean noBuild = true;
        public boolean autoSurvival = true;
        public Location spawnLocation = null;
    }

    public static class CookieClickerConfig {
        public boolean enabled = true;
        public List<Location> locations = new ArrayList<>();
        public Map<UUID, Integer> balance = new HashMap<>();
    }

    public static class MaintenanceConfig {
        public boolean enabled = false;
        public String extraMessage = null;
    }
}
