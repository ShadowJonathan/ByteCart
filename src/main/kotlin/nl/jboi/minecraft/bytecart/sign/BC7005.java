package nl.jboi.minecraft.bytecart.sign;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * An eject sign
 */
final class BC7005 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     * @param vehicle
     */
    public BC7005(Block block,
                  Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {
        if (this.getVehicle() != null)
            this.getVehicle().eject();
    }

    @Override
    public String getName() {
        return "BC7005";
    }

    @Override
    public String getFriendlyName() {
        return "Eject";
    }
}
