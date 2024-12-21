package net.bergbauer.better_pvp;
import net.bergbauer.better_pvp.gui.BetterPvP_MenuScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
public class KeyBindingHandler {
    // KeyBinding Objekt für die Taste 'G'
    public static KeyBinding keyBinding;
    public static KeyBinding debugBinding;
    public static void register() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "OpenGUI",  // Der Name der Keybinding (wird in den Optionen angezeigt)
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,               // Die Taste 'G'
                "Better-PvP"   // Kategorie, unter der die Keybinding erscheint
        ));
        // Registriere ein Tick-Event, um auf das Drücken der Taste zu reagieren
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                displayMenu();
            }
        });
    }


    private static void displayMenu() {
        // Öffne das Menü, wenn die Taste G gedrückt wurde
        MinecraftClient.getInstance().setScreen(new BetterPvP_MenuScreen());
        PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
    }

}

