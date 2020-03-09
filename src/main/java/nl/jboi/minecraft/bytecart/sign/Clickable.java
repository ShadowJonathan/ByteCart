package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.hal.IC;

/**
 * An IC that a player can right-click should implement this
 */
public interface Clickable extends IC {
    /**
     * Method called when a player right-clicks the IC
     */
	void click();
}
