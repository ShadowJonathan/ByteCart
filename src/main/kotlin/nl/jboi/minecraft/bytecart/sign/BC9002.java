package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A 2-station subnet bound
 */
final class BC9002 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9002(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 7;
    }

    @Override
    public final String getName() {
        return "BC9002";
    }

    @Override
    public final String getFriendlyName() {
        return "2-station subnet";
    }
}
