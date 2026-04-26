package net.bergbauer.better_pvp.gui;

import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.joml.Matrix3x2fStack;

@Environment(EnvType.CLIENT)
public class BetterPvP_MenuScreen extends Screen {

    // Positions- und Größenkonstanten
    private static final int EXIT_BUTTON_WIDTH = 40;
    private static final int EXIT_BUTTON_HEIGHT = 20;
    private static final int EXIT_BUTTON_X_OFFSET = 60;
    private static final int EXIT_BUTTON_Y = 20;

    private static final int TEAM_BUTTON_WIDTH = 150;
    private static final int TEAM_BUTTON_HEIGHT = 20;
    private static final int TEAM_BUTTON_Y = 160;

    private static final int SETTINGS_BUTTON_WIDTH = 150;
    private static final int SETTINGS_BUTTON_HEIGHT = 20;
    private static final int SETTINGS_BUTTON_Y = 190;

    private static final float TITLE_SCALE = 3.0f;
    private static final float CATEGORY_SCALE = 2.0f;

    public BetterPvP_MenuScreen() {
        super(Text.literal("BetterPvP"));
    }

    @Override
    protected void init() {
        super.init();

        // Exit Button
        // Widgets
        ButtonWidget exitButton = ButtonWidget.builder(Text.literal("Exit"), button -> this.close())
                .dimensions(width - EXIT_BUTTON_X_OFFSET, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("You close the current menu")))
                .build();
        addDrawableChild(exitButton);

        // Unsichtbare Buttons für TeamManager und Settings
        ButtonWidget teamButton = ButtonWidget.builder(Text.literal(""), button ->
                        MinecraftClient.getInstance().setScreen(new TeamManager_Screen())
                ).dimensions(width / 2 - TEAM_BUTTON_WIDTH / 2, TEAM_BUTTON_Y, TEAM_BUTTON_WIDTH, TEAM_BUTTON_HEIGHT)
                .build();
        addSelectableChild(teamButton);

        ButtonWidget settingsButton = ButtonWidget.builder(Text.literal(""), button ->
                        MinecraftClient.getInstance().setScreen(new Settings_Screen())
                ).dimensions(width / 2 - SETTINGS_BUTTON_WIDTH / 2, SETTINGS_BUTTON_Y, SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT)
                .build();
        addSelectableChild(settingsButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        Matrix3x2fStack matrices = context.getMatrices();

        // Headline
        matrices.pushMatrix();
        matrices.scale(TITLE_SCALE, TITLE_SCALE);
        int scaledWidth = (int) ((float) width / 2 / TITLE_SCALE);
        int scaledY = (int) (20 / TITLE_SCALE);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("BetterPvP"), scaledWidth, scaledY, 0xFFFF0000);
        matrices.popMatrix();

        // TeamManager Text
        matrices.pushMatrix();
        matrices.scale(CATEGORY_SCALE, CATEGORY_SCALE);
        scaledWidth = (int) ((float) width / 2 / CATEGORY_SCALE);
        scaledY = (int) (TEAM_BUTTON_Y / CATEGORY_SCALE);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("TeamManager"), scaledWidth, scaledY, 0xFFFFFFFF);
        matrices.popMatrix();

        // Settings Text
        matrices.pushMatrix();
        matrices.scale(CATEGORY_SCALE, CATEGORY_SCALE);
        scaledWidth = (int) ((float) width / 2 / CATEGORY_SCALE);
        scaledY = (int) (SETTINGS_BUTTON_Y / CATEGORY_SCALE);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Settings"), scaledWidth, scaledY, 0xFFFFFFFF);
        matrices.popMatrix();
    }


    @Override
    public void close() {
        super.close();
    }
}
