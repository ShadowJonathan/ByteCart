package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.api.util.DirectionRegistry;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/**
 * A router where the cart goes straight
 */
class StraightRouter extends AbstractRouter implements Router {
    StraightRouter(BlockFace from, Location loc, boolean isOldVersion) {
        super(from, loc, isOldVersion);

        FromTo.put(Side.BACK, Side.STRAIGHT);
        FromTo.put(Side.LEFT, Side.LEFT);
        FromTo.put(Side.STRAIGHT, Side.RIGHT);
        FromTo.put(Side.RIGHT, Side.BACK);

        setSecondpos(0b00100101);
    }

    @Override
    public void route(BlockFace from) {
        // activate main levers
        this.getOutput(0).setAmount(new DirectionRegistry(from.getOppositeFace()).getAmount());
    }

    @Override
    public final BlockFace getTo() {
        return this.getFrom().getOppositeFace();
    }
}
