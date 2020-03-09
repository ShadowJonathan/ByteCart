package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.sign.Triggable;

/**
 * A state machine depending of 2 elements
 */
interface CollisionAvoider {
    /**
     * Get the value stored as second pos
     *
     * @return the value of the second position
     */
	int getSecondpos();

    /**
     * Add the second triggered IC to current CollisonAvoider
     *
     * @param t
     */
	void Add(Triggable t);

}
