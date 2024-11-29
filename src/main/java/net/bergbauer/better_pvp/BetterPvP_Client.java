package net.bergbauer.better_pvp;

import net.fabricmc.api.ClientModInitializer;

public class BetterPvP_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHandler.register();
    }
}
