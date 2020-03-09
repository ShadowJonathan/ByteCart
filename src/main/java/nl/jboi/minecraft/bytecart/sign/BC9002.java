package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;


/**
 * A 2-station subnet bound
 */
final class BC9002 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable {

    BC9002(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
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
