package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.address.ReturnAddressFactory;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.collision.CollisionAvoiderBuilder;
import nl.jboi.minecraft.bytecart.collision.Router;
import nl.jboi.minecraft.bytecart.collision.RouterCollisionAvoiderBuilder;
import nl.jboi.minecraft.bytecart.routing.RoutingTableFactory;
import nl.jboi.minecraft.bytecart.routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.event.SignPostRouteEvent;
import nl.jboi.minecraft.bytecart.api.event.SignPreRouteEvent;
import nl.jboi.minecraft.bytecart.api.event.UpdaterPassRouterEvent;
import nl.jboi.minecraft.bytecart.api.sign.BCRouter;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.api.wanderer.AbstractWanderer;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import com.google.gson.JsonSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;


/**
 * An IC at the entry of a L1 router
 */
public class BC8010 extends AbstractTriggeredSign implements BCRouter, Triggable, HasRoutingTable {

    private final BlockFace From;
    private final Address Sign;
    private final RoutingTableWritable RoutingTable;
    private final Block center;
    protected boolean IsTrackNumberProvider;
    private AddressRouted destination;
    private boolean IsOldVersion = true;

    BC8010(Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.IsTrackNumberProvider = true;
        From = this.getCardinal().getOppositeFace();
        // reading destination address of the cart
        if (selectWanderer()) {
            destination = AddressFactory.getAddress(this.getInventory());
            if (destination == null) {
                destination = AddressFactory.getDefaultTicket(this.getInventory());
            }
        }
        // reading address written on BC8010 sign
        Sign = AddressFactory.getAddress(this.getBlock(), 3);
        // Center of the router, at sign level
        center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));

        this.RoutingTable = loadChest();
    }

    BC8010(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle, boolean isOldVersion) {
        this(block, vehicle);
        this.IsOldVersion = isOldVersion;
    }

    protected RoutingTableWritable loadChest() {
        BlockState blockstate;
        if ((blockstate = center.getRelative(BlockFace.UP, 5).getState()) instanceof InventoryHolder) {
            // Loading inventory of chest above router
            Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

            // Converting inventory in routing table
            try {
                return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
            } catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
                ByteCart.log.info("ByteCart: Error while loading chest at position " + this.center.getLocation() + ". Please clean its content and run bcupdater region command.");
                return null;
            }
        } else if ((blockstate = center.getRelative(BlockFace.DOWN, 2).getState()) instanceof InventoryHolder) {
            // Loading inventory of chest above router
            Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

            // Converting inventory in routing table
            try {
                return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
            } catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
                ByteCart.log.info("ByteCart: Error while loading chest at position " + this.center.getLocation() + ". Please clean its content and run bcupdater region command.");
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void trigger() {

        CollisionAvoiderBuilder builder = new RouterCollisionAvoiderBuilder(this, center.getLocation(), this.IsOldVersion);

        try {

            BlockFace direction, to;
            Router router = ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder);
            boolean isTrain = isTrain(destination);

            // Here begins the triggered action
            if (ByteCart.debug) {
                ByteCart.log.info("Router world: " + center.getWorld().getName());
                ByteCart.log.info("Router location: X: " + center.getX() + " Y: " + center.getY() + " Z: " + center.getZ());
            }


            // is this an wanderer who needs special routing ? no then routing normally
            if (selectWanderer()) {

                // if this is a cart in a train
                if (this.wasTrain(this.getLocation())) {

                    // leave a message to next cart that it is a train
                    ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
                    // tell to router not to change position
                    ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder).Book(isTrain);
                    return;
                }

                if (destination != null) {
                    // Time-to-live management

                    //loading TTl of cart
                    int ttl = destination.getTTL();

                    // if ttl did not reach end of life ( = 0)
                    if (ttl != 0) {

                        destination.updateTTL(ttl - 1);
                    }

                    // if ttl was 1 (now 0), we try to return the cart to source station

                    if (ttl == 1 && tryReturnCart())
                        destination = AddressFactory.getAddress(this.getInventory());

                    if (ByteCart.debug) {
                        ByteCart.log.info("ByteCart : TTL is " + destination.getTTL());
                    }


                    // if this is the first car of a train
                    // we keep it during 2 s
                    if (isTrain) {
                        this.setWasTrain(this.getLocation(), true);
                    }

                    destination.finalizeAddress();
                }

                direction = this.SelectRoute(destination, Sign, RoutingTable);

                // trigger event
                BlockFace bdest = router.WishToGo(From, direction, isTrain);
                int ring = this.getRoutingTable().getDirectlyConnected(bdest);

                SignPostRouteEvent event = new SignPostRouteEvent(this, ring);
                Bukkit.getServer().getPluginManager().callEvent(event);

                return;
            }

            // it's a wanderer, so let it choosing direction
            Wanderer wanderer = null;
            try {
                wanderer = getWanderer();
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // routing normally
            to = router.WishToGo(From, wanderer.giveRouterDirection(), isTrain);

            if (ByteCart.myPlugin.getWandererManager().isWanderer(getInventory(), "Updater")) {
                int nextring = this.getRoutingTable().getDirectlyConnected(to);
                UpdaterPassRouterEvent event = new UpdaterPassRouterEvent(wanderer, to, nextring);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }

            // here we perform routes update
            wanderer.doAction(to);

        } catch (ClassCastException e) {
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart : " + e.toString());
            e.printStackTrace();

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

    /**
     * Tells if this cart needs normal routing
     *
     * @return true if the cart needs normal routing
     */
    protected boolean selectWanderer() {
        // everything that is not an wanderer must be routed
        return !ByteCart.myPlugin.getWandererManager().isWanderer(getInventory());
    }

    /**
     * Compute the direction to take
     *
     * @param address            the destination address
     * @param sign                 the BC sign
     * @param RoutingTable the routing table contained in the chest
     * @return the direction to destination, or to ring 0. If ring 0 does not exist, random direction
     */
    protected BlockFace SelectRoute(AddressRouted address, Address sign, RoutingTableWritable RoutingTable) {

        BlockFace face;
        // same region : lookup destination track
        if (address != null && address.getRegion().getAmount() == sign.getRegion().getAmount() && address.getTTL() != 0) {
            int destination = this.destination.getTrack().getAmount();
            BlockFace out = RoutingTable.getAllowedDirection(destination);
            if (out != null) {
                // trigger event
                SignPreRouteEvent event = new SignPreRouteEvent(this, this.getRoutingTable().getDirectlyConnected(out));
                Bukkit.getServer().getPluginManager().callEvent(event);
                return RoutingTable.getDirection(event.getTargetTrack());
            }
        }

        // If not in same region, or if TTL is 0, or the ring does not exist then we lookup track 0
        if ((face = RoutingTable.getAllowedDirection(0)) != null)
            return face;

        // If everything has failed, then we randomize output direction
        return AbstractWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());
    }

    /**
     * Try to send the cart to its return address
     *
     * @return true if success
     */
    private boolean tryReturnCart() {
        Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());
        if (returnAddress != null && returnAddress.isReturnable()) {
            (new BC7017(this.getBlock(), this.getVehicle())).trigger();
            return true;
        }
        return false;
    }

    /**
     * Get the wanderer object
     *
     * @return the wanderer
     */
    private Wanderer getWanderer() throws ClassNotFoundException, IOException {
        return ByteCart.myPlugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());
    }

    @Override
    public Wanderer.Level getLevel() {
        return Wanderer.Level.REGION;
    }

    /**
     * Return the direction from where the cart is coming
     *
     * @return the direction
     */
    @Override
    public final BlockFace getFrom() {
        return From;
    }

    @Override
    public final Address getSignAddress() {
        return Sign;
    }

    @Override
    public final RoutingTableWritable getRoutingTable() {
        return RoutingTable;
    }

    /**
     * Tell if this IC will provide track numbers during configuration
     *
     * @return true if the IC provides track number
     */
    public final boolean isTrackNumberProvider() {
        return IsTrackNumberProvider;
    }

    @Override
    public final String getDestinationIP() {
        return destination.toString();
    }

    @Override
    public final int getOriginTrack() {
        return Sign.getTrack().getAmount();
    }

    @Override
    public final Block getCenter() {
        return center;
    }

    @Override
    public String getName() {
        return "BC8010";
    }

    @Override
    public String getFriendlyName() {
        return "L1 Router";
    }
}
