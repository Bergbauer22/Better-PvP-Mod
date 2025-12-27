package net.bergbauer.better_pvp.mixin.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bergbauer.better_pvp.gui.Screens.Settings_Screen;
import net.bergbauer.better_pvp.util.IdManager;
import net.bergbauer.better_pvp.util.PublicStaticFields;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.bergbauer.better_pvp.PlayerColorLoader.ColorIndexOfPlayer;
import static net.bergbauer.better_pvp.PlayerColorLoader.USER_COLORS;


@Mixin(EquipmentRenderer.class)
public class EquipmentRendererMixin {
    @WrapOperation(
            method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer modifyRenderLayer(Identifier texture, Operation<RenderLayer> original) {
        if(!Settings_Screen.isSettingEnabled("Teams activated") || !Settings_Screen.isSettingEnabled("Paint armor in team color"))
        {
            return original.call(texture);
        }

        EntityRenderState renderState = PublicStaticFields.currentEntityRenderState;

        if(!(renderState instanceof PlayerEntityRenderState playerEntityRenderState)){
            return original.call(texture);
        }

        String playerName = IdManager.getNameFromId(playerEntityRenderState.id);

        if (USER_COLORS.containsKey(playerName)) {
            //MY_LOGGER.info("Text: {} ", texture);
            int color = ColorIndexOfPlayer(playerName);
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
                case "minecraft:textures/entity/equipment/humanoid/leather_overlay.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/overlay/leather_armor_overlay_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/leather_overlay.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/leather/overlay/leather_armor_overlay_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/copper.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/copper/copper_armor_layer_1_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid_leggings/copper.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/copper/copper_armor_layer_2_color" + color + ".png");
                case "minecraft:textures/entity/equipment/humanoid/turtle_scute.png" ->
                        Identifier.of("better_pvp", "textures/entity/player/colored_armor/turtle/turtle_armor_layer_1_color" + color + ".png");
                default -> texture;
            };
            return RenderLayer.getArmorCutoutNoCull(overlay);
        }
        return original.call(texture);
    }
}