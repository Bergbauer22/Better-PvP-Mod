package net.bergbauer.better_pvp.gui;

import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
@Environment(EnvType.CLIENT)
public class BetterPvP_MenuScreen extends Screen {
    public BetterPvP_MenuScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("BetterPvP"));
    }

    public ButtonWidget exitButton;

    @Override
    public void close(){
        // Öffne den temporären Screen, der sich sofort wieder schließt
        super.close();
    }
    @Override
    protected void init() {
        exitButton = ButtonWidget.builder(Text.literal("Exit"), button -> {
                    System.out.println("You clicked button1!");
                    this.close();

                })
                .dimensions(width - 60, 20, 40, 20)
                .tooltip(Tooltip.of(Text.literal("Plottwist: You close the Menu")))
                .build();
        addDrawableChild(exitButton);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //Headline
        super.render(context, mouseX, mouseY, delta);
        float scaleTitle = 3.0f;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scaleTitle, scaleTitle, scaleTitle);
        int scaledWidth = (int) ((float) width / 2 / scaleTitle);
        int scaledY = (int) (20 / scaleTitle);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("BetterPvP"), scaledWidth, scaledY, 11141120);
        matrices.pop();
        //TeamCategory-Settings
        float scaleTeamCategory = 2f;
        int YTeamCategoryPosition = 160;
        //TeamManagerButton
        matrices.push();
        matrices.scale(scaleTeamCategory, scaleTeamCategory, scaleTeamCategory);
        scaledWidth = (int) ((float) width / 2 / scaleTeamCategory);
        scaledY = (int) (YTeamCategoryPosition / scaleTeamCategory);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("TeamManager"), scaledWidth, scaledY, 0xFFFFFF);
        matrices.pop();

        //SettingsCategory-Settings
        float scaleSettingCategory = 2f;
        int YSettingCategoryPosition = 190;
        //TeamManagerButton
        matrices.push();
        matrices.scale(scaleSettingCategory, scaleSettingCategory, scaleSettingCategory);
        scaledWidth = (int) ((float) width / 2 / scaleSettingCategory);
        scaledY = (int) (YSettingCategoryPosition / scaleSettingCategory);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Settings"), scaledWidth, scaledY, 0xFFFFFF);
        matrices.pop();
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Überprüfe, ob die linke Maustaste gedrückt wurde
        if (button == 0) {
            int TM_buttonY = 160;

            if (mouseX >= (double) width / 2 - 75 && mouseX <= (double) width / 2 + 75 && mouseY >= TM_buttonY && mouseY <= TM_buttonY + 20) {
                MinecraftClient.getInstance().setScreen(new TeamManager_Screen());
                return true;
            }
            int S_buttonY = 190;
            if (mouseX >= (double) width / 2 - 75 && mouseX <= (double) width / 2 + 75 && mouseY >= S_buttonY && mouseY <= S_buttonY + 20) {
                MinecraftClient.getInstance().setScreen(new Settings_Screen());
                return true;
            }
        }
        // Standardverhalten, wenn kein Button gedrückt wurde
        return super.mouseClicked(mouseX, mouseY, button);
    }
}