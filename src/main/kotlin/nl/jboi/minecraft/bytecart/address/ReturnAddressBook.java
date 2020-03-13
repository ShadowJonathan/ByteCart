package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.address.AddressBook.Parameter;
import nl.jboi.minecraft.bytecart.api.hal.RegistryBoth;

/**
 * A class implementing a return address in a book
 */
final class ReturnAddressBook implements Returnable {

    /**
     * The book address that this class will proxify
     */
    private final AddressBook address;

    /**
     * Creates a return address from a ticket of a certain type
     *
     * @param ticket    the ticket to use as support
     * @param parameter the type of the address
     */
    ReturnAddressBook(Ticket ticket, Parameter parameter) {
        this.address = new AddressBook(ticket, parameter);
    }

    @Override
    public RegistryBoth getRegion() {
        return address.getRegion();
    }

    @Override
    public RegistryBoth getTrack() {
        return address.getTrack();
    }

    @Override
    public RegistryBoth getStation() {
        return address.getStation();
    }

    @Override
    public boolean isTrain() {
        return address.isTrain();
    }

    @Override
    public boolean setAddress(String s) {
        return address.setAddress(s);
    }

    @Override
    public boolean setAddress(String s, String name) {
        return address.setAddress(s);
    }

    @Override
    public boolean setTrain(boolean istrain) {
        return address.setTrain(istrain);
    }

    @Override
    public boolean isValid() {
        return address.isValid();
    }

    @Override
    public void remove() {
        address.remove();
    }

    @Override
    public int getTTL() {
        return address.getTTL();
    }

    @Override
    public void updateTTL(int i) {
        address.updateTTL(i);
    }

    @Override
    public void initializeTTL() {
        address.initializeTTL();
    }

    @Override
    public boolean isReturnable() {
        return address.isReturnable();
    }

    @Override
    public String toString() {
        return address.toString();
    }

    @Override
    public void finalizeAddress() {
        address.finalizeAddress();
    }
}
