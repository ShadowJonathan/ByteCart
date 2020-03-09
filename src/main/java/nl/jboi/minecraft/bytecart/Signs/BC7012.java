package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressRouted;
import nl.jboi.minecraft.bytecart.api.HAL.RegistryInput;

/**
 * A region field setter using redstone
 */
final class BC7012 extends BC7013 implements Triggable {

    BC7012(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
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
