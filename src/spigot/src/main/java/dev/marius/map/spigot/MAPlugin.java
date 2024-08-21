package dev.marius.map.spigot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.marius.map.script.Script;
import dev.marius.map.spigot.cookies.CookieClicker;
import dev.marius.map.spigot.events.*;
import dev.marius.map.spigot.util.LocationTypeAdapter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public final class MAPlugin extends JavaPlugin {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationTypeAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();
    public static MAPlugin instance;
    private static Config config;

    private final File configFile = new File("plugins\\MAP\\config.json");

    public static Gson getGson() {
        return gson;
    }

    @NotNull
    public static Config getConfiguration() {
        return config;
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            this.loadConfig();
        } catch (IOException e) {
            getLogger().throwing("Plugin", "loadConfig", e);
        }

        getServer().getPluginManager().registerEvents(new LobbyJoinListener(), this);
        getServer().getPluginManager().registerEvents(new MaintenanceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerClickListener(), this);
        getServer().getPluginManager().registerEvents(new VanishListener(this), this);

        if (config.lobby.noBuild) {
            getServer().getConsoleSender().sendMessage("[MAP] Enabled Lobby No-Build");
            getServer().getPluginManager().registerEvents(new NoBuildListener(), this);
        }

        if (config.cookieClicker.enabled) {
            getServer().getConsoleSender().sendMessage("[MAP] Enabled Cookie Clicker");
            new CookieClicker(this);
        }


  /*      for (Command command : new Command[] {
                new AlertCommand(),
                new FlyCommand(),
                new GamemodeCommand(),
                new PlayerguiCommand(),
                new QuickGuiCommand(),
                new StaffChatCommand(),
                new TpHereCommand(),
                new TpToCommand(),
                new VanishCommand()
        }) command.register(this);
*/
        if (config.autorunScript.trim().length() > 1) {
            File scriptFile = new File(this.getDataFolder(), config.autorunScript);
            if (!scriptFile.exists()) {
                this.getLogger().warning("Could not find autorun script in config folder.");
            } else if (!scriptFile.isFile() || !scriptFile.toPath().endsWith(".js")) {
                this.getLogger().warning("Autorun Script is supposed to be a JavaScript-file.");
            } else {
                Script.launchScript(scriptFile, this, new String[0], Map.of("console", this.getLogger()), result -> {
                    this.getLogger().info("Script ended with code " + result.name());
                });
            }
        }

        getServer().getConsoleSender().sendMessage("[MAP] Enabled plugin");
    }

    @Override
    public void onDisable() {
        try (FileWriter writer = new FileWriter(getConfigFile())) {
            writer.write(getGson().toJson(getConfiguration(), Config.class));
        } catch (IOException e) {
            getLogger().throwing("Plugin", "writeConfig", e);
        }
        getServer().getConsoleSender().sendMessage("[MAP] Disabled plugin");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadConfig() throws IOException {
        String content;
        File file = getConfigFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            content = "{}";
        } else {
            try (FileReader reader = new FileReader(file)) {
                StringBuilder builder = new StringBuilder();
                int current;
                while ((current = reader.read()) != -1) builder.append((char) current);
                content = builder.toString();
            }
        }
        config = getGson().fromJson(content, Config.class);
    }

    public File getConfigFile() {
        return configFile;
    }
}
