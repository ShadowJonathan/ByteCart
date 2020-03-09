package nl.jboi.minecraft.bytecart.Routing;

import nl.jboi.minecraft.bytecart.api.Wanderer.RoutingTable;
import org.bukkit.block.BlockFace;

import java.util.Iterator;
import java.util.Set;

/**
 * An abstract class for routing tables
 */
abstract class AbstractRoutingTable implements RoutingTable {

    @Override
    public final boolean isDirectlyConnected(int ring, BlockFace direction) {
        if (this.getDirection(ring) != null)
            return this.getMetric(ring, direction) == 0;
        return false;
    }

    @Override
    public final int getDirectlyConnected(BlockFace direction) {
        Set<Integer> rings = getDirectlyConnectedList(direction);
        return rings.size() == 1 ? rings.iterator().next() : -1;
    }

    @Override
    public final BlockFace getFirstUnknown() {
        for (BlockFace face : BlockFace.values()) {
            switch (face) {
                case NORTH:
                case EAST:
                case SOUTH:
                case WEST:

                    if (this.getDirectlyConnectedList(face).isEmpty())
                        return face;
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * Store a line in the routing table
     *
     * @param entry     the track number
     * @param direction the direction to associate
     * @param metric    the metric to associate
     */
    public abstract void setEntry(int entry, BlockFace direction, int metric);

    @Override
    public abstract Set<Integer> getNotDirectlyConnectedList(BlockFace direction);

    /**
     * Remove a line from the routing table
     *
     * @param entry the track number
     * @param from  the direction to remove
     */
    public abstract void removeEntry(int entry, BlockFace from);
}
