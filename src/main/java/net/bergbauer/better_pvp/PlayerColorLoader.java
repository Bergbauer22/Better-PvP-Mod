package net.bergbauer.better_pvp;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

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
            System.out.println("Spielerfarben wurden neu geladen:");
            for (Map.Entry<String, TextColor> entry : USER_COLORS.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

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
}
    /*
    public static final Map<String, TextColor> USER_COLORS = new HashMap<>();
    private static final String filePath = "config/team_objects.txt";

    public static void main(String[] args) {
        // Starte die Dateiüberwachung
        startFileWatcher();
    }

    public static void startFileWatcher() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(filePath).getParent();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("Überwache Änderungen an der Datei: " + filePath);

            // Endloser Schleife, die auf Änderungen wartet
            while (true) {
                WatchKey key;
                try {
                    key = watchService.take(); // Blockiert, bis eine Änderung festgestellt wird
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path changed = (Path) event.context();
                        if (changed.endsWith(filePath)) {
                            System.out.println("Änderungen erkannt, lade erneut...");
                            loadUserColors(filePath); // Datei erneut laden
                        }
                    }
                }

                // Schlüssel zurücksetzen, um weitere Events zu empfangen
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            System.out.println("Spielerfarben wurden neu geladen:");
            for (Map.Entry<String, TextColor> entry : USER_COLORS.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

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

    @Override
    public void onInitializeClient() {

    }
}*/