package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.api.address.Address;


/**
 * Represents an address currently routed
 */
public interface AddressRouted extends Address {

    /**
     * Get the TTL (time-to-live) associated with the address
     *
     * @return the TTL
     */
    int getTTL();

    /**
     * Set the TTL
     * <p>
     * {@link Address#finalizeAddress()} should be called later to actually set the TTL
     *
     * @param i the value to set
     */
    void updateTTL(int i);

    /**
     * Initialize TTL to its default value
     * <p>
     * {@link Address#finalizeAddress()} should be called later to actually initialize the TTL
     */
    void initializeTTL();
}
