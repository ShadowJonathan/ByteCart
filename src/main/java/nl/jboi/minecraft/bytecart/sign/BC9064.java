package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A 64-station subnet bound
 */
final class BC9064 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9064(Block block, Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 2;
    }

    @Override
    public final String getName() {
        return "BC9064";
    }

    @Override
    public final String getFriendlyName() {
        return "64-station subnet";
    }
}