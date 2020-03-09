package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.ReturnAddressFactory;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;

/**
 * A return address checker
 */
final class BC7016 extends AbstractTriggeredSign implements Triggable {

    BC7016(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public String getName() {
        return "BC7016";
    }

    @Override
    public String getFriendlyName() {
        return "Is returnable ?";
    }

    @Override
    public void trigger() {
        addIO();
        Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());

        if (returnAddress != null && returnAddress.isReturnable())
            this.getOutput(0).setAmount(3);
        else
            this.getOutput(0).setAmount(0);
    }

    /**
     * Register the levers output
     */
    private void addIO() {
        // Output[0] = 2 bits registry representing levers on the left and on the right of the sign
        OutputPin[] lever2 = new OutputPin[2];

        // Left
        lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
        // Right
        lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

        PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

        this.addOutputRegistry(command1);
    }

}
