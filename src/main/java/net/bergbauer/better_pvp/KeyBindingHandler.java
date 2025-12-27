package net.bergbauer.better_pvp;

import net.bergbauer.better_pvp.gui.BetterPvP_MenuScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    // KeyBindingCategory für Better PvP
    private static final KeyBinding.Category keyBindingCategory  = KeyBinding.Category.create(Identifier.of("better_pvp:better_pvp"));
    // KeyBinding Objekt für die Taste 'B'
    private static final KeyBinding guiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "OpenGUI",  // Der Name der Keybinding (wird in den Optionen angezeigt)
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            keyBindingCategory   // Kategorie, unter der die Keybinding erscheint
    ));

    private static void tickKeybindings(MinecraftClient client) {
        while (KeyBindingHandler.guiKeyBinding.wasPressed()) {
            displayMenu();
        }
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(KeyBindingHandler::tickKeybindings);
    }

    private static void displayMenu() {
        // Öffne das Menü, wenn die Taste B gedrückt wurde
        MinecraftClient.getInstance().setScreen(new BetterPvP_MenuScreen());
        PlayerColorLoader.loadUserColors(PlayerColorLoader.filePath);
    }

}
