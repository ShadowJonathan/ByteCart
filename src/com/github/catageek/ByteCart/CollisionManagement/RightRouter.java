package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Util.MathUtil;
import org.bukkit.block.BlockFace;

/**
 * A router where the cart turns right
 */
final class RightRouter extends AbstractRouter implements Router {
    RightRouter(BlockFace from, org.bukkit.Location loc, boolean b) {
        super(from, loc, b);

        FromTo.put(Side.BACK, Side.RIGHT);
        FromTo.put(Side.LEFT, Side.LEFT);
        FromTo.put(Side.STRAIGHT, Side.STRAIGHT);
        FromTo.put(Side.RIGHT, Side.BACK);

        setSecondpos(0b00101001);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#route(org.bukkit.block.BlockFace)
     */
    @Override
    public void route(BlockFace from) {
        // activate main levers
        this.getOutput(0).setAmount((new DirectionRegistry(MathUtil.anticlockwise(from))).getAmount());
    }

    /* (non-Javadoc)
     * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#getTo()
     */
    @Override
    public BlockFace getTo() {
        return MathUtil.anticlockwise(this.getFrom());
    }
}
