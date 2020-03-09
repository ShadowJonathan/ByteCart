/**
 *
 */
package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ListIterator;

/**
 * A cart remover
 */
final class BC7008 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     */
    public BC7008(org.bukkit.block.Block block, Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public String getName() {
        return "BC7008";
    }

    @Override
    public String getFriendlyName() {
        return "Cart remover";
    }

    @Override
    public void trigger() {
        org.bukkit.entity.Vehicle vehicle = this.getVehicle();

        // we eject the passenger
        vehicle.eject();

        // we drop items
        if (ByteCart.myPlugin.keepItems()) {
            org.bukkit.inventory.Inventory inventory;
            if (vehicle instanceof InventoryHolder) {
                inventory = ((InventoryHolder) vehicle).getInventory();
                World world = this.getBlock().getWorld();
                org.bukkit.Location loc = this.getBlock().getRelative(BlockFace.UP, 2).getLocation();
                ListIterator<ItemStack> it = inventory.iterator();
                while (it.hasNext()) {
                    ItemStack stack = it.next();
                    if (stack != null)
                        world.dropItem(loc, stack);
                }
            }
        }

        vehicle.remove();
    }
}
