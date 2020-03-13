package nl.jboi.minecraft.bytecart.io;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A button
 */
class ComponentButton extends AbstractComponent implements OutputPin, InputPin {

    private static final Map<Location, Integer> ActivatedButtonMap = new ConcurrentHashMap<Location, Integer>();

    /**
     * @param block the block containing the component
     */
    protected ComponentButton(Block block) {
        super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Button at " + block.getLocation().toString());
*/
    }

    @Override
    public void write(boolean bit) {
        final Block block = this.getBlock();
        final BlockData blockdata = block.getBlockData();
        if (blockdata instanceof Switch) {
            final ComponentButton component = this;
            int id;

            final Switch button = (Switch) block.getBlockData();

            if (bit) {
                if (ActivatedButtonMap.containsKey(block)) {

                    // if button is already on, we cancel the scheduled thread
                    ByteCart.plugin.getServer().getScheduler().cancelTask(ActivatedButtonMap.get(block));

                    // and we reschedule one
                    id = ByteCart.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.plugin, new SetButtonOff(component, ActivatedButtonMap)
                            , 40);

                    // We update the HashMap
                    ActivatedButtonMap.put(block.getLocation(), id);
                } else {
                    // if button is off, we power the button
                    button.setPowered(true);
                    block.setBlockData(button);
                    MathUtil.forceUpdate(block.getRelative(button.getFacing().getOppositeFace()));

                    // delayed action to unpower the button after 2 s.

                    id = ByteCart.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.plugin, new SetButtonOff(component, ActivatedButtonMap)
                            , 40);

                    // We update the HashMap
                    ActivatedButtonMap.put(block.getLocation(), id);
                }
            }
        }
    }

    @Override
    public boolean read() {
        final BlockData md = this.getBlock().getBlockData();
        if (md instanceof Powerable) {
            return ((Powerable) md).isPowered();
        }
        return false;
    }
}
