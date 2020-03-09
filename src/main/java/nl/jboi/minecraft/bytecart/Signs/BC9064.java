package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.api.Signs.HasNetmask;
import nl.jboi.minecraft.bytecart.api.Signs.Subnet;

/**
 * A 64-station subnet bound
 */
final class BC9064 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9064(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
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