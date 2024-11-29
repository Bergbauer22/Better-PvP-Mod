package net.bergbauer.better_pvp.gui;

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
        //Category-Settings
        float scaleCategory = 2f;
        int YCategoryPosition = 160;
        //TeamManagerButton
        matrices.push();
        matrices.scale(scaleCategory, scaleCategory, scaleCategory);
        scaledWidth = (int) ((float) width / 2 / scaleCategory);
        scaledY = (int) (YCategoryPosition / scaleCategory);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("TeamManager"), scaledWidth, scaledY, 0xFFFFFF);
        matrices.pop();
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Überprüfe, ob die linke Maustaste gedrückt wurde
        if (button == 0) {
            int buttonY = 160;

            if (mouseX >= (double) width / 2 - 75 && mouseX <= (double) width / 2 + 75 && mouseY >= buttonY && mouseY <= buttonY + 20) {
                MinecraftClient.getInstance().setScreen(new TeamManager_Screen());
                return true;
            }
        }
        // Standardverhalten, wenn kein Button gedrückt wurde
        return super.mouseClicked(mouseX, mouseY, button);
    }
}