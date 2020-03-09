package nl.jboi.minecraft.bytecart.io;

import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;


/**
 * A Redstone wire
 */
class ComponentWire extends AbstractComponent implements InputPin, RegistryInput {

    /**
     * @param block the block containing the wire
     */
    ComponentWire(Block block) {
        super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Redstone wire at " + block.getLocation().toString());
*/
    }

    @Override
    public boolean read() {
        if (((AnaloguePowerable) this.getBlock().getBlockData()).getPower() != 0) {
//			if(ByteCart.debug)
//				ByteCart.log.info("Redstone wire on at (" + this.getBlock().getLocation().toString() + ")");
            return true;
        }
//		if(ByteCart.debug)
//			ByteCart.log.info("Redstone wire off at (" + this.getBlock().getLocation().toString() + ")");

        return false;
    }

    @Override
    public boolean getBit(int index) {
        final AnaloguePowerable wire = ((AnaloguePowerable) this.getBlock().getBlockData());
        return (wire.getPower() & 1 << (length() - index)) != 0;
    }

    @Override
    public int getAmount() {
        return ((AnaloguePowerable) this.getBlock().getBlockData()).getPower();
    }

    @Override
    public int length() {
        return 4;
    }


}
