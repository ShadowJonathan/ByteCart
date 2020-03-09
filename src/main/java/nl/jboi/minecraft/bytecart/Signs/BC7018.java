package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.AddressLayer.TicketFactory;
import org.bukkit.entity.Player;

/**
 * A ticket remover
 */
class BC7018 extends AbstractTriggeredSign implements Triggable, Clickable {

    BC7018(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        // TODO Auto-generated constructor stub
    }

    BC7018(org.bukkit.block.Block block, Player player) {
        super(block, null);
        this.setInventory(player.getInventory());
    }

    @Override
    public void click() {
        this.trigger();
    }

    @Override
    public void trigger() {
        TicketFactory.removeTickets(this.getInventory());
    }

    @Override
    public String getName() {
        return "BC7018";
    }

    @Override
    public String getFriendlyName() {
        return "Remove Ticket";
    }
}
