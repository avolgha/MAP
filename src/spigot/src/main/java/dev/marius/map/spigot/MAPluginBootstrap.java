package dev.marius.map.spigot;

import dev.marius.map.spigot.commands.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class MAPluginBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands registry = event.registrar();

            registry.register(new AlertCommand().node(), "Broadcast some alert message on the server");
            registry.register(new FlyCommand().node(), "Creative flight for any gamemode");
            registry.register(new GameModeCommand().node(), "Changes your gamemode", List.of("gm"));
            registry.register(new MaintenanceCommand().node(), "Toggle maintenance mode");
            registry.register(new PlayerGuiCommand().node(), "Toggles PlayerGUI mode");
            registry.register(new QuickGuiCommand().node(), "Quickly open any Minecraft-native GUI");
            registry.register(new RunAsCommand().node(), "Run commands as another player");
            registry.register(new StaffChatCommand().node(), "Sends messages to a staff only chat");
            registry.register(new TeleportHereCommand().node(), "Teleports a player to you");
            registry.register(new TeleportToCommand().node(), "Teleports you to a player or to a coordinate");
            registry.register(new VanishCommand().node(), "Vanishes you to other players", List.of("v"));
        });
    }
}
