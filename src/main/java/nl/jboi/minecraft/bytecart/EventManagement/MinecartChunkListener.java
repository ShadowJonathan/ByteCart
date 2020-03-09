package nl.jboi.minecraft.bytecart.EventManagement;

import nl.jboi.minecraft.bytecart.Util.MinecartChunkManager;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

/**
 * Listener to load chunks around moving carts
 */
public final class MinecartChunkListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Minecart) { // we care only about minecarts
            if (event.getTo().getChunk() != event.getFrom().getChunk())
            MinecartChunkManager.register(event.getTo().getChunk());
            MinecartChunkManager.checkAndUnregister(event.getFrom().getChunk());
        }
    }
}
