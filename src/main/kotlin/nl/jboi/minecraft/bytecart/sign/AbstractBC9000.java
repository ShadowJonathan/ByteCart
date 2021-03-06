package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.event.SignPostSubnetEvent;
import nl.jboi.minecraft.bytecart.api.event.SignPreSubnetEvent;
import nl.jboi.minecraft.bytecart.api.hal.RegistryBoth;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.sign.HasNetmask;
import nl.jboi.minecraft.bytecart.api.sign.Subnet;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.collision.SimpleCollisionAvoider;
import nl.jboi.minecraft.bytecart.hal.SubRegistry;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

import java.io.IOException;

/**
 * An abstract class for all subnet class
 */
abstract class AbstractBC9000 extends AbstractSimpleCrossroad implements Subnet, HasNetmask {

    protected int netmask;

    AbstractBC9000(Block block,
                   Vehicle vehicle) {
        super(block, vehicle);
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

                Side result = intersection.WishToGo(this.route(), isTrain);
                SignPostSubnetEvent event = new SignPostSubnetEvent(this, result);
                Bukkit.getServer().getPluginManager().callEvent(event);
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

    @Override
    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // it's an updater, so let it choosing direction
        Wanderer wanderer;
        try {
            wanderer = ByteCart.plugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());

            // routing
            Side to = intersection.WishToGo(wanderer.giveSimpleDirection(), false);

            // here we perform routes update
            wanderer.doAction(to);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected Side route() {
        SignPreSubnetEvent event;
        AddressRouted dst = this.getDestinationAddress();
        int ttl;
        if (this.isAddressMatching() && (ttl = dst.getTTL()) != 0) {
            dst.updateTTL(ttl - 1);
            dst.finalizeAddress();
            event = new SignPreSubnetEvent(this, Side.LEVER_ON);
        } else
            event = new SignPreSubnetEvent(this, Side.LEVER_OFF);

        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.getSide();
    }

    /**
     * Get the first station of the subnet
     *
     * @param station a station number in the subnet
     * @return the first station number
     */
    private RegistryBoth applyNetmask(RegistryBoth station) {
        if (this.netmask < station.length())
            return new SubRegistry<RegistryBoth>(station, this.netmask, 0);
        return station;
    }

    /**
     * Tell if the address stored in the ticket is matching the subnet address stored in the IC
     *
     * @return true if the address is in the subnet
     */
    protected boolean isAddressMatching() {
        try {
            return this.getInput(2).getAmount() == this.getInput(5).getAmount()
                    && this.getInput(1).getAmount() == this.getInput(4).getAmount()
                    && this.getInput(0).getAmount() == this.getInput(3).getAmount();
        } catch (NullPointerException e) {
            // there is no address on sign
        }
        return false;
    }

    /*
     * Configures all IO ports of this sign.
     *
     * The following input pins are configured:
     * 0: vehicle region
     * 1: vehicle track
     * 2: vehicle station (w/ applied net mask)
     * 3: sign region
     * 4: sign track
     * 5: sign station (w/ applied net mask)
     *
     * The following output pins are configured:
     * 0: left lever
     * 1: right lever
     */

    @Override
    protected void addIO() {
        Address sign = this.getSignAddress();

        super.addIO();

        // Input[0] = destination region taken from Inventory, slot #0

        Address IPaddress = getDestinationAddress();

        if (IPaddress == null)
            return;

        RegistryInput slot2 = IPaddress.getRegion();

        this.addInputRegistry(slot2);

        // Input[1] = destination track taken from cart, slot #1

        RegistryInput slot1 = IPaddress.getTrack();

        this.addInputRegistry(slot1);

        // Input[2] = destination station taken from cart, slot #2, 6 bits

        RegistryBoth slot0 = IPaddress.getStation();

// We keep only the X most significant bits (netmask)

        slot0 = applyNetmask(slot0);

        this.addInputRegistry(slot0);

// Address is on a sign, line #3
        // Input[3] = region from sign, line #3, 6 bits registry
        // Input[4] = track from sign, line #3, 6 bits registry
        // Input[5] = station number from sign, line #0, 6 bits registry
        this.addAddressAsInputs(sign);
    }

    /**
     * Register the given address as an input of the IC
     * <p>
     * This method will register 3 inputs.
     *
     * @param addr the address to register
     */
    private void addAddressAsInputs(Address addr) {
        if (addr.isValid()) {
            RegistryInput region = addr.getRegion();
            this.addInputRegistry(region);

            RegistryInput track = addr.getTrack();
            this.addInputRegistry(track);

            RegistryBoth station = addr.getStation();
            station = applyNetmask(station);
            this.addInputRegistry(station);
        }
    }

    @Override
    public final int getNetmask() {
        return netmask;
    }
}
