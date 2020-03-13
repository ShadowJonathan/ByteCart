package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.address.Resolver;
import nl.jboi.minecraft.bytecart.api.hal.VirtualRegistry;

/**
 * This class represents a canonical address like xx.xx.xx
 */
public class AddressString extends AbstractAddress implements Address {

    private static final Resolver resolver;

    static {
        resolver = ByteCart.plugin.getResolver();
    }

    /**
     * String used as internal storage
     */
    private String String; // address as displayed

    /**
     * Creates the address
     *
     * @param s the string containing the address or a name to resolve to an address
     */
    public AddressString(String s, boolean resolve) {

        if (isAddress(s)) {
            this.String = s;
            return;
        }

        if (isResolvableName(s)) {
            if (resolve) {
                this.String = resolver.resolve(s);
            } else {
                this.String = s;
            }
            return;
        }

        this.String = null;
        this.isValid = false;
    }

    /**
     * Static method to check the format of an address
     * <p>
     * A resolvable name will return true
     * <p>
     * This method does not check if the address fields are in a valid range
     *
     * @param s the string containing the address to check
     * @return true if the address is in the valid format or a resolvable name
     */
    public static boolean isResolvableAddressOrName(String s) {
        return (s.matches("([0-9]{1,4}\\.){2}[0-9]{1,3}"))
                || isResolvableName(s);
    }

    /**
     * Static method to check the format of an address
     * <p>
     * This method does not check if the address fields are in a valid range
     *
     * @param s the string containing the address to check
     * @return true if the address is in the valid format
     */
    private static boolean isAddress(String s) {
        return s.matches("([0-9]{1,4}\\.){2}[0-9]{1,3}");
    }

    /**
     * Static method to check if a name resolution gives a valid address
     *
     * @param name the string containing the name to check
     * @return true if the resolution gives an address
     */
    private static boolean isResolvableName(String name) {
        if (resolver != null) {
            String a = resolver.resolve(name);
            return a != null && AddressString.isAddress(a);
        }
        return false;
    }

    @Override
    public VirtualRegistry getRegion() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.REGION.getLength())).setAmount(this.getField(0));
        return ret;
    }

    @Override
    public void setRegion(int region) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VirtualRegistry getTrack() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.TRACK.getLength())).setAmount(this.getField(1));
        return ret;
    }

    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VirtualRegistry getStation() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.STATION.getLength())).setAmount(this.getField(2));
        return ret;
    }

    @Override
    public void setStation(int station) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTrain() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a field by index
     *
     * @param index a number between 0 and 2
     * @return the number contained in the field
     */
    private int getField(int index) {
        if (this.String == null)
            throw new IllegalStateException("Address is not valid.");
        String[] st = this.String.split("\\.");
        return Integer.parseInt(st[index].trim());
    }

    @Override
    public void setIsTrain(boolean isTrain) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.lang.String toString() {
        if (this.String != null)
            return this.String;
        return "";
    }

    @Override
    public boolean setAddress(java.lang.String s) {
        if (isResolvableAddressOrName(s)) {
            this.String = s;
            this.isValid = true;
        } else {
            this.String = null;
            this.isValid = false;
        }
        return this.isValid;
    }

    @Override
    protected boolean UpdateAddress() {
        finalizeAddress();
        return true;
    }

    @Override
    public void remove() {
        this.String = null;
        this.isValid = false;
    }

    @Override
    public boolean isReturnable() {
        return false;
    }
}
