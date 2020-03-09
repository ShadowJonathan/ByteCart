package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A 8-station subnet bound
 */
final class BC9008 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9008(Block block, Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 5;
    }

    @Override
    public final String getName() {
        return "BC9008";
    }

    @Override
    public final String getFriendlyName() {
        return "8-station subnet";
    }
}
