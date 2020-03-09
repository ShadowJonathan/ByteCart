package nl.jboi.minecraft.bytecart.Updaters;

import nl.jboi.minecraft.bytecart.api.Signs.BCSign;
import nl.jboi.minecraft.bytecart.api.Wanderer.AbstractWanderer;
import nl.jboi.minecraft.bytecart.api.Wanderer.Wanderer;
import org.bukkit.block.BlockFace;

class UpdaterResetBackbone extends UpdaterBackBone implements Wanderer {

    UpdaterResetBackbone(BCSign bc, UpdaterContent rte) {
        super(bc, rte);
    }


    @Override
    public void doAction(BlockFace to) {
        if (!this.isAtBorder())
            reset();
    }

    @Override
    protected BlockFace selectDirection() {
        BlockFace face;
        if ((face = manageBorder()) != null)
            return face;
        return AbstractWanderer.getRandomBlockFace(getRoutingTable(), getFrom().getBlockFace());
    }


}
