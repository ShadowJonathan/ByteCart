package nl.jboi.minecraft.bytecart.io;

/**
 * Represents a readable component, giving 1 bit
 */
public interface InputPin {
    /**
     * Read the bit
     *
     * @return true if 1, false otherwise
     */
	boolean read();
}
