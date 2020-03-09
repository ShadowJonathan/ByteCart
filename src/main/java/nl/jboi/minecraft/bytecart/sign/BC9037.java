package nl.jboi.minecraft.bytecart.sign;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * Match IP ranges.
 * <p>
 * 1. Empty
 * 2. [BC9037]
 * 3. AA.BB.CC
 * 4. XX.YY.ZZ
 * <p>
 * onState <=> AA.BB.CC <= IP <= XX.YY.ZZ
 */
final class BC9037 extends AbstractBC9037 {

    BC9037(Block block, Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected boolean negated() {
        return false;
    }

    @Override
    public final String getName() {
        return "BC9037";
    }

    @Override
    public final String getFriendlyName() {
        return "Range matcher";
    }
}
