package dev.marius.map.spigot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class GameModeCommand extends BaseCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("gamemode")
                .requires(source -> source.getExecutor() instanceof Player player && (player.isOp() || player.hasPermission("map.gamemode")))
                .then(Commands.argument("gamemode", ArgumentTypes.gameMode())
                        .executes(context -> {
                            GameMode mode = context.getArgument("gamemode", GameMode.class);
                            ((Player) Objects.requireNonNull(context.getSource().getExecutor())).setGameMode(mode);
                            return SUCCESS;
                        })
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> {
                                    GameMode mode = context.getArgument("gamemode", GameMode.class);
                                    PlayerSelectorArgumentResolver resolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

                                    for (Player target : resolver.resolve(context.getSource())) {
                                        target.setGameMode(mode);
                                        this.message(
                                                Objects.requireNonNull((Player) context.getSource().getExecutor()),
                                                Component.text("You set the GameMode of")
                                                        .appendSpace()
                                                        .append(target.displayName())
                                                        .appendSpace()
                                                        .append(Component.text("to"))
                                                        .appendSpace()
                                                        .append(Component.translatable(mode.translationKey()))
                                        );
                                    }
                                    return SUCCESS;
                                })))
                .build();
    }
}
