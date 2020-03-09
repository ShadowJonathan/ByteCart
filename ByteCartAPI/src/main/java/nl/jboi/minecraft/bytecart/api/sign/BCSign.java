package nl.jboi.minecraft.bytecart.api.sign;

import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.hal.IC;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Level;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A network sign should implement this
 */
public interface BCSign extends IC {
    /**
     * Get the hierarchical level of the IC
     *
     * @return the level
     */
    Level getLevel();

    /**
     * Get the vehicle that uses this IC
     *
     * @return the vehicle
     */
    Vehicle getVehicle();

    /**
     * Get the address stored in the IC
     *
     * @return the address
     */
    Address getSignAddress();

    /**
     * Get the address stored in the ticket
     *
     * @return the address
     */
    String getDestinationIP();

    /**
     * Get the center of the IC.
     *
     * @return the center
     */
    Block getCenter();
}
