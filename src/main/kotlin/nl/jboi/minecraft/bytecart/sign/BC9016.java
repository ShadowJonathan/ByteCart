package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A 16-station subnet bound
 */
final class BC9016 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9016(Block block, Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 4;
    }

    @Override
    public final String getName() {
        return "BC9016";
    }

    @Override
    public final String getFriendlyName() {
        return "16-station subnet";
    }
}
