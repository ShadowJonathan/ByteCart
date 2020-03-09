package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressRouted;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.Routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.Wanderer.BCWandererManager;
import nl.jboi.minecraft.bytecart.api.AddressLayer.Address;
import nl.jboi.minecraft.bytecart.api.Signs.BCRouter;
import nl.jboi.minecraft.bytecart.api.Wanderer.AbstractWanderer;
import nl.jboi.minecraft.bytecart.api.Wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.api.Wanderer.Wanderer.Scope;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;


/**
 * An IC at the entry of a L2 router
 */
class BC8020 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {


    BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.IsTrackNumberProvider = false;
    }

    BC8020(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle, boolean isOldVersion) {
        super(block, vehicle, isOldVersion);
        this.IsTrackNumberProvider = false;
    }

    @Override
    protected boolean selectWanderer() {
        final BCWandererManager wm = ByteCart.myPlugin.getWandererManager();
        return (!wm.isWanderer(this.getInventory()))
                || wm.isWanderer(this.getInventory(), Scope.LOCAL);
    }

    @Override
    protected BlockFace SelectRoute(AddressRouted address, Address sign, RoutingTableWritable RoutingTable) {

        try {
            if (address.getTTL() != 0) {
                // lookup destination region
                return RoutingTable.getDirection(address.getRegion().getAmount());
            }
        } catch (NullPointerException e) {
        }

        // if TTL reached end of life and is not returnable, then we lookup region 0
        try {
            return RoutingTable.getDirection(0);
        } catch (NullPointerException e) {
        }

        // If everything has failed, then we randomize output direction
        return AbstractWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());

    }

    @Override
    public Wanderer.Level getLevel() {
        return Wanderer.Level.BACKBONE;
    }

    @Override
    public String getName() {
        return "BC8020";
    }

    @Override
    public String getFriendlyName() {
        return "L2 Router";
    }
}
