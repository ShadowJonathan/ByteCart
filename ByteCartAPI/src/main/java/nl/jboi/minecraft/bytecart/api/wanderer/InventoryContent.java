package nl.jboi.minecraft.bytecart.api.wanderer;

import nl.jboi.minecraft.bytecart.api.util.DirectionRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;
import java.util.Stack;


public interface InventoryContent extends Serializable {
    /**
     * @return the inventory
     */
    Inventory getInventory();

    /**
     * Get the level of the wanderer
     *
     * @return the level
     */
    Wanderer.Level getLevel();

    /**
     * Get the region of the wanderer
     *
     * @return the region
     */
    int getRegion();

    /**
     * @return the player that creates the wanderer
     */
    Player getPlayer();

    Counter getCounter();

    Stack<Integer> getStart();

    Stack<Integer> getEnd();

    boolean hasRouteTo(int ring);

    int getMinDistanceRing(
            RoutingTable routingTable,
            DirectionRegistry from);

    int getCurrent();

    void setCurrent(int i);

    int getMetric(int ring);

    void setRoute(int ring, int i);

    long getCreationtime();

    long getExpirationTime();

    void setExpirationTime(long lastupdate);
}
