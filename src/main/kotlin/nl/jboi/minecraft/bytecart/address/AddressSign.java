package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.hal.RegistryBoth;
import nl.jboi.minecraft.bytecart.io.AbstractComponent;
import nl.jboi.minecraft.bytecart.io.ComponentSign;
import org.bukkit.block.Block;

/**
 * Implements an address using a line of a sign as support
 */
final class AddressSign extends AbstractComponent implements Address {

    /**
     * String used as internal storage
     */
    private final AddressString Address;

    /**
     * Creates the address
     *
     * @param block the sign block containing the address
     * @param ligne the line number containing the address
     */
    AddressSign(Block block, int ligne) {

        super(block);

        this.Address = new AddressString((new ComponentSign(block)).getLine(ligne), false);
		
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating AddressSign line #" + ligne + " at " + block.getLocation().toString());
	*/
    }

    @Override
    public final RegistryBoth getRegion() {
        return Address.getRegion();
    }

    @Override
    public final RegistryBoth getTrack() {
        return Address.getTrack();
    }

    @Override
    public final RegistryBoth getStation() {
        return Address.getStation();
    }

    @Override
    public final boolean setAddress(String s) {
        this.Address.setAddress(s);
        return true;
    }

    @Override
    public boolean setAddress(String s, String name) {
        (new ComponentSign(this.getBlock())).setLine(2, name);
        return setAddress(s);
    }

    @Override
    public boolean isTrain() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setTrain(boolean istrain) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void finalizeAddress() {
        (new ComponentSign(this.getBlock())).setLine(3, this.Address.toString());
    }

    @Override
    public boolean isValid() {
        return this.Address.isValid;
    }

    @Override
    public void remove() {
        this.Address.remove();
        (new ComponentSign(this.getBlock())).setLine(3, "");
    }

    @Override
    public final String toString() {
        return Address.toString();
    }

    @Override
    public boolean isReturnable() {
        return false;
    }
}
