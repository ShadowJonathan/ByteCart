package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.ByteCartAPI;


/**
 * Abstract class implementing basic operations on address
 * <p>
 * All address subclass must extend this class
 */
abstract class AbstractAddress implements Address {

    /**
     * A flag to tell to the world if the address should be considered as valid or not
     */
    protected boolean isValid = true;

    @Override
    public final boolean isValid() {
        return isValid;
    }

    /**
     * Copy the fields of the address object
     *
     * @param a the source address to copy
     * @return true if the address was copied
     */
    private boolean setAddress(Address a) {
        this.setStation(a.getStation().getAmount());
        this.setIsTrain(a.isTrain());
        this.setTrack(a.getTrack().getAmount());
        this.setRegion(a.getRegion().getAmount());
        return this.UpdateAddress();

    }

    @Override
    public boolean setAddress(String a, String name) {
        return this.setAddress(a);
    }

    @Override
    public boolean setAddress(String s) {
        return setAddress(AddressFactory.getUnresolvedAddress(s));
    }

    @Override
    public final boolean setTrain(boolean istrain) {
        this.setIsTrain(istrain);
        return this.UpdateAddress();
    }

    @Override
    public String toString() {
        return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount());
    }

    /**
     * flush the address to its support
     *
     * @return always true
     */
    protected boolean UpdateAddress() {
        finalizeAddress();
        return true;
    }

    @Override
    public void finalizeAddress() {
    }

    /**
     * Set the region field
     *
     * @param region the region number to set
     */
    protected abstract void setRegion(int region);

    /**
     * Set the ring field
     *
     * @param track the ring number to set
     */
    protected abstract void setTrack(int track);

    /**
     * Set the station field
     *
     * @param station the station number to set
     */
    protected abstract void setStation(int station);

    /**
     * Set the train flag
     *
     * @param isTrain true if the flag must be set
     */
    protected abstract void setIsTrain(boolean isTrain);

    /**
     * Length (in bits) for various fields of address
     * <p>
     * position is deprecated
     */
    protected enum Offsets {
        // length (default : 6), pos (default : 0)
        REGION(ByteCartAPI.MAXRINGLOG, 0),
        TRACK(ByteCartAPI.MAXRINGLOG, 0),
        STATION(ByteCartAPI.MAXSTATIONLOG, 0);

        private final int Length, Offset;

        Offsets(int length, int offset) {
            Length = length;
            Offset = offset;
        }

        /**
         * @return the length
         */
        public int getLength() {
            return Length;
        }


        /**
         * @return the offset
         */
        public int getOffset() {
            return Offset;
        }
    }
}
