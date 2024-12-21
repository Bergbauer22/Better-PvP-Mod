package net.bergbauer.better_pvp.mixin;

import net.bergbauer.better_pvp.PlayerColorLoader;
import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin{
    @Inject(
            method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getCustomSkin(AbstractClientPlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
        if(!Settings_Screen.isSettingEnabled("Teams activated") || !Settings_Screen.isSettingEnabled("Paint player in team color"))
        {
            cir.setReturnValue(player.getSkinTextures().texture());
        }
        else{
            // Hier wird der Skin ersetzt, falls der Spielername übereinstimmt
            Identifier customSkin = PlayerColorLoader.getCustomSkin(player);
            if (customSkin != null) {
                cir.setReturnValue(customSkin);
            }
        }
    }
}