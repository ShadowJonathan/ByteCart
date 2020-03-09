package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a vehicle is entering a station sign.
 * <p>
 * This event is triggered before the "busy line" check, so the
 * direction may change.
 */
public class SignPreStationEvent extends SignPreSubnetEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Default constructor
     * <p>
     * The side parameter may be:
     * - LEFT: the vehicle wish not to enter the station
     * - RIGHT: the vehicle wish to enter the station
     *
     * @param subnet The BC9XXX sign involved
     * @param side   The direction taken by the cart
     */
    public SignPreStationEvent(Subnet subnet, Side side) {
        super(subnet, side);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
