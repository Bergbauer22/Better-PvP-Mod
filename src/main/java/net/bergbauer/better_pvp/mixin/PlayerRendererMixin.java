package net.bergbauer.better_pvp.mixin;

import net.bergbauer.better_pvp.PlayerColorLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    // Eine einfache Thread-Local-Variable, um Rekursion zu verhindern
    private static final ThreadLocal<Boolean> isRenderingCustomColor = ThreadLocal.withInitial(() -> false);

    public PlayerRendererMixin() {
        super(null, null,1);
    }

    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderCustomColor(AbstractClientPlayerEntity abstractClientPlayerEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, CallbackInfo ci) {
        // Prüfen, ob wir uns bereits im Custom-Render-Prozess befinden (Rekursionsschutz)
        if (isRenderingCustomColor.get()) {
            System.out.println("Abbruch");
            return;
        }
        System.out.println("SpielerSuche");
        // Holt den Spielernamen
        String playerName = abstractClientPlayerEntity.getName().getString();

        // Prüfe, ob der Spieler in USER_COLORS enthalten ist
        Map<String, TextColor> userColors = PlayerColorLoader.USER_COLORS;
        if (userColors.containsKey(playerName)) {

        }
    }
}


/*
// Hole die Farbe für den Spieler
            TextColor color = userColors.get(playerName);
            System.out.println("Umcolorierung bei " + playerName);
            // Konvertiere die TextColor in RGB-Werte
            int rgb = color.getRgb();
            float r = ((rgb >> 16) & 0xFF) / 255.0f;
            float g = ((rgb >> 8) & 0xFF) / 255.0f;
            float b = (rgb & 0xFF) / 255.0f;

            // Setze den Rekursionsschutz
            isRenderingCustomColor.set(true);

            // Rendern mit Farbe (Hier sollte die eigentliche Farbanpassung erfolgen)

            // Optional: Hier Shader oder Texturen für die Farbänderung anwenden
            this.render(abstractClientPlayerEntity, entityYaw, partialTicks, matrixStack, buffer, light);
            matrixStack.push();
            matrixStack.pop();

            // Render stoppen, da wir das Custom-Rendering abgeschlossen haben
            ci.cancel();

            // Rekursionsschutz zurücksetzen
            isRenderingCustomColor.set(false);
 */