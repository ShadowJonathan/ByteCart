package com.github.catageek.ByteCart.Updaters;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Wanderer.AbstractWanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;
import org.bukkit.block.BlockFace;

class UpdaterBackBone extends AbstractRegionUpdater implements Wanderer {
    UpdaterBackBone(BCSign bc, UpdaterContent rte) {
        super(bc, rte);
    }

    @Override
    protected BlockFace selectDirection() {
        BlockFace face;
        if ((face = manageBorder()) != null)
            return face;

        return AbstractWanderer.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
    }

    @Override
    public void Update(BlockFace To) {
        // current: track number we are on
        int current = getCurrent();

        if (getRoutes() != null) {
            if (getSignAddress().isValid()) {
                // there is an address on the sign
                if (ByteCart.debug)
                    ByteCart.log.info("ByteCart : track number is " + getTrackNumber());

                setCurrent(getTrackNumber());

                if (ByteCart.debug)
                    ByteCart.log.info("ByteCart : current is " + current);
            } else
                // no address on sign, and is not provider
                // assumes it's 0 if first sign met
                if (current == -2)
                    setCurrent(0);

            routeUpdates(To);
        }
    }

    @Override
    public final int getTrackNumber() {
        Address address;
        if ((address = getSignAddress()).isValid())
            return address.getRegion().getAmount();
        return -1;
    }
}
