package net.bergbauer.better_pvp.mixin;


import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.bergbauer.better_pvp.util.ModRenderLayers;

import net.minecraft.client.render.*;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.bergbauer.better_pvp.PlayerColorLoader.ColorIndexOfPlayer;
import static net.bergbauer.better_pvp.PlayerColorLoader.USER_COLORS;


@Mixin(value = ArmorFeatureRenderer.class)
public class ArmorTextureRenderer<T extends LivingEntity, A extends BipedEntityModel<T>> {
    @Unique
    T currentEntity;
    @Inject(
            method = "renderArmorParts",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, A model, int i, Identifier overlay, CallbackInfo ci) {
        if(!Settings_Screen.isSettingEnabled("Teams activated") || !Settings_Screen.isSettingEnabled("Paint armor in team color")){ return;}
        String playerName = currentEntity.getName().getString();
        if (USER_COLORS.containsKey(playerName)) {
            int color = ColorIndexOfPlayer(playerName);

            switch (overlay.toString()) {
                case "minecraft:textures/models/armor/leather_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/leather_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/leather_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/leather_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/models/armor/chainmail_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/chain/chain_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/chainmail_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/chain/chain_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/models/armor/gold_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/gold/gold_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/gold_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/gold/gold_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/models/armor/iron_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/iron/iron_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/iron_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/iron/iron_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/models/armor/diamond_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/diamond/diamand_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/diamond_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/diamond/diamand_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/models/armor/netherite_layer_1.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/netheride/netheride_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/models/armor/netherite_layer_2.png" ->
                        overlay = Identifier.of("better_pvp", "textures/entity/player/colored_armor/netheride/netheride_armor_layer_2_color" + color + ".png");
            }

            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(
                    vertexConsumers, ModRenderLayers.ARMOR_TRANSLUCENT_NO_CULL.apply(overlay),
                    true
            );
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, ColorHelper.Argb.fromFloats(1f, 1.0f, 1.0f, 1.0f));
            ci.cancel();
        }
    }
    @Inject(
            method = "renderArmor",
            at = @At(value = "HEAD")
    )
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        currentEntity = entity;
    }
}
