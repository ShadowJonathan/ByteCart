package nl.jboi.minecraft.bytecart.Updaters;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.Storage.ExpirableSet;
import nl.jboi.minecraft.bytecart.api.Event.UpdaterRemoveEvent;
import org.bukkit.Bukkit;

import java.util.Iterator;

/**
 * A set for integers with a timeout of 1h
 */
final class UpdaterSet {

    private final ExpirableSet<Integer> updateSet;

    // entries stay for 1h
    UpdaterSet() {
        long duration = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60) * 1200;
        updateSet = new ExpirableSet<Integer>(duration, false, "UpdaterRoutes");
    }

    ExpirableSet<Integer> getMap() {
        return updateSet;
    }

    boolean isUpdater(Integer id) {
        return updateSet.contains(id);
    }

    void addUpdater(int id) {
        this.updateSet.add(id);
    }

    void clear() {
        Iterator<Integer> it = updateSet.getIterator();
        while (it.hasNext()) {
            Bukkit.getServer().getPluginManager().callEvent(new UpdaterRemoveEvent(it.next()));
        }
        updateSet.clear();
    }
}
