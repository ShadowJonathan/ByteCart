package com.github.catageek.ByteCart.EventManagement;

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

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Event.UpdaterMoveEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterRemoveEvent;

/**
 * Launch an event when an updater moves
 * This listener unregisters itself automatically if there is no updater
 */
public class ByteCartUpdaterMoveListener implements Listener {

	// flag for singleton
	private static boolean exist = false;

	@EventHandler(ignoreCancelled = true)
	public void onVehicleMoveEvent(VehicleMoveEvent event) {

		Location loc_from = event.getFrom();
		int from_x = loc_from.getBlockX();
		int from_z = loc_from.getBlockZ();

		Location loc_to = event.getTo();
		int to_x = loc_to.getBlockX();
		int to_z = loc_to.getBlockZ();

		// Check if the vehicle crosses a cube boundary
		if(from_x == to_x && from_z == to_z)
			return;	// no boundary crossed, resumed

		Vehicle v = event.getVehicle();
		// reset the timer
		if (v instanceof InventoryHolder) {
			Inventory inv = ((InventoryHolder) v).getInventory();
			if (ByteCart.myPlugin.getWandererManager().isWanderer(inv, "Updater")) {
				Bukkit.getServer().getPluginManager().callEvent(new UpdaterMoveEvent(event));
				return;
			}
		}

		if (ByteCart.myPlugin.getWandererManager().getFactory("Updater").areAllRemoved()) {
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
			if (ByteCart.myPlugin.getWandererManager().isWanderer(inv, "Updater")) {
				ByteCart.myPlugin.getWandererManager().getFactory("Updater").destroyWanderer(inv);
				Bukkit.getServer().getPluginManager().callEvent(new UpdaterRemoveEvent(v.getEntityId()));
			}
		}
	}
	
	private void removeListener() {
		HandlerList.unregisterAll(this);
		setExist(false);
	}

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
}
