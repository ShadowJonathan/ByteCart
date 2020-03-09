package nl.jboi.minecraft.bytecart.IO;

import nl.jboi.minecraft.bytecart.api.Util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;

import java.util.Map;

/**
 * this call represents a thread that powers off a button
 */
class SetButtonOff implements Runnable {

    private final Component component;
    private final Map<Location, Integer> ActivatedButtonMap;

    /**
     * @param component          the component to power off
     * @param ActivatedButtonMap a map containing the task id of current task
     */
    SetButtonOff(Component component, Map<Location, Integer> ActivatedButtonMap) {
        this.component = component;
        this.ActivatedButtonMap = ActivatedButtonMap;
    }

    @Override
    public void run() {

        final Block block = component.getBlock();

        if (block.getBlockData() instanceof Switch) {
            final Switch button = (Switch) block.getBlockData();

            button.setPowered(false);
            block.setBlockData(button);

            MathUtil.forceUpdate(block.getRelative(button.getFacing().getOppositeFace()));
        }

        ActivatedButtonMap.remove(block.getLocation());
    }
}
