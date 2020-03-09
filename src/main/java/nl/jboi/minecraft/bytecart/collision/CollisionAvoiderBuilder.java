package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.sign.Triggable;
import org.bukkit.Location;

/**
 * A builder for a collision manager
 */
public interface CollisionAvoiderBuilder {
    /**
     * Get an instance of the collision manager
     *
     * @return an instance of collision manager
     */
	<T extends CollisionAvoider> T getCollisionAvoider();

    /**
     * Get the location to where the collision managers built will be attached
     *
     * @return the location
     */
	Location getLocation();

    /**
     * Get the IC attached to the collision managers built
     *
     * @return the IC
     */
	Triggable getIc();

}
