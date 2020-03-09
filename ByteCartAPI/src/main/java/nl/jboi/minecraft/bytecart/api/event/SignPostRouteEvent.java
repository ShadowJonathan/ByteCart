package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.sign.BCRouter;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import org.bukkit.event.HandlerList;


/**
 * Event triggered when a vehicle is leaving a router,
 * after the collision avoidance layer operations.
 * <p>
 * The target track is definitive.
 */
public class SignPostRouteEvent extends BCRoutableSignEvent {

    private static final HandlerList handlers = new HandlerList();

    private final BCRouter router;

    protected int to;

    /**
     * Default constructor
     *
     * @param router The router involved
     * @param to     The ring number of the track where the vehicle is currently (not the destination ring)
     */
    public SignPostRouteEvent(BCRouter router, int to) {
        super(router);
        this.router = router;
        this.to = to;
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
     * Get the track from where the vehicle entered the router
     *
     * @return The number of the ring
     */
    public final int getOriginTrack() {
        return router.getOriginTrack();
    }

    /**
     * Get the track to where the vehicle leaves the router
     *
     * @return The number of the ring
     */
    public final int getTargetTrack() {
        return to;
    }

    protected final BCSign getSign() {
        return router;
    }
}
