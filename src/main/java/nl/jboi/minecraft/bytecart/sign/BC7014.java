package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.io.InputFactory;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.block.BlockFace;

/**
 * A station field setter using a redstone signal strength
 */
class BC7014 extends BC7010 implements Triggable {

    BC7014(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.StorageCartAllowed = true;
    }

    @Override
    protected Address getAddressToWrite() {
        addIO();
        AddressRouted InvAddress = AddressFactory.getAddress(this.getInventory());

        if (InvAddress == null)
            return null;

        RegistryInput wire = this.getInput(0);

        if (wire == null || wire.getAmount() == 0)
            return null;

        if (ByteCart.debug)
            ByteCart.log.info("ByteCart: " + this.getName() + " wire : " + wire.getAmount());

        return AddressFactory.getAddress(format(wire, InvAddress));
    }

    /**
     * Build the address string
     *
     * @param wire       the wire to take as input
     * @param InvAddress the address to modify
     * @return a string containing the address
     */
    protected String format(RegistryInput wire, AddressRouted InvAddress) {
        return "" + InvAddress.getRegion().getAmount() + "."
                + InvAddress.getTrack().getAmount() + "."
                + wire.getAmount();
    }

    /**
     * Register the input wire on the left of the sign
     */
    protected void addIO() {
        // Input[0] : wire on left
        org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
        RegistryInput wire = InputFactory.getInput(block);
        this.addInputRegistry(wire);
    }

    @Override
    protected final boolean getIsTrain() {
        boolean signtrain = super.getIsTrain();
        Address address;
        if ((address = AddressFactory.getAddress(this.getInventory())) != null)
            return address.isTrain() || signtrain;
        return signtrain;
    }

    @Override
    public String getName() {
        return "BC7014";
    }

    @Override
    public String getFriendlyName() {
        return "setStation";
    }

    @Override
    protected boolean forceTicketReuse() {
        return true;
    }
}
