package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.sign.Triggable;
import nl.jboi.minecraft.bytecart.data.ExpirableMap;
import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide;
import nl.jboi.minecraft.bytecart.api.hal.RegistryOutput;
import org.bukkit.Location;

/**
 * A collision avoider for T cross-roads
 */
public class SimpleCollisionAvoider extends AbstractCollisionAvoider implements CollisionAvoider {

    private static final ExpirableMap<Location, Boolean> recentlyUsedMap = new ExpirableMap<Location, Boolean>(20, false, "recentlyUsed9000");
    private static final ExpirableMap<Location, Boolean> hasTrainMap = new ExpirableMap<Location, Boolean>(14, false, "hastrain");
    private final Location loc1;
    private RegistryOutput Lever1, Lever2, Active;
    private IntersectionSide.Side state;

    private boolean reversed;


    SimpleCollisionAvoider(Triggable ic, org.bukkit.Location loc) {
        super(loc);

        if (ByteCart.debug)
            ByteCart.log.info("ByteCart: new IntersectionSide() at " + loc);

        Lever1 = ic.getOutput(0);
        Active = Lever1;
        reversed = ic.isLeverReversed();
        loc1 = ic.getLocation();
        state = (Lever1.getAmount() == 0 ? IntersectionSide.Side.LEVER_OFF : IntersectionSide.Side.LEVER_ON);
    }

    /**
     * Ask for a direction, requesting a possible transition
     *
     * @param s       the direction where the cart goes to
     * @param isTrain true if it is a train
     * @return the direction actually taken
     */
    public IntersectionSide.Side WishToGo(IntersectionSide.Side s, boolean isTrain) {

        IntersectionSide.Side trueside = getActiveTrueSide(s);

        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : WishToGo to side " + trueside + " and isTrain is " + isTrain);
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : state is " + state);
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : recentlyUsed is " + this.getRecentlyUsed() + " and hasTrain is " + this.getHasTrain());
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : Lever1 is " + Lever1.getAmount());

        if (trueside != state
                && (Lever2 == null
                || (!this.getRecentlyUsed()) && !this.getHasTrain())) {
            Set(trueside);
        }
        this.setRecentlyUsed(true);
        return state;

    }

    /**
     * Get the fixed side of the active lever.
     * the second IC lever can be reversed
     *
     * @param s the original side
     * @return the fixed side
     */
    private IntersectionSide.Side getActiveTrueSide(IntersectionSide.Side s) {
        if (Active != Lever2)
            return s;
        return getSecondLeverSide(s);
    }

    /**
     * Get the fixed side of the second lever
     *
     * @param s the original side
     * @return the fixed side
     */
    private IntersectionSide.Side getSecondLeverSide(IntersectionSide.Side s) {
        return reversed ? s : s.opposite();
    }

    @Override
    public void Add(Triggable t) {
        if (t.getLocation().equals(loc1)) {
            Active = Lever1;
            return;
        }
        if (Lever2 != null) {
            Active = Lever2;
            return;
        }
        Lever2 = t.getOutput(0);
        Active = Lever2;
        reversed ^= t.isLeverReversed();
        Lever2.setAmount(getSecondLeverSide(state).Value());
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart: Add and setting lever2 to " + Lever2.getAmount());
    }

    /**
     * Activate levers. The 2 levers are in opposition
     *
     * @param s the side of the lever of the IC that created this collision avoider
     */
    private void Set(IntersectionSide.Side s) {
        this.Lever1.setAmount(s.Value());
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart: Setting lever1 to " + Lever1.getAmount());
        if (this.Lever2 != null) {
            this.Lever2.setAmount(getSecondLeverSide(state).Value());
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart: Setting lever2 to " + Lever2.getAmount());
        }
        state = s;
    }

    @Override
    public int getSecondpos() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ExpirableMap<Location, Boolean> getRecentlyUsedMap() {
        return recentlyUsedMap;
    }

    @Override
    protected ExpirableMap<Location, Boolean> getHasTrainMap() {
        return hasTrainMap;
    }


}
