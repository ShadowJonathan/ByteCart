package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.hal.RegistryBoth;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.collision.CollisionAvoiderBuilder;
import nl.jboi.minecraft.bytecart.collision.SimpleCollisionAvoider;
import nl.jboi.minecraft.bytecart.collision.SimpleCollisionAvoiderBuilder;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * An abstract class for T-intersection signs
 */
abstract class AbstractSimpleCrossroad extends AbstractTriggeredSign implements BCSign {

    protected CollisionAvoiderBuilder builder;
    private AddressRouted destination;

    AbstractSimpleCrossroad(Block block,
                            Vehicle vehicle) {
        super(block, vehicle);
        builder = new SimpleCollisionAvoiderBuilder(this, block.getRelative(this.getCardinal(), 3).getLocation());
    }

    /**
     * Register the inputs and outputs
     */
    protected void addIO() {
        // Output[0] = 2 bits registry representing levers on the left and on the right of the sign
        OutputPin[] lever2 = new OutputPin[3];

        // Left
        lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
        // Right
        lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));
        // Back
        lever2[2] = OutputPinFactory.getOutput(this.getBlock().getRelative(this.getCardinal()));

        PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

        this.addOutputRegistry(command1);
    }

    protected final void addIOInv() {
        // Input[0] = destination region taken from Inventory, slot #0

        Address IPaddress = getDestinationAddress();

        if (IPaddress == null)
            return;

        RegistryInput slot2 = IPaddress.getRegion();

        this.addInputRegistry(slot2);

        // Input[1] = destination track taken from cart, slot #1

        RegistryInput slot1 = IPaddress.getTrack();

        this.addInputRegistry(slot1);

        // Input[2] = destination station taken from cart, slot #2

        RegistryBoth slot0 = IPaddress.getStation();

        this.addInputRegistry(slot0);
    }

    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // routing
        intersection.WishToGo(route(), false);
    }

    protected Side route() {
        return Side.LEVER_OFF;
    }

    @Override
    public void trigger() {
        try {

            this.addIO();

            SimpleCollisionAvoider intersection = ByteCart.plugin.getCollisionAvoiderManager().getCollisionAvoider(builder);

            if (!ByteCart.plugin.getWandererManager().isWanderer(getInventory())) {

                boolean isTrain = isTrain(getDestinationAddress());

                // if this is a cart in a train
                if (this.wasTrain(this.getLocation())) {
                    ByteCart.plugin.getIsTrainManager().getMap().reset(getBlock().getLocation());
                    intersection.Book(isTrain);
                    return;
                }

                // if this is the first car of a train
                // we keep it during 2 s
                if (isTrain) {
                    this.setWasTrain(this.getLocation(), true);
                }

                intersection.WishToGo(this.route(), isTrain);
                return;
            }

            manageWanderer(intersection);
        } catch (ClassCastException e) {
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart : " + e.toString());

            // Not the good blocks to build the signs
            return;
        } catch (NullPointerException e) {
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart : " + e.toString());
            e.printStackTrace();

            // there was no inventory in the cart
            return;
        }
    }

    protected final AddressRouted getDestinationAddress() {
        if (destination != null)
            return destination;
        return destination = AddressFactory.getAddress(this.getInventory());
    }

    @Override
    public Wanderer.Level getLevel() {
        return Wanderer.Level.LOCAL;
    }

    @Override
    public final Address getSignAddress() {
        return AddressFactory.getAddress(getBlock(), 3);
    }

    @Override
    public final Block getCenter() {
        return this.getBlock();
    }

    @Override
    public final String getDestinationIP() {
        Address ip;
        if ((ip = getDestinationAddress()) != null)
            return ip.toString();
        return "";
    }
}
