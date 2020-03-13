package nl.jboi.minecraft.bytecart.updater;

import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.wanderer.AbstractWanderer;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
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
