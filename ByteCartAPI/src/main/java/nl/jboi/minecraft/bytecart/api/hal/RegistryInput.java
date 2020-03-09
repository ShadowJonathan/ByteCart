package nl.jboi.minecraft.bytecart.api.hal;

/**
 * A registry that can be used as input and read
 */
public interface RegistryInput extends Registry {
    /**
     * Get the value of the bit at position index
     *
     * @param index the position, 0 for most significant bit
     * @return true if the bit is set to 1, false otherwise
     */
    boolean getBit(int index);
}
