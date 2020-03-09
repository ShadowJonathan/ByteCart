package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.event.HandlerList;


/**
 * Event triggered when a vehicle is leaving a station sign,
 * going into the station or not.
 * <p>
 * This event is triggered after the "busy line" check, so the
 * direction is definitive.
 */
public class SignPostStationEvent extends SignPostSubnetEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Default constructor
     * <p>
     * The side parameter may be:
     * - LEFT: the vehicle will not enter the station
     * - RIGHT: the vehicle enters the station
     *
     * @param subnet The BC9XXX sign involved
     * @param side   The direction taken by the cart
     */
    public SignPostStationEvent(Subnet subnet, Side side) {
        super(subnet, side);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
