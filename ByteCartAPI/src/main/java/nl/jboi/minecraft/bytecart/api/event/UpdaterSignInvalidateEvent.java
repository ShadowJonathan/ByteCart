package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a sign is invalidated by an updater
 */
public final class UpdaterSignInvalidateEvent extends UpdaterEvent {

    private static final HandlerList handlers = new HandlerList();

    public UpdaterSignInvalidateEvent(Wanderer updater) {
        super(updater);
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
}