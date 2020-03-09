/**
 *
 */
package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.api.Util.MathUtil;
import org.bukkit.entity.Minecart;

/**
 * An unbooster
 */
final class BC7007 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     * @param vehicle
     */
    public BC7007(org.bukkit.block.Block block,
                  org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {
        org.bukkit.entity.Vehicle vehicle = this.getVehicle();
        Minecart cart = (Minecart) vehicle;
        if (cart.getMaxSpeed() > 0.4D)
            cart.setMaxSpeed(0.4D);

        MathUtil.setSpeed(cart, 0.4D);
    }

    @Override
    public String getName() {
        return "BC7007";
    }

    @Override
    public String getFriendlyName() {
        return "Unbooster";
    }
}
