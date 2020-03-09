package nl.jboi.minecraft.bytecart.api.hal;

/**
 * A read-only registry
 */
public interface Registry {
    /**
     * @return The value stored in this registry.
     */
    int getAmount();

    /**
     * @return The length of this registry in bits.
     */
    int length();
}
