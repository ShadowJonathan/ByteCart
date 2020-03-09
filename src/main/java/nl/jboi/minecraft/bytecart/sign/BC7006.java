/**
 *
 */
package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.entity.Minecart;

/**
 * A booster
 */
final class BC7006 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     * @param vehicle
     */
    public BC7006(org.bukkit.block.Block block,
                  org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {
        org.bukkit.entity.Vehicle vehicle = this.getVehicle();
        Minecart cart = (Minecart) vehicle;
        if (cart.getMaxSpeed() <= 0.4D)
            cart.setMaxSpeed(0.68D);

        MathUtil.setSpeed(cart, 0.68D);
    }

    @Override
    public String getName() {
        return "BC7006";
    }

    @Override
    public String getFriendlyName() {
        return "Booster";
    }
}
