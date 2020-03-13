package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import nl.jboi.minecraft.bytecart.collision.SimpleCollisionAvoider;
import nl.jboi.minecraft.bytecart.updater.UpdaterLocal;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

import java.io.IOException;

/**
 * A simple intersection block with anticollision
 */
final class BC9000 extends AbstractSimpleCrossroad implements Subnet, Triggable {

    private final int netmask;

    BC9000(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 0;
    }

    @Override
    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // it's an updater, so let it choosing direction
        super.manageWanderer(intersection);

        if (ByteCart.plugin.getConfig().getBoolean("oldBC9000behaviour", true)) {
            UpdaterLocal updater;
            try {
                updater = (UpdaterLocal) ByteCart.plugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());

                // here we perform routes update
                updater.leaveSubnet();
                updater.save();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "BC9000";
    }

    @Override
    public String getFriendlyName() {
        return "Collision avoider";
    }

    /**
     * @return the netmask
     */
    @Override
    public final int getNetmask() {
        return netmask;
    }
}
