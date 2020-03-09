package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressRouted;
import nl.jboi.minecraft.bytecart.HAL.PinRegistry;
import nl.jboi.minecraft.bytecart.HAL.SuperRegistry;
import nl.jboi.minecraft.bytecart.IO.InputFactory;
import nl.jboi.minecraft.bytecart.IO.InputPin;
import nl.jboi.minecraft.bytecart.api.HAL.RegistryInput;
import nl.jboi.minecraft.bytecart.api.Util.MathUtil;
import org.bukkit.block.BlockFace;

/**
 * A ring field setter using redstone
 */
class BC7013 extends BC7014 implements Triggable {

    BC7013(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
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
        org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
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
