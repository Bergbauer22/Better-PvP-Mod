package net.bergbauer.better_pvp.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ArmorContext {

    private final LivingEntity entity;

    public ArmorContext(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public boolean shouldModify() {
        return getEntity() instanceof PlayerEntity;
    }

}
