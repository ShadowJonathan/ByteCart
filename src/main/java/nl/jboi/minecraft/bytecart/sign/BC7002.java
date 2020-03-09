package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.hal.AbstractIC;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A cart detector
 */
final class BC7002 extends AbstractTriggeredSign implements Triggable {

    BC7002(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    public void trigger() {
        OutputPin[] lever = new OutputPin[1];

        // Right
        lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal()));

        // OutputRegistry[1] = red light signal
        this.addOutputRegistry(new PinRegistry<OutputPin>(lever));

        this.getOutput(0).setAmount(1);
        //		if(ByteCart.debug)
        //			ByteCart.log.info("ByteCart : BC7002 count 1");

//		ByteCart.myPlugin.getDelayedThreadManager().renew(getLocation(), 4, new Release(this));
        (new Release(this)).runTaskLater(ByteCart.myPlugin, 4);
    }

    @Override
    public final String getName() {
        return "BC7002";
    }

    @Override
    public final String getFriendlyName() {
        return "Cart detector";
    }

    private static final class Release extends BukkitRunnable {

        private final AbstractIC bc;

        public Release(AbstractIC bc) {
            this.bc = bc;
        }

        @Override
        public void run() {
            this.bc.getOutput(0).setAmount(0);
        }
    }
}