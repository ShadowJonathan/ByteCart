package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.hal.RegistryOutput;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.data.ExpirableMap;
import nl.jboi.minecraft.bytecart.hal.AbstractIC;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.InputFactory;
import nl.jboi.minecraft.bytecart.io.InputPin;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import nl.jboi.minecraft.bytecart.thread.Expirable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * A cart counter
 */
final class BC7003 extends AbstractIC implements Triggable, Powerable {

    private static final ExpirableMap<Location, Integer> wavecount = new ExpirableMap<Location, Integer>(400, false, "BC7003");

    BC7003(Block block) {
        super(block);
    }

    @Override
    public void trigger() {

        // adding lever as output 0 (if not forced in constructor)
        this.AddOutputIO();

        // We treat the counter
        try {

            if (!this.decrementWaveCount()) {

                (new RemoveCount(ByteCart.plugin.Lockduration + 6, true, "Removecount")).reset(getLocation(), this.getOutput(0));
            }
        } catch (Exception e) {
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart : " + e.toString());

            e.printStackTrace();
        }
    }

    @Override
    public void power() {
        // check if we are really powered
        if (!this.getBlock().getRelative(MathUtil.clockwise(getCardinal())).isBlockPowered() && !this.getBlock().getRelative(MathUtil.anticlockwise(getCardinal())).isBlockPowered()) {
            return;
        }

        // add input command = redstone

        InputPin[] wire = new InputPin[2];

        // Right
        wire[0] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
        // left
        wire[1] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

        // InputRegistry[0] = detector
        this.addInputRegistry(new PinRegistry<InputPin>(wire));

        // Adding lever as output 0
        this.AddOutputIO();

        // if detector is on, the signal is red (= on)
        if (this.getInput(0).getAmount() != 0) {

            // setting red signal
            this.getOutput(0).setAmount(1);

            this.incrementWaveCount();
            (new RemoveCount(400, true, "Removecount")).reset(getLocation(), this.getOutput(0));
            wavecount.reset(getLocation(), this.getOutput(0));
        }
    }

    /**
     * increment the counter
     */
    private void incrementWaveCount() {
        synchronized (wavecount) {
            if (!wavecount.contains(this.getLocation())) {
                wavecount.put(getLocation(), 1);
//				if(ByteCart.debug)
//					ByteCart.log.info("ByteCart." + getName() + ": count = " + wavecount.getValue(getBlock()) + " init");
            } else {
//				if(ByteCart.debug)
//					ByteCart.log.info("ByteCart." + getName() + ": ++count = " + wavecount.getValue(getBlock()) + " before");
                wavecount.put(getLocation(), wavecount.get(getLocation()) + 1);
//				if(ByteCart.debug)
//					ByteCart.log.info("ByteCart." + getName() + ": ++count = " + wavecount.getValue(getBlock()) + " after");
            }
        }
    }

    /**
     * decrement the counter
     *
     * @return true if the counter is strictly positive
     */
    private boolean decrementWaveCount() {
        synchronized (wavecount) {
            if (wavecount.contains(getLocation()) && wavecount.get(getLocation()) > 1)
                wavecount.put(getLocation(), wavecount.get(getLocation()) - 1);

            else {
                wavecount.remove(getLocation());
//				if(ByteCart.debug)
//					ByteCart.log.info("ByteCart." + getName() + ": --count = 0");
                return false;
            }

//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart." + getName() + ": --count = " + wavecount.getValue(getBlock()));

            return true;
        }
    }

    /**
     * Add the lever behind the sign to give the red light signal
     */
    private void AddOutputIO() {
        // Declare red light signal = lever

        OutputPin[] lever = new OutputPin[1];

        // Right
        lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal(), 2));

        // OutputRegistry = red light signal
        this.addOutputRegistry(new PinRegistry<OutputPin>(lever));
    }

    @Override
    public final String getName() {
        return "BC7003";
    }

    @Override
    public final String getFriendlyName() {
        return "Cart counter";
    }

    @Override
    public boolean isTrain() {
        return false;
    }

    @Override
    public boolean wasTrain(Location loc) {
        return false;
    }

    @Override
    public boolean isLeverReversed() {
        return false;
    }

    /**
     * Runnable to remove the counter after a timeout
     */
    private static final class RemoveCount extends Expirable<Location> {

        public RemoveCount(long duration, boolean isSync, String name) {
            super(duration, isSync, name);
        }

        @Override
        public void expire(Object... objects) {
            ((RegistryOutput) objects[0]).setAmount(0);
        }
    }
}

