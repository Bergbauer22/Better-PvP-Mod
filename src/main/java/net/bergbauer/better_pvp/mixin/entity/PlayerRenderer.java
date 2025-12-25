package net.bergbauer.better_pvp.mixin.entity;

import net.bergbauer.better_pvp.PlayerColorLoader;
import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.bergbauer.better_pvp.util.EntityContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
    Dieser Code ist dafür verantwortlich, dass Skins durch Farben ersetzt werden
 */

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRenderer {
    @Inject(
            method = "getTexture(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getCustomSkin(PlayerEntityRenderState playerEntityRenderState, CallbackInfoReturnable<Identifier> cir) {
        Entity entity = EntityContext.getCurrentEntity();

        // 🔥 WICHTIG: GUI / Preview / Cache → KEINE Entity
        if (entity == null) {
            return;
        }

        // Nur echte Client-Spieler
        if (!(entity instanceof AbstractClientPlayerEntity player)) {
            return;
        }

        // Settings deaktiviert → Vanilla-Skin
        if (!Settings_Screen.isSettingEnabled("Teams activated")
                || !Settings_Screen.isSettingEnabled("Paint player in team color")) {

            cir.setReturnValue(player.getSkinTextures().texture());
            return;
        }

        // Custom Skin
        Identifier customSkin = PlayerColorLoader.getCustomSkin(player);
        if (customSkin != null) {
            cir.setReturnValue(customSkin);
        }
    }
}