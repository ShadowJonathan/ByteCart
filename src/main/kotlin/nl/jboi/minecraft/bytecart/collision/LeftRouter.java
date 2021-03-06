package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.EnumSet;
import java.util.Set;

/**
 * A router where a cart turns left
 */
final class LeftRouter extends AbstractRouter implements Router {
    LeftRouter(BlockFace from, Location loc, boolean b) {
        super(from, loc, b);
        FromTo.put(Side.BACK, Side.LEFT);

        Set<Side> left = EnumSet.of(Side.LEFT, Side.STRAIGHT, Side.RIGHT);
        Possibility.put(Side.LEFT, left);

        Set<Side> straight = EnumSet.of(Side.STRAIGHT, Side.LEFT, Side.BACK);
        Possibility.put(Side.STRAIGHT, straight);

        Set<Side> right = EnumSet.of(Side.LEFT, Side.BACK);
        Possibility.put(Side.RIGHT, right);

        setSecondpos(0b01000000);
        setPosmask(0b11100000);
    }

    @Override
    public void route(BlockFace from) {
    }

    @Override
    public BlockFace getTo() {
        return MathUtil.clockwise(this.getFrom());
    }
}
