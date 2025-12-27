package net.bergbauer.better_pvp.gui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.ExecutionException;

public class GameProfileUtils {

    public static Identifier getSkinTextureByName(String playerName) throws ExecutionException, InterruptedException {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getNetworkHandler() == null) {
            return Identifier.of(("minecraft:textures/entity/player/wide/steve.png"));
        }

        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            GameProfile profile = entry.getProfile();

            if (profile.name().equalsIgnoreCase(playerName)) {
                Identifier skin = entry.getSkinTextures().body().texturePath();
                if(skin == null) return Identifier.of(("minecraft:textures/entity/player/wide/steve.png"));
                return skin;
            }
        }

        return Identifier.of(("minecraft:textures/entity/player/wide/steve.png"));
    }
}