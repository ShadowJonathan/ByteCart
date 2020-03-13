package nl.jboi.minecraft.bytecart.sign;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A universal ticket spawner
 */
public class BC7011 extends BC7010 implements Triggable {

    public BC7011(Block block,
                  Vehicle vehicle) {
        super(block, vehicle);

        this.StorageCartAllowed = true;
    }

    @Override
    public String getName() {
        return "BC7011";
    }

    @Override
    public String getFriendlyName() {
        return "Storage Goto";
    }
}
