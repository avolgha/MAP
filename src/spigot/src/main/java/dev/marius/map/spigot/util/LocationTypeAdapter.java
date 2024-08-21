package dev.marius.map.spigot.util;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationTypeAdapter implements JsonDeserializer<Location>, JsonSerializer<Location>, InstanceCreator<Location> {
    @Override
    public Location createInstance(Type type) {
        return new Location(Bukkit.getWorld("world"), 0, 0, 0);
    }

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String worldName = object.get("world").getAsString();
        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();
        float yaw = object.get("yaw").getAsFloat();
        float pitch = object.get("pitch").getAsFloat();

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("world", src.getWorld().getName());
        object.addProperty("x", src.getX());
        object.addProperty("y", src.getY());
        object.addProperty("z", src.getZ());
        object.addProperty("yaw", src.getYaw());
        object.addProperty("pitch", src.getPitch());

        return object;
    }
}
