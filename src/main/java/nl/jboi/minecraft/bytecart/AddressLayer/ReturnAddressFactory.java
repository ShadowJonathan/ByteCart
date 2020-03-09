package nl.jboi.minecraft.bytecart.AddressLayer;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressBook.Parameter;
import nl.jboi.minecraft.bytecart.FileStorage.BookFile;
import nl.jboi.minecraft.bytecart.api.AddressLayer.Address;
import nl.jboi.minecraft.bytecart.FileStorage.BookProperties;
import org.bukkit.inventory.Inventory;

/**
 * Factory class to create a return address from various supports
 */
public final class ReturnAddressFactory {

    /**
     * Creates a return address from a ticket
     *
     * @param inv the inventory containing the ticket
     * @return the return address
     */
    @SuppressWarnings("unchecked")
    public static <T extends Address> T getAddress(Inventory inv) {
        int slot;
        if ((slot = Ticket.getTicketslot(inv)) != -1)
            return (T) new ReturnAddressBook(new Ticket(BookFile.getFrom(inv, slot, false, "ticket"), BookProperties.Conf.NETWORK), Parameter.RETURN);
        return null;
    }
}
