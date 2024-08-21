package dev.marius.map.spigot.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class BooleanArgumentType implements CustomArgumentType<Boolean, SignedMessageResolver> {
    @Override
    public @NotNull Boolean parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readBoolean();
    }

    @Override
    public @NotNull ArgumentType<SignedMessageResolver> getNativeType() {
        return ArgumentTypes.signedMessage();
    }

    @Override
    public @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext context, @NotNull SuggestionsBuilder builder) {
        return builder.suggest("true").suggest("false").buildFuture();
    }
}
