package nl.jboi.minecraft.bytecart.io;

import org.bukkit.block.Block;

/**
 * Represents a component, i.e a lever, a button, etc.
 */
interface Component {
    /**
     * Get the block containing the component
     *
     * @return the block
     */
	Block getBlock();

}
