package net.bergbauer.better_pvp.gui.Screens;

import net.bergbauer.better_pvp.gui.BetterPvP_MenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Settings_Screen extends Screen {

    private final List<Tab> tabs = new ArrayList<>();
    private Tab activeTab;
    private ButtonWidget backButton;
    private static final LinkedHashMap<String, Boolean> settingsState = new LinkedHashMap<>();
    private static final String CONFIG_PATH = "config/better_pvp_settings.txt";

    public Settings_Screen() {
        super(Text.of("Settings Screen"));
        initializeTabs();
    }

    public static boolean isSettingEnabled(String settingName) {
        if (settingsState.firstEntry() == null) {
            loadSettings();
        }
        return settingsState.get(settingName);
    }

    @Override
    protected void init() {
        initializeBackButton();
        loadSettings();

        if (activeTab == null && !tabs.isEmpty()) {
            setActiveTab(tabs.getFirst()); // Erste Tab aktiv setzen
        }
    }

    private void initializeBackButton() {
        backButton = ButtonWidget.builder(Text.literal("Back"), button -> {
                    saveSettings();
                    MinecraftClient.getInstance().setScreen(new BetterPvP_MenuScreen());
                }).dimensions(width - 60, 20, 40, 20)
                .tooltip(Tooltip.of(Text.literal("You come back to the general page")))
                .build();

        addDrawableChild(backButton);
    }

    @Override
    public void close() {
        saveSettings();
        super.close();
    }

    private void initializeTabs() {
        Tab teamManagerTab = new Tab("Team Manager");
        teamManagerTab.addSetting("Teams activated", true);
        teamManagerTab.addSetting("Show teams in chat", true);
        teamManagerTab.addSetting("Paint armor in team color", true);
        teamManagerTab.addSetting("Paint player in team color", false);
        tabs.add(teamManagerTab);
    }

    private String boolToString(Boolean value) {
        return value ? "✔" : "✖";
    }

    private void setActiveTab(Tab tab) {
        activeTab = tab;
        addDrawableChild(backButton);

        int yPosition = 120;
        int textXPosition = 10; // Position des Textes (Einstellung)
        int checkboxXPosition = 150; // Position der Checkbox, weiter rechts

        for (String setting : tab.getSettings().keySet()) {
            boolean currentState = settingsState.getOrDefault(setting, tab.getSettings().get(setting));

            // TextWidget für die Anzeige des Textes (der Einstellung)
            TextWidget settingTextWidget = new TextWidget(textXPosition, yPosition, textRenderer.getWidth(setting), 20, Text.literal(setting), textRenderer);
            addDrawableChild(settingTextWidget);
            // Checkbox
            ButtonWidget checkbox = ButtonWidget.builder(Text.literal(boolToString(currentState)), button -> {
                        // Checkbox toggle logic
                        boolean newState = !settingsState.getOrDefault(setting, tab.getSettings().get(setting));
                        settingsState.put(setting, newState);

                        // Update the button text
                        button.setMessage(Text.literal(boolToString(newState)));
                    })
                    .dimensions(checkboxXPosition, yPosition, 20, 20) // Checkbox weiter rechts
                    .tooltip(Tooltip.of(Text.literal("Change state of " + setting)))
                    .build();

            addDrawableChild(checkbox);
            yPosition += 25; // Erhöhe die Y-Position für das nächste Element
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        MatrixStack matrices = context.getMatrices();
        float titleScale = 2.0f;

        // Zeichne Titel
        matrices.push();
        matrices.scale(titleScale, titleScale, titleScale);
        int scaledWidth = (int) ((float) width / 2 / titleScale);
        int scaledY = (int) (20 / titleScale);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Settings"), scaledWidth, scaledY, 11141290);
        matrices.pop();

        // Zeichne Tabs
        int yPosition = 40;
        float tabScale = 2.0f;
        matrices.push();
        matrices.scale(tabScale, tabScale, tabScale);
        for (Tab tab : tabs) {
            context.drawText(textRenderer, Text.literal(tab.getName()), 10, yPosition, 11141120, true);
            yPosition += 25;
        }
        matrices.pop();
    }

    private void saveSettings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_PATH))) {
            for (String setting : settingsState.keySet()) {
                writer.write(setting + "=" + settingsState.get(setting));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    private static void loadSettings() {
        File file = new File(CONFIG_PATH);
        if (!file.exists()) {
            System.out.println("Settings file not found, loading defaults.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    settingsState.put(parts[0], Boolean.parseBoolean(parts[1].trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
        }
    }

    // Tab Klasse
    private static class Tab {
        private final String name;
        private final LinkedHashMap<String, Boolean> settings = new LinkedHashMap<>();

        public Tab(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSetting(String setting, Boolean defaultValue) {
            settings.put(setting, defaultValue);
        }

        public LinkedHashMap<String, Boolean> getSettings() {
            return settings;
        }
    }
}
