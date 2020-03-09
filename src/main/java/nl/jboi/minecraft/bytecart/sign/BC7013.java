package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.hal.SuperRegistry;
import nl.jboi.minecraft.bytecart.io.InputFactory;
import nl.jboi.minecraft.bytecart.io.InputPin;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

/**
 * A ring field setter using redstone
 */
class BC7013 extends BC7014 implements Triggable {

    BC7013(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected String format(RegistryInput wire, AddressRouted InvAddress) {
        return "" + InvAddress.getRegion().getAmount() + "."
                + wire.getAmount() + "."
                + InvAddress.getStation().getAmount();
    }

    @Override
    protected void addIO() {
        // Input[0] : wire on left
        Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
        RegistryInput wire = InputFactory.getInput(block);

        InputPin[] levers = new InputPin[2];
        block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal()));
        levers[0] = InputFactory.getInput(block);

        block = this.getBlock().getRelative(getCardinal().getOppositeFace());
        levers[1] = InputFactory.getInput(block);

        RegistryInput ret = new SuperRegistry<RegistryInput>(new PinRegistry<InputPin>(levers), wire);

        this.addInputRegistry(ret);
    }

    @Override
    public String getName() {
        return "BC7013";
    }

    @Override
    public String getFriendlyName() {
        return "setTrack";
    }

    @Override
    protected boolean forceTicketReuse() {
        return true;
    }
}
