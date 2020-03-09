package nl.jboi.minecraft.bytecart.event;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.ModifiableRunnable;
import nl.jboi.minecraft.bytecart.api.event.UpdaterCreateEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Class implementing a listener and waiting for a player to right-click an inventory holder
 * and running a Runnable
 */
public class ByteCartInventoryListener implements Listener {

    private final Player player;
    // the Runnable to update
    private final ModifiableRunnable<Inventory> exec;
    // flag set when we deal with an updater command
    private final boolean isUpdater;

    public ByteCartInventoryListener(ByteCart plugin, Player player, ModifiableRunnable<Inventory> exec,
                                     boolean is_updater) {
        this.player = player;
        this.exec = exec;
        this.isUpdater = is_updater;
        // self registering as Listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler()
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (event.getPlayer() == player && entity instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) entity).getInventory();
            // we set the member and run the Runnable
            this.exec.SetParam(inv);
            this.exec.run();
            // we cancel the right-click
            event.setCancelled(true);

            if (isUpdater) {
                // we launch an UpdaterCreateEvent
                StorageMinecart v = (StorageMinecart) inv.getHolder();
                UpdaterCreateEvent e = new UpdaterCreateEvent(v.getEntityId(), v.getLocation());
                ByteCart.myPlugin.getServer().getPluginManager().callEvent(e);
            }
        }
        // Self unregistering
        PlayerInteractEntityEvent.getHandlerList().unregister(this);
    }
}

