package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.api.Signs.HasNetmask;
import nl.jboi.minecraft.bytecart.api.Signs.Subnet;


/**
 * A 16-station subnet bound
 */
final class BC9016 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
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
