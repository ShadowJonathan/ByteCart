package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.hal.IC;

import java.io.IOException;

/**
 * An IC that can be powered should implement this
 */
public interface Powerable extends IC {
    /**
     * Method called when the IC is powered
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
	void power();

}
