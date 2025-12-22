package net.bergbauer.better_pvp;

import net.fabricmc.api.ClientModInitializer;

import static net.bergbauer.better_pvp.PlayerColorLoader.loadUserColors;

public class BetterPvP_Client implements ClientModInitializer {
    public static final String filePathColorLoad = "config/team_objects.txt";
    @Override
    public void onInitializeClient()
    {
        KeyBindingHandler.register();
        TickEvent.registerTickEvent();
        loadUserColors(filePathColorLoad);
    }
}

