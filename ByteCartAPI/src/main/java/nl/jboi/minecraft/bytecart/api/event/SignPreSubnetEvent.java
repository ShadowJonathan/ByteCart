package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a vehicle is using a subnet sign,
 * before the collision avoidance layer operations.
 * <p>
 * The direction may be modified by collision avoidance layer.
 */
public class SignPreSubnetEvent extends SignPostSubnetEvent {

    private static final HandlerList handlers = new HandlerList();


    /**
     * Default constructor
     * <p>
     * The side parameter may be:
     * - LEFT: the vehicle wish not to enter the subnet
     * - RIGHT: the vehicle wish to enter the subnet OR wish to leave the subnet if it was inside
     *
     * @param subnet The BC9XXX sign involved
     * @param side   The direction wished of the vehicle
     */
    public SignPreSubnetEvent(Subnet subnet, Side side) {
        super(subnet, side);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    protected BCSign getSign() {
        return subnet;
    }

    /**
     * Change the direction taken by the vehicle on the fly
     * This will modify internal state of the sign before actual operations.
     * This will not change the destination address recorded in the vehicle.
     * <p>
     * The final direction is undefined until routing layer operations occur.
     *
     * @param side A value from IntersectionSide.Side enum
     */
    public void setSide(Side side) {
        this.side = side;
    }
}
