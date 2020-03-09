package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.hal.IC;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a sign is physically removed from the world
 */
public final class SignRemoveEvent extends BCEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;

    public SignRemoveEvent(IC ic, Entity entity) {
        super(ic);
        this.entity = entity;
    }

    /**
     * Needed for Bukkit Event API usage
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return the entity that broke the block
     */
    public Entity getEntity() {
        return entity;
    }
}
