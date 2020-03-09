package nl.jboi.minecraft.bytecart.api.sign;

import nl.jboi.minecraft.bytecart.api.wanderer.RoutingTable;
import org.bukkit.block.BlockFace;


/**
 * A router
 */
public interface BCRouter extends BCSign {
    /**
     * Get the track from where the cart is coming.
     * <p>
     * For a region router, the returned value is the ring number.
     * <p>
     * For a backbone router, the returned value is the region number.
     *
     * @return the track number
     */
    int getOriginTrack();

    /**
     * Return the direction from where the cart is coming
     *
     * @return the direction
     */
    BlockFace getFrom();

    RoutingTable getRoutingTable();
}
