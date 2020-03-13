package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A 128-station subnet bound
 */
final class BC9128 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9128(Block block, Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 1;
    }

    @Override
    public final String getName() {
        return "BC9128";
    }

    @Override
    public final String getFriendlyName() {
        return "128-station subnet";
    }
}