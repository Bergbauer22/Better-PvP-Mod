package net.bergbauer.better_pvp.mixin.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.bergbauer.better_pvp.util.PublicStaticFields;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.bergbauer.better_pvp.BetterPvP.MY_LOGGER;
import static net.bergbauer.better_pvp.PlayerColorLoader.ColorIndexOfPlayer;
import static net.bergbauer.better_pvp.PlayerColorLoader.USER_COLORS;


@Mixin(EquipmentRenderer.class)
public class EquipmentRendererMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer modifyRenderLayer(Identifier texture, Operation<RenderLayer> original) {
        if(!Settings_Screen.isSettingEnabled("Teams activated") || !Settings_Screen.isSettingEnabled("Paint armor in team color")){ original.call(texture);}

        var ctx = PublicStaticFields.currentArmorContext;
        String entityName = ctx.getEntity().getName().getString();
        //MY_LOGGER.info("Name: " + entityName + "  Bool: " + String.valueOf(USER_COLORS.containsKey(entityName)));
        if (ctx.shouldModify() && USER_COLORS.containsKey(entityName)) {
            MY_LOGGER.info("Text: {}  RL: {}", texture, original);
            int color = ColorIndexOfPlayer(entityName);
            Identifier overlay = switch (texture.toString()){
                case "minecraft:textures/entity/equipment/humanoid/leather.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/leather_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/leather.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/leather_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/chainmail.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/chain/chain_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/chainmail.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/chain/chain_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/gold.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/gold/gold_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/gold.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/gold/gold_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/iron.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/iron/iron_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/iron.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/iron/iron_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/diamond.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/diamond/diamand_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/diamond.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/diamond/diamand_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/netherite.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/netheride/netheride_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/netherite.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/netheride/netheride_armor_layer_2_color" + color + ".png");
                default -> texture;
            };
            return RenderLayer.getArmorCutoutNoCull(overlay);
        }
        return original.call(texture);
    }

    /*@WrapOperation(
            method = "render(Lnet/minecraft/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/model/Model.render (Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
            )
    )
    private void modifyColor(Model instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, Operation<Void> original) {
        var ctx = PublicStaticFields.currentArmorContext;
        var percentage = 1F;
        MY_LOGGER.info("Color:" + color + "  Original: " + original + "  Model: " + instance + "  VertexCon: " + vertices + "  Overlay: " + overlay + "  Matrix: " + matrices);
        original.call(instance, matrices, vertices, light, overlay, ColorHelper.withAlpha(ColorHelper.channelFromFloat(0.7f), color));
    }
    // The Mod Show me your Skin was a real help for fixing Update Problems---Thank you
    */
}
