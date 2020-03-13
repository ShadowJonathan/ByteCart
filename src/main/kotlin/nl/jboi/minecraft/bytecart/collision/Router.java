package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.api.hal.RegistryOutput;
import org.bukkit.block.BlockFace;

/**
 * A router representation in the collision management layer
 */
public interface Router extends CollisionAvoider {
    /**
     * Ask for a direction, requesting a possible transition
     *
     * @param from    the direction from where comes the cart
     * @param to      the direction where the cart goes to
     * @param isTrain true if it is a train
     * @return the direction actually taken
     */
    BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain);

    /**
     * Book the router, i.e mark it as currently in use
     *
     * @param b true if this is a train
     */
    void Book(boolean b);

    @Override
    int getSecondpos();

    /**
     * Set the value of the position of the 8 exterior levers, starting from the origin axis.
     *
     * <p>a bit to 1 means the lever is on, a bit to 0 means the lever is off</p>
     *
     * @param secondpos the value to set
     */
    void setSecondpos(int secondpos);

    /**
     * Get the mask of the current usage of the router, starting from the origin axis.
     *
     * <p>a bit to 1 means the lever is blocked, a bit to 0 means the lever is not blocked</p>
     *
     * @return the mask
     */
    int getPosmask();

    /**
     * Set the mask of the current usage of the router, starting from the origin axis.
     *
     * <p>a bit to 1 means the lever is blocked, a bit to 0 means the lever is not blocked</p>
     *
     * @param posmask the mask to set
     */
    void setPosmask(int posmask);

    /**
     * Get the direction from where the cart is coming.
     *
     * <p>This direction is also the origin axis of the registries</p>
     *
     * @return the direction
     */
    BlockFace getFrom();

    /**
     * Activate levers according to registry
     *
     * @param from the origin direction
     */
    void route(BlockFace from);

    /**
     * Get the lever registry
     *
     * @param i 0 for main levers, 1 for secondary levers
     * @return the value of the registry
     */
    RegistryOutput getOutput(int i);

    /**
     * Get the direction where the cart eventually go
     *
     * @return the direction
     */
    BlockFace getTo();
}
