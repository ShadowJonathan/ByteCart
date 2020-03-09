package nl.jboi.minecraft.bytecart.plugins;

import nl.jboi.minecraft.bytecart.HAL.AbstractIC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.dynmap.markers.Marker;

import java.util.Iterator;

/**
 * Synchronous task to remove markers
 */
final class searchObsoleteMarkers implements Runnable {

    @Override
    public void run() {
        Iterator<Marker> it = BCDynmapPlugin.markerset.getMarkers().iterator();
        int x, y, z;
        while (it.hasNext()) {
            Marker m = it.next();
            x = Location.locToBlock(m.getX());
            y = Location.locToBlock(m.getY());
            z = Location.locToBlock(m.getZ());
            Block block = Bukkit.getServer().getWorld(m.getWorld()).getBlockAt(x, y, z);
            if (!AbstractIC.checkEligibility(block))
                m.deleteMarker();
        }
    }
}

