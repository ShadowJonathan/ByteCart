package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A region field setter using redstone
 */
final class BC7012 extends BC7013 implements Triggable {

    BC7012(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected String format(RegistryInput wire, AddressRouted InvAddress) {
        return "" + wire.getAmount() + "."
                + InvAddress.getTrack().getAmount() + "."
                + InvAddress.getStation().getAmount();
    }

    @Override
    public final String getName() {
        return "BC7012";
    }

    @Override
    public final String getFriendlyName() {
        return "setRegion";
    }

    @Override
    protected boolean forceTicketReuse() {
        return true;
    }
}
