package nl.jboi.minecraft.bytecart.io;

/**
 * Represents a writable component, storing a single bit
 */
public interface OutputPin {
    /**
     * write the bit
     *
     * @param bit true to write 1, false to write 0
     */
	void write(boolean bit);

}
