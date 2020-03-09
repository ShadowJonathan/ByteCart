package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.TicketFactory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

/**
 * A ticket remover
 */
class BC7018 extends AbstractTriggeredSign implements Triggable, Clickable {

    BC7018(Block block, Vehicle vehicle) {
        super(block, vehicle);
        // TODO Auto-generated constructor stub
    }

    BC7018(Block block, Player player) {
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
