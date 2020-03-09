package nl.jboi.minecraft.bytecart.IO;

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
