package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.routing.RoutingTableWritable;

/**
 * An IC that have a routing table should implement this
 */
interface HasRoutingTable {
    /**
     * Get the routing table
     *
     * @return the routing table
     */
    RoutingTableWritable getRoutingTable();
}
