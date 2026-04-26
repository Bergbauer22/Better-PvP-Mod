package net.bergbauer.better_pvp.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class IdManager {
    public static String getNameFromId(int id){
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.world != null;
        Entity entity = client.world.getEntityById(id);
        assert entity != null;
        return entity.getName().getString();
    }
}
