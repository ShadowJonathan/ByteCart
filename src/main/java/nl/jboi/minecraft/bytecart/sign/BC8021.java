package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.routing.RoutingTableFactory;
import nl.jboi.minecraft.bytecart.routing.RoutingTableWritable;
import nl.jboi.minecraft.bytecart.api.sign.BCRouter;
import com.google.gson.JsonSyntaxException;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

class BC8021 extends BC8020 implements BCRouter, Triggable, HasRoutingTable {

    BC8021(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
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
        return "BC8021";
    }

}
