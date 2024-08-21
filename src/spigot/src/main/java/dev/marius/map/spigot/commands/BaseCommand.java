package dev.marius.map.spigot.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.marius.map.spigot.MAPlugin;
import dev.marius.map.spigot.util.TextUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseCommand {
    public static final Component prefix = Component.join(
            JoinConfiguration.spaces(),
            TextUtil.colorifyText(
                    "MAP",
                    TextColor.fromCSSHexString("#af69ef"),
                    TextColor.fromCSSHexString("#b65fcf"),
                    TextColor.fromCSSHexString("#9867c5")
            ).decorate(TextDecoration.BOLD),
            Component.text("»")
                    .color(NamedTextColor.DARK_GRAY)
    );
    protected static int SUCCESS = Command.SINGLE_SUCCESS;

    public abstract LiteralCommandNode<CommandSourceStack> node();

    protected void message(@NotNull Audience audience, String message) {
        this.message(audience, Component.text(message));
    }

    protected void message(@NotNull Audience audience, String message, TextColor color) {
        this.message(audience, Component.text(message).color(color));
    }

    protected void message(@NotNull Audience audience, Component component) {
        audience.sendMessage(prefix.appendSpace().append(component));
    }
}
