package net.bergbauer.better_pvp;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Debug;

public class BetterPvP_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient()
    {
        KeyBindingHandler.register();
        TickEvent.registerTickEvent();
    }
}

