package net.bergbauer.better_pvp.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public record ArmorContext(LivingEntity entity) {

    public boolean shouldModify() {
        return entity() instanceof PlayerEntity;
    }

}
