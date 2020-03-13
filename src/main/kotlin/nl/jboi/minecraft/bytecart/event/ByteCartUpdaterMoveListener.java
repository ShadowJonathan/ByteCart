package nl.jboi.minecraft.bytecart.event;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.event.UpdaterMoveEvent;
import nl.jboi.minecraft.bytecart.api.event.UpdaterRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Launch an event when an updater moves
 * This listener unregisters itself automatically if there is no updater
 */
public class ByteCartUpdaterMoveListener implements Listener {

    // flag for singleton
    private static boolean exist = false;

    /**
     * @return the exist
     */
    public static boolean isExist() {
        return exist;
    }

    /**
     * @param exist the exist to set
     */
    public static void setExist(boolean exist) {
        ByteCartUpdaterMoveListener.exist = exist;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleMoveEvent(VehicleMoveEvent event) {

        Location loc_from = event.getFrom();
        int from_x = loc_from.getBlockX();
        int from_z = loc_from.getBlockZ();

        Location loc_to = event.getTo();
        int to_x = loc_to.getBlockX();
        int to_z = loc_to.getBlockZ();

        // Check if the vehicle crosses a cube boundary
        if (from_x == to_x && from_z == to_z)
            return;    // no boundary crossed, resumed

        Vehicle v = event.getVehicle();
        // reset the timer
        if (v instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) v).getInventory();
            if (ByteCart.plugin.getWandererManager().isWanderer(inv, "Updater")) {
                Bukkit.getServer().getPluginManager().callEvent(new UpdaterMoveEvent(event));
                return;
            }
        }

        if (ByteCart.plugin.getWandererManager().getFactory("Updater").areAllRemoved()) {
            removeListener();
        }
    }

    /**
     * Detect a destroyed updater
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        Vehicle v = event.getVehicle();
        if (v instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) v).getInventory();
            if (ByteCart.plugin.getWandererManager().isWanderer(inv, "Updater")) {
                ByteCart.plugin.getWandererManager().getFactory("Updater").destroyWanderer(inv);
                Bukkit.getServer().getPluginManager().callEvent(new UpdaterRemoveEvent(v.getEntityId()));
            }
        }
    }

    private void removeListener() {
        HandlerList.unregisterAll(this);
        setExist(false);
    }
}
