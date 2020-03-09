/**
 *
 */
package nl.jboi.minecraft.bytecart.sign;

/**
 * An eject sign
 */
final class BC7005 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     * @param vehicle
     */
    public BC7005(org.bukkit.block.Block block,
                  org.bukkit.entity.Vehicle vehicle) {
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
