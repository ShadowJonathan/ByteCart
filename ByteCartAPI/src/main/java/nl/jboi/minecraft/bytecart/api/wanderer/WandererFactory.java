package nl.jboi.minecraft.bytecart.api.wanderer;

import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Level;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public interface WandererFactory {

    /**
     * @return a new wanderer instance
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Wanderer getWanderer(BCSign bc, Inventory inv);

    void removeAllWanderers();

    void createWanderer(int id, Inventory inv, int region, Level level, Player player, boolean isfullreset, boolean isnew);

    boolean areAllRemoved();

    void updateTimestamp(InventoryContent routes);

    void destroyWanderer(Inventory inv);

}
