package nl.jboi.minecraft.bytecart.api.hal;

import java.io.Serializable;

/**
 * A registry with an internal implementation
 */
public class VirtualRegistry implements RegistryBoth, Serializable {

    // bit index 0 est le bit de poids fort

    private static final long serialVersionUID = -7296392835005177288L;
    private final int Length;
    private int Virtual = 0;

    /**
     * @param length the length of the registry to create
     */
    public VirtualRegistry(int length) {
        this.Length = length;

		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : creating VirtualRegistry of " + this.length() + " bit(s).");
*/
    }

    @Override
    public void setBit(int index, boolean value) {

        if (value)
            this.Virtual |= 1 << (length() - index - 1);
        else
            this.Virtual &= ~(1 << (length() - index - 1));
    }

    @Override
    public boolean getBit(int index) {
        return ((this.Virtual >> (length() - index - 1)) & 1) != 0;
    }

    @Override
    public int length() {
        return this.Length;
    }

    @Override
    public int getAmount() {
        return Virtual;
    }

    @Override
    public void setAmount(int amount) {
        this.Virtual = amount % (1 << this.length());
    }

}
