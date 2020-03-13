package nl.jboi.minecraft.bytecart.updater;

import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import org.bukkit.block.BlockFace;

/**
 * This class implements a wanderer that will run through all routers
 * randomly, without going to branches.
 * <p>
 * Wanderers implementors may extends this class and overrides its methods
 */
class DefaultRouterWanderer extends AbstractUpdater {

    DefaultRouterWanderer(BCSign bc, int region) {
        super(bc, region);
    }

    @Override
    public void doAction(Side To) {
        return;
    }

    @Override
    public void doAction(BlockFace To) {
    }

    @Override
    public Side giveSimpleDirection() {
        return Side.LEVER_OFF;
    }

    @Override
    public BlockFace giveRouterDirection() {
        return getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
    }
}
