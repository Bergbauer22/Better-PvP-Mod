package net.bergbauer.better_pvp.mixin.entity;

import net.bergbauer.better_pvp.util.EntityContext;
import net.bergbauer.better_pvp.util.PublicStaticFields;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V",
            at = @At("HEAD")
    )
    private <E extends net. minecraft. entity. Entity, S extends net. minecraft. client. render. entity. state. EntityRenderState> void captureEntityContext(E entity, double x, double y, double z, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityRenderer<? super E, S> renderer, CallbackInfo ci ){
        EntityContext.setCurrentEntity(entity);
        PublicStaticFields.currentEntity = entity;
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V",
            at = @At("RETURN")
    )
    private <E extends net. minecraft. entity. Entity, S extends net. minecraft. client. render. entity. state. EntityRenderState> void clearEntityContext(E entity, double x, double y, double z, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityRenderer<? super E, S> renderer, CallbackInfo ci ){
        EntityContext.clearCurrentEntity();
        PublicStaticFields.currentEntity = null;
    }
}
