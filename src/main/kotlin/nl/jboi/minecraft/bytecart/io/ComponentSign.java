package nl.jboi.minecraft.bytecart.io;

import nl.jboi.minecraft.bytecart.ByteCart;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

/**
 * A sign
 */
public final class ComponentSign extends AbstractComponent {

    /**
     * @param block the block containing the component
     */
    public ComponentSign(Block block) {
        super(block);
    }

    /**
     * Set a line of the sign
     *
     * @param line index of the line
     * @param s    the text to write
     */
    public void setLine(int line, String s) {
        final BlockState blockstate = this.getBlock().getState();

        if (blockstate instanceof Sign) {
            ((Sign) blockstate).setLine(line, s);
            blockstate.update();
        }
    }

    /**
     * Get a line of a sign
     *
     * @param line index of the line
     * @return the text
     */
    public String getLine(int line) {
        final BlockState blockstate = this.getBlock().getState();
        if (blockstate instanceof Sign)
            return ((Sign) blockstate).getLine(line);
        else {
            ByteCart.log.info("ByteCart: AddressSign cannot be built");
            throw new IllegalArgumentException();
        }
    }
}
