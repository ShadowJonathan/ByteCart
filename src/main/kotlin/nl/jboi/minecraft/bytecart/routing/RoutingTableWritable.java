package nl.jboi.minecraft.bytecart.routing;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.wanderer.RoutingTable;
import nl.jboi.minecraft.bytecart.updater.UpdaterContent;
import org.bukkit.block.BlockFace;

import java.io.IOException;
import java.util.Map;

/**
 * A routing table
 */
public interface RoutingTableWritable extends RoutingTable {

    /**
     * Store a line in the routing table
     *
     * @param entry     the track number
     * @param direction the direction to associate
     * @param metric    the metric to associate
     */
    void setEntry(int entry, BlockFace direction, int metric);

    /**
     * Remove a line from the routing table
     *
     * @param entry the track number
     * @param from  the direction to remove
     */
    void removeEntry(int entry, BlockFace from);

    /**
     * Performs the IGP protocol to update the routing table
     *
     * @param neighbour the IGP packet received
     * @param from      the direction from where we received it
     */
    default void Update(UpdaterContent neighbour, BlockFace from) {

        // Djikstra algorithm
        // search for better routes in the received ones
        int interfacedelay = neighbour.getInterfaceDelay();

        for (Map.Entry<Integer, Metric> entry : neighbour.getEntrySet()) {

            int ring = entry.getKey();
            Metric metric = entry.getValue();
            int computedmetric = metric.value();
            if (interfacedelay > 0)
                computedmetric += interfacedelay;
            int routermetric = this.getMetric(ring, from);
            boolean directlyconnected = (this.getMinMetric(ring) == 0);

            if (!directlyconnected && (routermetric > computedmetric || routermetric < 0)) {
                this.setEntry(ring, from, computedmetric);
                if (ByteCart.debug) {
                    ByteCart.log.info("ByteCart : Update : ring = " + ring + ", metric = " + computedmetric + ", direction " + from);
                }
                ByteCart.plugin.getWandererManager().getFactory("Updater").updateTimestamp(neighbour);
            }
        }
        // search for routes that are no more announced and not directly connected
        // to remove them
        for (Integer route : this.getNotDirectlyConnectedList(from)) {
            if (!neighbour.hasRouteTo(route)) {
                this.removeEntry(route, from);
                if (ByteCart.debug) {
                    ByteCart.log.info("ByteCart : Remove : ring = " + route + " from " + from);
                }
                ByteCart.plugin.getWandererManager().getFactory("Updater").updateTimestamp(neighbour);
            }
        }
    }

    /**
     * Clear the routing table
     *
     * @param fullreset if set to false, route to entry 0 is kept.
     */
    void clear(boolean fullreset);

    /**
     * Serialize the routing table
     *
     * @param allowconversion if set to false, non conversion to another format
     * @throws IOException
     */
    void serialize(boolean allowconversion) throws IOException;
}
