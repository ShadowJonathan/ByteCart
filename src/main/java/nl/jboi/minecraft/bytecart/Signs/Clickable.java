package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.api.HAL.IC;

/**
 * An IC that a player can right-click should implement this
 */
public interface Clickable extends IC {
    /**
     * Method called when a player right-clicks the IC
     */
	void click();
}
