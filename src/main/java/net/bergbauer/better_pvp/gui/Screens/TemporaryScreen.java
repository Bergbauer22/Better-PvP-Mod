package net.bergbauer.better_pvp.gui.Screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TemporaryScreen extends Screen {
    public int Cooldown = 0;
    public Screen ScreenToSwitch;
    public TemporaryScreen(Screen ScreenToSwitch_) {
        super(Text.of("Temporary Screen"));
        ScreenToSwitch = ScreenToSwitch_;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        // Render wird überschrieben, aber nichts gezeichnet

    }

    @Override
    public void tick() {
        // Sobald der Screen "getickt" hat, sofort schließen
        if(Cooldown > 3){
            assert this.client != null;
            this.client.setScreen(ScreenToSwitch);
        }
    }
}