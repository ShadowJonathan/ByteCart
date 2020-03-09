package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a local updater enters a subnet
 */
public class UpdaterEnterSubnetEvent extends UpdaterEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Address address, oldaddress;
    private final int length, oldlength;

    /**
     * Default constructor
     *
     * @param updater    updater involved
     * @param address    address of the subnet
     * @param length     number of stations this subnet can contain
     * @param oldlength  number of stations the subnet we are nested in can contain
     * @param oldaddress address of the subnet we are nested in
     */
    public UpdaterEnterSubnetEvent(Wanderer updater, Address address, int length, Address oldaddress, int oldlength) {
        super(updater);
        this.address = address;
        this.length = length;
        this.oldaddress = oldaddress;
        this.oldlength = oldlength;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return the length of the subnet we enter
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the address of the subnet we enter
     */
    public String getAddress() {
        return address.toString();
    }

    /**
     * @return the address of the subnet we are nested in
     */
    public String getOldAddressString() {
        return oldaddress.toString();
    }

    /**
     * @return the address of the subnet we are nested in
     */
    @Deprecated
    public Address getOldAddress() {
        return oldaddress;
    }

    /**
     * @return the length of the subnet we are nested in
     */
    public int getOldLength() {
        return oldlength;
    }
}
