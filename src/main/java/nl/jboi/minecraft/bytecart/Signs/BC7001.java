package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.HAL.PinRegistry;
import nl.jboi.minecraft.bytecart.IO.InputFactory;
import nl.jboi.minecraft.bytecart.IO.InputPin;
import nl.jboi.minecraft.bytecart.IO.OutputPin;
import nl.jboi.minecraft.bytecart.IO.OutputPinFactory;
import nl.jboi.minecraft.bytecart.api.Util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;

/**
 * this IC represents a stop/start block
 * it is commanded by a wire (like FalseBook 'station' block)
 * wire on => start or no velocity change
 * wire off => stop
 * it provides a busy bit with a lever on the block above the sign
 * lever off = block occupied and not powered
 * lever on = block free OR powered
 */
final class BC7001 extends AbstractTriggeredSign implements Triggable, Powerable {

    /**
     * Constructor : !! vehicle can be null !!
     */
    BC7001(org.bukkit.block.Block block, Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {

        // add output occupied line = lever

        OutputPin[] lever = new OutputPin[1];

        // Right
        lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal().getOppositeFace()));

        // OutputRegistry[0] = occupied signal
        this.addOutputRegistry(new PinRegistry<OutputPin>(lever));

        // here starts the action

        // is there a minecart above ?
        if (this.getVehicle() != null) {

            // add input command = redstone

            InputPin[] wire = new InputPin[2];

            // Right
            wire[0] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
            // left
            wire[1] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

            // InputRegistry[0] = start/stop command
            this.addInputRegistry(new PinRegistry<InputPin>(wire));

            // if the wire is on
            if (this.getInput(0).getAmount() > 0) {
                if (this.wasTrain(this.getLocation()))
                    ByteCart.myPlugin.getIsTrainManager().getMap().reset(this.getLocation());

                if (this.isTrain()) {
                    this.setWasTrain(this.getLocation(), true);
                }

                // the lever is on too
                //this.getOutput(0).setAmount(1);
                final BC7001 myBC7001 = this;

                ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
                            @Override
                            public void run() {

                                // we set busy
                                myBC7001.getOutput(0).setAmount(1);
                            }
                        }
                        , 5);


                // if the cart is stopped, start it
                if (this.getVehicle().getVelocity().equals(new Vector(0, 0, 0))) {
                    if (((Minecart) this.getVehicle()).getMaxSpeed() == 0)
                        ((Minecart) this.getVehicle()).setMaxSpeed(0.4d);
                    this.getVehicle().setVelocity((new Vector(this.getCardinal().getModX(), this.getCardinal().getModY(), this.getCardinal().getModZ())).multiply(ByteCart.myPlugin.getConfig().getDouble("BC7001.startvelocity")));
                }
            }

            // if the wire is off
            else {

                // stop the cart if this is not a train and tells to the previous block that we are stopped
                if (!this.wasTrain(getLocation())) {
                    // the lever is off
                    this.getOutput(0).setAmount(0);
                    this.getVehicle().setVelocity(new Vector(0, 0, 0));
                    ((Minecart) this.getVehicle()).setMaxSpeed(0d);
                    ByteCart.myPlugin.getIsTrainManager().getMap().remove(getBlock().getRelative(getCardinal().getOppositeFace(), 2).getLocation());
                } else
                    ByteCart.myPlugin.getIsTrainManager().getMap().reset(this.getLocation());

				/*
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: BC7001 : cart on stop at " + this.Vehicle.getLocation().toString());
				 */
            }
            // if this is the first car of a train
            // we keep it during 2 s
        }

        // there is no minecart above
        else {
            // the lever is on
            this.getOutput(0).setAmount(1);
        }

    }

    @Override
    public void power() {
        // power update

        // We need to find if a cart is stopped and set the member variable Vehicle
        org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP, 2);
        Location loc = block.getLocation();
        BlockData rail;

        // if the rail is in slope, the cart is 1 block up
        if ((rail = block.getState().getBlockData()) instanceof Rail
                && MathUtil.isOnSlope((Rail) rail)) {
            loc.add(0, 1, 0);
        }

        final Triggable bc = TriggeredSignFactory.getTriggeredIC(this.getBlock(), MathUtil.getVehicleByLocation(loc));

        if (bc != null) {
            ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
                        @Override
                        public void run() {
                            bc.trigger();
                        }
                    }
                    , 1);
        } else
            this.trigger();
    }

    @Override
    public final String getName() {
        return "BC7001";
    }

    @Override
    public final String getFriendlyName() {
        return "Stop/Start";
    }
}
