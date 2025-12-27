package net.bergbauer.better_pvp.mixin.entity;

import net.bergbauer.better_pvp.util.PublicStaticFields;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderManager.class)
public class EntityRenderManagerMixin {
    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    public <S extends EntityRenderState> void captureRenderStateContext(S renderState, CameraRenderState cameraRenderState, double d, double e, double f, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CallbackInfo ci) {
        PublicStaticFields.currentEntityRenderState = renderState;

    }

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    public <S extends EntityRenderState> void clearRenderStateContext(S renderState, CameraRenderState cameraRenderState, double d, double e, double f, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CallbackInfo ci) {    // EntityContext.clearCurrentEntity();
        PublicStaticFields.currentEntityRenderState = null;
    }
}