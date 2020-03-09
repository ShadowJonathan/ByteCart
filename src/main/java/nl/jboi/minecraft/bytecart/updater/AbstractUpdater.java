package nl.jboi.minecraft.bytecart.updater;

import nl.jboi.minecraft.bytecart.routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.sign.BC8010;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.wanderer.AbstractWanderer;
import org.bukkit.block.BlockFace;

abstract class AbstractUpdater extends AbstractWanderer {

    private final RoutingTableWritable RoutingTable;

    protected AbstractUpdater(BCSign bc, int region) {
        super(bc, region);

        if (bc instanceof BC8010) {
            BC8010 ic = (BC8010) bc;
            RoutingTable = ic.getRoutingTable();
        } else {
            RoutingTable = null;
        }

    }

    /**
     * Get the direction where to go if we are at the border of a region or the backbone
     *
     * @return the direction where we must go
     */
    public final BlockFace manageBorder() {
        if ((isAtBorder())) {
            BlockFace dir;
            if ((dir = this.getRoutingTable().getDirection(this.getWandererRegion())) != null)
                return dir;
            return getFrom().getBlockFace();
        }
        return null;
    }

    /**
     * @return the routing table
     */
    protected final RoutingTableWritable getRoutingTable() {
        return RoutingTable;
    }
}
