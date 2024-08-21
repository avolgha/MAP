package dev.marius.map.spigot.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.BuiltInExceptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class InventoryTypeArgumentType implements CustomArgumentType<InventoryType, SignedMessageResolver> {
    @Override
    public @NotNull InventoryType parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        String typeName = stringReader.readString();
        try {
            return InventoryType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            throw new BuiltInExceptions().dispatcherUnknownArgument().createWithContext(stringReader);
        }
    }

    @Override
    public @NotNull ArgumentType<SignedMessageResolver> getNativeType() {
        return ArgumentTypes.signedMessage();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for (InventoryType type : InventoryType.values()) {
            builder.suggest(type.name());
        }
        return builder.buildFuture();
    }
}
