package nl.jboi.minecraft.bytecart.Signs;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.Routing.RoutingTableFactory;
import nl.jboi.minecraft.bytecart.Routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.api.Signs.BCRouter;
import com.google.gson.JsonSyntaxException;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

final class BC8011 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {

    BC8011(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle, false);
    }

    protected RoutingTableWritable loadChest() {
        BlockState blockstate;
        if ((blockstate = getCenter().getState()) instanceof InventoryHolder) {
            // Loading inventory of chest at same level of the sign
            Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

            // Converting inventory in routing table
            try {
                return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
            } catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
                ByteCart.log.info("ByteCart: Error while loading chest at position " + this.getCenter().getLocation() + ". Please clean its content and run bcupdater region command.");
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return "BC8011";
    }

}
