package net.bergbauer.better_pvp.mixin.armor;

import net.bergbauer.better_pvp.util.ArmorContext;
import net.bergbauer.better_pvp.util.PublicStaticFields;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArmorFeatureRenderer.class, priority = 500)
public abstract class ArmorFeatureRendererMixin<T extends BipedEntityRenderState, A extends BipedEntityModel<T>> {
    @Inject(
            method = "renderArmor",
            at = @At(value = "HEAD")
    )
    private void setArmorContext(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack,
                                 EquipmentSlot slot, int light, A armorModel, CallbackInfo ci) {
        if (PublicStaticFields.currentEntity instanceof LivingEntity livingEntity) {
            PublicStaticFields.currentArmorContext = new ArmorContext(livingEntity);
        }
    }
}