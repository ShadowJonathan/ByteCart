package nl.jboi.minecraft.bytecart.api.hal;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Represents an IC, i.e an component with input and output
 */
public interface IC {
    /**
     * Return the name of the permission needed to create or destroy this IC
     *
     * @return the permission
     */
    String getBuildPermission();

    /**
     * Return the official name of this IC.
     *
     * @return the name
     */
    String getName();

    /**
     * Return the friendly name of this IC
     *
     * @return the friendly name
     */
    String getFriendlyName();

    /**
     * Return the tax assigned to this IC when used
     *
     * @return the tax
     */
    int getTriggertax();


    /**
     * Return the tax assigned to this IC when built
     *
     * @return the tax
     */
    int getBuildtax();

    /**
     * Get the block implementing this IC, usually the sign
     *
     * @return the block
     */
    Block getBlock();

    /**
     * Get the location of the IC, usually the location of the sign
     *
     * @return the location
     */
    Location getLocation();

    /**
     * Get the orientation of the sign
     *
     * @return the same direction as the player is facing when looking at the sign
     */
    BlockFace getCardinal();

    /**
     * Register an input for this IC
     *
     * @param reg the input to register
     */
    void addInputRegistry(RegistryInput reg);

    /**
     * Register an output for this IC
     *
     * @param reg the output to register
     */
    void addOutputRegistry(RegistryOutput reg);

    /**
     * Get an input of this IC
     *
     * @param index the index of the input to get
     * @return the input
     */
    RegistryInput getInput(int index);

    /**
     * Get an output of this IC
     *
     * @param index the index of the output to get
     * @return the output
     */
    RegistryOutput getOutput(int index);
}

