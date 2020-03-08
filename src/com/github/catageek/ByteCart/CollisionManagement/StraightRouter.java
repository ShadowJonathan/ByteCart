package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import org.bukkit.block.BlockFace;

/**
 * A router where the cart goes straight
 */
class StraightRouter extends AbstractRouter implements Router {
    StraightRouter(BlockFace from, org.bukkit.Location loc, boolean isOldVersion) {
        super(from, loc, isOldVersion);

        FromTo.put(Side.BACK, Side.STRAIGHT);
        FromTo.put(Side.LEFT, Side.LEFT);
        FromTo.put(Side.STRAIGHT, Side.RIGHT);
        FromTo.put(Side.RIGHT, Side.BACK);

        setSecondpos(0b00100101);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#route(org.bukkit.block.BlockFace)
     */
    @Override
    public void route(BlockFace from) {
        // activate main levers
        this.getOutput(0).setAmount(new DirectionRegistry(from.getOppositeFace()).getAmount());
    }

    /* (non-Javadoc)
     * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#getTo()
     */
    @Override
    public final BlockFace getTo() {
        return this.getFrom().getOppositeFace();
    }
}
