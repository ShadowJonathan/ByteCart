package nl.jboi.minecraft.bytecart.sign;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * Match IP ranges and negate the result.
 * <p>
 * 1. Empty
 * 2. [BC9137]
 * 3. AA.BB.CC
 * 4. XX.YY.ZZ
 * <p>
 * onState <=> !(AA.BB.CC <= IP <= XX.YY.ZZ)
 */
final class BC9137 extends AbstractBC9037 {

    BC9137(Block block, Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected boolean negated() {
        return true;
    }

    @Override
    public final String getName() {
        return "BC9137";
    }

    @Override
    public final String getFriendlyName() {
        return "Negated range matcher";
    }

    @Override
    public boolean isLeverReversed() {
        return true;
    }
}
