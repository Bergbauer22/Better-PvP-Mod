package net.bergbauer.better_pvp;

import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class TickEvent {
    static int Cooldown = 0;


    public static void registerTickEvent() {
        // Registriere das Tick-Event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.world != null) {
                // Führe hier deine wiederkehrende Logik aus
                executeGameLogic();
            }
        });
    }

    private static void executeGameLogic() {
        // Hier deine Logik, die pro Tick ausgeführt wird
        Cooldown++;
        if(Cooldown > 10){
            Cooldown = 0;
            PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
            if(MinecraftClient.getInstance().currentScreen != null){
                if(MinecraftClient.getInstance().currentScreen instanceof Settings_Screen){
                    return;
                }
            }
            Settings_Screen.loadSettings();
        }
    }
}
