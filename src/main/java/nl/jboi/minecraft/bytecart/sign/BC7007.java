package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;

/**
 * An unbooster
 */
final class BC7007 extends AbstractTriggeredSign implements Triggable {

    /**
     * @param block
     * @param vehicle
     */
    public BC7007(Block block,
                  Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {
        Vehicle vehicle = this.getVehicle();
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
