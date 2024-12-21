package net.bergbauer.better_pvp;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
        if(Cooldown > 50){
            Cooldown = 0;
            PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
            System.out.println("Reload");
        }
    }
}
