package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressFactory;
import nl.jboi.minecraft.bytecart.AddressLayer.AddressRouted;
import nl.jboi.minecraft.bytecart.AddressLayer.TicketFactory;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.HAL.AbstractIC;
import nl.jboi.minecraft.bytecart.api.AddressLayer.Address;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

/**
 * Base class for all signs that are triggered by vehicles that pass over it.
 * <p>
 * Reads the address configuration of the vehicle from its inventory and caches it.
 */
abstract class AbstractTriggeredSign extends AbstractIC implements Triggable {

    private final org.bukkit.entity.Vehicle Vehicle;
    private org.bukkit.inventory.Inventory Inventory;

    AbstractTriggeredSign(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block);
        this.Vehicle = vehicle;

        this.Inventory = this.extractInventory();
    }

    public static boolean isTrain(Address address) {
        if (address != null)
            return address.isTrain();
        return false;
    }

    /**
     * @return The vehicle which triggered the sign.
     */
    public final org.bukkit.entity.Vehicle getVehicle() {
        return Vehicle;
    }

    /**
     * Extract the address configuration of the current vehicle. If the vehicle has
     * no address configuration, the default address (if configured) is applied.
     *
     * @return Inventory with address configuration from the current vehicle.
     */
    private org.bukkit.inventory.Inventory extractInventory() {

        org.bukkit.inventory.Inventory newInv = Bukkit.createInventory(null, 27);


        // we load inventory of cart or player
        if (this.Vehicle != null) {

            if (this.getVehicle() instanceof InventoryHolder)
                return ((InventoryHolder) this.getVehicle()).getInventory();

            else if (this.getVehicle() instanceof Minecart) {
                final List<Entity> passengers = this.getVehicle().getPassengers();
                for (Entity passenger : passengers) {
                    if (passenger instanceof InventoryHolder) {
                        if (passenger instanceof Player) {
                            if (ByteCart.debug)
                                ByteCart.log.info("ByteCart: loading player inventory :" + ((Player) passenger).getDisplayName());
                        }
                        return ((InventoryHolder) passenger).getInventory();
                    }
                }
            }

            /* There is no inventory, so we create one */

            // we have a default route ? so we write it in inventory
            if (ByteCart.myPlugin.getConfig().contains("EmptyCartsDefaultRoute")) {
                String DefaultRoute = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute");
                TicketFactory.getOrCreateTicket(newInv);
                //construct address object
                AddressRouted myAddress = AddressFactory.getAddress(newInv);
                //write address
                myAddress.setAddress(DefaultRoute);
                myAddress.initializeTTL();
                myAddress.finalizeAddress();
            }

        }
        return newInv;
    }

    /**
     * @return The inventory of the vehicle which triggered this sign.
     */
    public org.bukkit.inventory.Inventory getInventory() {
        return Inventory;
    }

    /**
     * Set the inventory variable
     *
     * @param inv
     */
    protected void setInventory(org.bukkit.inventory.Inventory inv) {
        this.Inventory = inv;
    }

    @Override
    public final boolean isTrain() {
        return AbstractTriggeredSign.isTrain(AddressFactory.getAddress(this.getInventory()));
    }

    @Override
    public final boolean wasTrain(Location loc) {
        boolean ret;
        if (ByteCart.myPlugin.getIsTrainManager().getMap().contains(loc)) {
            ret = ByteCart.myPlugin.getIsTrainManager().getMap().get(loc);
			/*			if(ByteCart.debug  && ret)
				ByteCart.log.info("ByteCart: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is wagon !");
			 */
            return ret;
        }
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is not wagon !");
		 */
        return false;
    }

    /**
     * Remember the train bit
     *
     * @param loc the location where to store the bit
     * @param b   the bit
     */
    protected final void setWasTrain(Location loc, boolean b) {
        if (b)
            ByteCart.myPlugin.getIsTrainManager().getMap().put(loc, true);

    }

    /**
     * Default is lever not reversed
     *
     * @return false
     */
    @Override
    public boolean isLeverReversed() {
        return false;
    }


}
