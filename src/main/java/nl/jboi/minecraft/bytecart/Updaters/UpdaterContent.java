package nl.jboi.minecraft.bytecart.Updaters;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.Routing.Metric;
import nl.jboi.minecraft.bytecart.Routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.Wanderer.WandererContent;
import nl.jboi.minecraft.bytecart.api.Util.DirectionRegistry;
import nl.jboi.minecraft.bytecart.api.Wanderer.Wanderer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A class to store data in books used by updater
 */
public class UpdaterContent extends WandererContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 848098890652934583L;

    private boolean fullreset, isnew;
    private long lastrouterseen;

    UpdaterContent(Inventory inv, Wanderer.Level level, int region, Player player, boolean isfullreset) {
        this(inv, level, region, player, isfullreset, false);
    }

    UpdaterContent(Inventory inv, Wanderer.Level level, int region, Player player, boolean isfullreset, boolean isnew) {
        super(inv, level, region, player);
        this.fullreset = isfullreset;
        this.isnew = isnew;
        this.setExpirationTime(ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60) * 60000 + getCreationtime());
    }

    /**
     * Get a set of the entries of the IGP packet
     *
     * @return the set
     */
    public Set<Entry<Integer, Metric>> getEntrySet() {
        return tablemap.entrySet();
    }

    /**
     * Build the IGP exchange packet
     *
     * @param table     the routing table
     * @param direction the direction to exclude
     */
    void putRoutes(RoutingTableWritable table, DirectionRegistry direction) {
        tablemap.clear();
        Iterator<Integer> it = table.getOrderedRouteNumbers();
        while (it.hasNext()) {
            int i = it.next();
            if (table.getDirection(i) != null && table.getDirection(i) != direction.getBlockFace()) {
                tablemap.put(i, new Metric(table.getMinMetric(i)));
                if (ByteCart.debug)
                    ByteCart.log.info(String.format("ByteCart : Route exchange : give ring %d with metric %d to %s",
                            i, table.getMinMetric(i), table.getDirection(i)));
            }

        }
    }

    /**
     * Set the timestamp field to now
     */
    void seenTimestamp() {
        this.lastrouterseen = Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Get the time difference between now and the last time we called seenTimestamp()
     *
     * @return the time difference, or -1 if seenTimestamp() was never called
     */
    public int getInterfaceDelay() {
        if (lastrouterseen != 0)
            return (int) ((Calendar.getInstance().getTimeInMillis() - lastrouterseen) / 1000);
        return -1;
    }

    /**
     * @return the fullreset
     */
    boolean isFullreset() {
        return fullreset;
    }

    /**
     * @return the isnew
     */
    boolean isNew() {
        return isnew;
    }
}
