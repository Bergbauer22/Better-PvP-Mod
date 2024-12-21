package net.bergbauer.better_pvp;

import net.bergbauer.better_pvp.gui.TeamManager_Screen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static net.bergbauer.better_pvp.gui.TeamManager_Screen.TeamCategoryButton.colors;

public class PlayerColorLoader implements ClientModInitializer {
    public static final Map<String, TextColor> USER_COLORS = new HashMap<>();
    public static final String filePath = "config/team_objects.txt";

    @Override
    public void onInitializeClient() {
        loadUserColors(filePath);

    }
    public static void main(String[] args) {
        // Lade die Datei beim Start und beginne mit der Überwachung
        loadUserColors(filePath);

    }


    public static void loadUserColors(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            USER_COLORS.clear(); // Alte Einträge löschen

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 3) {
                    System.err.println("Ungültiges Zeilenformat: " + line);
                    continue;
                }

                String colorName = parts[1].trim();
                String[] players = parts[2].split(",");

                Formatting formattingColor = getFormattingFromName(colorName);

                if (formattingColor != null) {
                    TextColor color = TextColor.fromFormatting(formattingColor);
                    for (String player : players) {
                        player = player.trim();
                        USER_COLORS.put(player, color);
                    }
                } else {
                    System.err.println("Ungültige Farbe: " + colorName);
                }
            }

            // Beispielausgabe
            /*System.out.println("Spielerfarben wurden neu geladen:");
            for (Map.Entry<String, TextColor> entry : USER_COLORS.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Formatting getFormattingFromName(String colorName) {
        try {
            return Formatting.valueOf(colorName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static int ColorIndexOfPlayer(String playerName){
        if (USER_COLORS.containsKey(playerName)) {
            TextColor playerColor = USER_COLORS.get(playerName);

            // Suche den Index der Farbe im Texturen-Array
            return getColorIndex(playerColor);
        }
        else{
            return -1;
        }
    }
    // Hier überschreibst du die Methode, die die Textur des Spielers holt
    public static Identifier getCustomSkin(AbstractClientPlayerEntity player) {
        // Hole den Spielernamen

        String playerName = player.getName().getString();
        loadUserColors(filePath);
        // Prüfe, ob der Spieler in der USER_COLORS Map existiert
        if (USER_COLORS.containsKey(playerName)) {
            TextColor playerColor = USER_COLORS.get(playerName);

            // Suche den Index der Farbe im Texturen-Array
            int colorIndex = getColorIndex(playerColor);
            if (colorIndex != -1) {
                // Gib den Identifier der Textur zurück, basierend auf dem Farbindex
                return Identifier.of( "better_pvp","textures/entity/player/colored_player/skin" + colorIndex + ".png");
            }
            else{
                return player.getSkinTextures().texture();
            }
        }
        else {
            // Standard-Skin verwenden, falls es nicht der gesuchte Spieler ist
            return player.getSkinTextures().texture();
        }

    }
    public static int getColorIndex(TextColor color) {
        // Durchlaufe das colors-Array und vergleiche die Farben
        for (int i = 0; i < colors.length; i++) {
            if (color.equals(TextColor.fromFormatting(colors[i]))) {
                return i; // Gib den Index zurück, wenn die Farbe gefunden wurde
            }
        }
        // Wenn die Farbe nicht gefunden wurde, gib -1 zurück
        return -1;
    }

}