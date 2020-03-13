package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.api.hal.RegistryOutput;
import nl.jboi.minecraft.bytecart.api.util.DirectionRegistry;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.ComponentSign;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

final class BC7009 extends AbstractTriggeredSign implements Triggable {

    private final BlockFace From;

    BC7009(Block block, Vehicle vehicle) {
        super(block, vehicle);
        From = getCardinal().getOppositeFace();
    }

    @Override
    public void trigger() {
        this.addIO();
        RoundRobin();
    }

    private void RoundRobin() {
        final ComponentSign sign = new ComponentSign(this.getBlock());
        final String line = sign.getLine(3);
        DirectionRegistry dir;
        try {
            int current = Integer.parseInt(line);
            dir = new DirectionRegistry(current);
        } catch (NumberFormatException e) {
            dir = new DirectionRegistry(From);
        }
        BlockFace newdir = MathUtil.clockwise(dir.getBlockFace());
        if (newdir.equals(From))
            newdir = MathUtil.clockwise(newdir);
        final int amount = new DirectionRegistry(newdir).getAmount();
        this.getOutput(0).setAmount(amount);
        sign.setLine(3, "" + amount);
    }

    @Override
    public String getName() {
        return "BC7009";
    }

    @Override
    public String getFriendlyName() {
        return "Load Balancer";
    }

    /**
     * Registers levers as output
     */
    private void addIO() {

        // Center of the device, at sign level
        Block center = this.getBlock().getRelative(this.getCardinal(), 2).getRelative(MathUtil.clockwise(this.getCardinal()));

        // Main output
        OutputPin[] main = new OutputPin[4];

        /** fixme rework {@link nl.jboi.minecraft.bytecart.collision.AbstractRouter#addIO} */
        // East
        main[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST, 3).getRelative(BlockFace.SOUTH));
        // North
        main[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST, 3).getRelative(BlockFace.NORTH));
        // South
        main[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH, 3).getRelative(BlockFace.EAST));
        // West
        main[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH, 3).getRelative(BlockFace.WEST));

        // output[0] is main levers
        this.addOutputRegistry(new PinRegistry<>(main));
    }
}
