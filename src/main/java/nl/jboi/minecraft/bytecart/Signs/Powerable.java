package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.api.HAL.IC;

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
