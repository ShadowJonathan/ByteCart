package nl.jboi.minecraft.bytecart.address;

import nl.jboi.minecraft.bytecart.address.AddressBook.Parameter;
import nl.jboi.minecraft.bytecart.file.BookFile;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.file.BookProperties;
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
