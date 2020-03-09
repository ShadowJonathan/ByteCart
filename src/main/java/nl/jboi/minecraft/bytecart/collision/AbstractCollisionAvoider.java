package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.hal.AbstractIC;
import nl.jboi.minecraft.bytecart.data.ExpirableMap;
import org.bukkit.Location;

/**
 * Abstract class for collision avoiders
 */
abstract class AbstractCollisionAvoider extends AbstractIC {

    /**
     * @param loc the location where the collision avoider will be attached
     */
    AbstractCollisionAvoider(org.bukkit.Location loc) {
        super(loc.getBlock());
    }

    /**
     * Get a map of locations that have been recently used
     *
     * @return the map
     */
    protected abstract ExpirableMap<Location, Boolean> getRecentlyUsedMap();

    /**
     * Get a map of locations that have the train flag
     *
     * @return the map
     */
    protected abstract ExpirableMap<Location, Boolean> getHasTrainMap();

    /**
     * Tell if this collision avoider has the train flag set
     *
     * @return true if the flag is set
     */
    protected boolean getHasTrain() {
        return this.getHasTrainMap().contains(getLocation());
    }

    /**
     * @param hasTrain the hasTrain to set
     */
    private void setHasTrain(boolean hasTrain) {
        this.getHasTrainMap().put(getLocation(), hasTrain);
    }

    /**
     * Tell if this collision avoider has been recently used
     *
     * @return true if recently used
     */
    protected boolean getRecentlyUsed() {
        return this.getRecentlyUsedMap().contains(getLocation());
    }

    /**
     * @param recentlyUsed the recentlyUsed to set
     */
    protected void setRecentlyUsed(boolean recentlyUsed) {
        this.getRecentlyUsedMap().put(getLocation(), recentlyUsed);
    }

    /**
     * {@link Router#Book(boolean)}
     */
    public void Book(boolean isTrain) {
        setRecentlyUsed(true);
        setHasTrain(this.getHasTrain() | isTrain);
    }

    @Override
    public final String getName() {
        return "Collision avoider";
    }

    @Override
    public final String getFriendlyName() {
        return getName();
    }

    /**
     * Relative direction for the router. BACK is the direction from where the cart is arriving
     */
    enum Side {
        BACK(0),
        LEFT(2),
        STRAIGHT(4),
        RIGHT(6);

        private int Value;

        Side(int b) {
            Value = b;
        }

        int Value() {
            return Value;
        }
    }

}
