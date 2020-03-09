package nl.jboi.minecraft.bytecart.io;

import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.Switch.Face;

/**
 * A lever
 */
class ComponentLever extends AbstractComponent implements OutputPin, InputPin, RegistryInput {

    /**
     * @param block the block containing the component
     */
    ComponentLever(Block block) {
        super(block);
    }

    @Override
    public void write(boolean bit) {
        final Block block = this.getBlock();
        final BlockData md = block.getBlockData();
        if (md instanceof Switch) {
            Switch pw = (Switch) md;
            pw.setPowered(bit);
            block.setBlockData(pw);
            Face face = pw.getFace();
            switch (face) {
                case CEILING:
                    MathUtil.forceUpdate(block.getRelative(BlockFace.UP, 2));
                    break;
                case WALL:
                    MathUtil.forceUpdate(block.getRelative(pw.getFacing().getOppositeFace()));
                    break;
                default:
                    MathUtil.forceUpdate(block.getRelative(BlockFace.DOWN));
            }
        }
    }

    @Override
    public boolean read() {
        BlockData md = this.getBlock().getBlockData();
        if (md instanceof Powerable) {
            return ((Powerable) md).isPowered();
        }
        return false;
    }

    @Override
    public boolean getBit(int index) {
        return read();
    }

    @Override
    public int getAmount() {
        return (read() ? 15 : 0);
    }

    @Override
    public int length() {
        return 4;
    }


}
