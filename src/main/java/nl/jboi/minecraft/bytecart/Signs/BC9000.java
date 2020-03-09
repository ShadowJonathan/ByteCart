package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.CollisionManagement.SimpleCollisionAvoider;
import nl.jboi.minecraft.bytecart.Updaters.UpdaterLocal;
import nl.jboi.minecraft.bytecart.api.Signs.Subnet;

import java.io.IOException;


/**
 * A simple intersection block with anticollision
 */
final class BC9000 extends AbstractSimpleCrossroad implements Subnet, Triggable {

    private final int netmask;

    BC9000(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 0;
    }

    @Override
    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // it's an updater, so let it choosing direction
        super.manageWanderer(intersection);

        if (ByteCart.myPlugin.getConfig().getBoolean("oldBC9000behaviour", true)) {
            UpdaterLocal updater;
            try {
                updater = (UpdaterLocal) ByteCart.myPlugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());

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
