package nl.jboi.minecraft.bytecart.collision;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.EnumSet;
import java.util.Set;

/**
 * A router where the cart goes back
 */
final class BackRouter extends AbstractRouter implements Router {
    BackRouter(BlockFace from, Location loc, boolean b) {
        super(from, loc, b);
        FromTo.put(Side.BACK, Side.BACK);

        Set<Side> left = EnumSet.of(Side.BACK, Side.LEFT);
        Possibility.put(Side.LEFT, left);

        Set<Side> straight = EnumSet.of(Side.RIGHT, Side.LEFT, Side.BACK);
        Possibility.put(Side.STRAIGHT, straight);

        Set<Side> right = EnumSet.of(Side.STRAIGHT, Side.BACK, Side.RIGHT);
        Possibility.put(Side.RIGHT, right);

        setSecondpos(0b10000000);
        setPosmask(0b11000001);
    }

    @Override
    public void route(BlockFace from) {
    }

    @Override
    public final BlockFace getTo() {
        return this.getFrom();
    }
}
