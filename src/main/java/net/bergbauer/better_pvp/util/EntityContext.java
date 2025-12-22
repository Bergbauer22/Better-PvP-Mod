package net.bergbauer.better_pvp.util;

import net.minecraft.entity.Entity;

public class EntityContext {
    private static final ThreadLocal<Entity> CURRENT_ENTITY = new ThreadLocal<>();

    public static void setCurrentEntity(Entity entity){
        CURRENT_ENTITY.set(entity);
    }

    public static Entity getCurrentEntity(){
        return CURRENT_ENTITY.get();
    }

    public static void clearCurrentEntity(){
        CURRENT_ENTITY.remove();
    }
}
