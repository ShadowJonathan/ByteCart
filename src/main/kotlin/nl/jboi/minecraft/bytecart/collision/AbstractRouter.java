package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.data.ExpirableMap;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.OutputPin;
import nl.jboi.minecraft.bytecart.io.OutputPinFactory;
import nl.jboi.minecraft.bytecart.sign.Triggable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

abstract class AbstractRouter extends AbstractCollisionAvoider implements Router {

    private static final ExpirableMap<Location, Boolean> recentlyUsedMap = new ExpirableMap<>(40, false, "recentlyUsedRouter");
    private static final ExpirableMap<Location, Boolean> hasTrainMap = new ExpirableMap<>(14, false, "hasTrainRouter");
    protected Map<Side, Side> FromTo = new ConcurrentHashMap<>();
    protected Map<Side, Set<Side>> Possibility = new ConcurrentHashMap<>();
    private BlockFace From;
    private int secondpos = 0;
    private int posmask = 255;

    private boolean IsOldVersion;

    AbstractRouter(BlockFace from, Location loc, boolean isOldVersion) {
        super(loc);
        this.setFrom(from);
        this.IsOldVersion = isOldVersion;
        this.addIO(from, loc.getBlock());
    }

    /**
     * Get the relative direction of an absolute direction with a specific origin
     *
     * @param from the origin axis
     * @param to   the absolute direction
     * @return the relative direction
     */
    private static Side getSide(BlockFace from, BlockFace to) {
        BlockFace t = to;
        if (from == t)
            return Side.BACK;
        t = turn(t);
        if (from == t)
            return Side.LEFT;
        t = turn(t);
        if (from == t)
            return Side.STRAIGHT;
        return Side.RIGHT;
    }

    /**
     * Get the next absolute direction on the left
     *
     * @param b the initial direction
     * @return the next direction
     */
    private static BlockFace turn(BlockFace b) {
        return MathUtil.anticlockwise(b);
    }

    /**
     * Bit-rotate a value on 8 bits
     *
     * @param value the value to rotate
     * @param d     the numbers of bits to shift left
     * @return the result
     */
    private static int leftRotate8(int value, int d) {
        int b = 8 - d;
        return value >> b | (value & (1 << b) - 1) << d;
    }

    @Override
    public void Add(Triggable t) {
        return;
    }

    /**
     * @return the isOldVersion
     */
    private boolean isIsOldVersion() {
        return IsOldVersion;
    }

    @Override
    public final BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain) {
        //		IntersectionSide sfrom = getSide(from);
        //		IntersectionSide sto = getSide(to);

        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : Router : coming from " + from + " going to " + to);
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Router : going to " + sto);
		 */
        Router ca = this;
		/*
		if(ByteCart.debug) {
			ByteCart.log.info("ByteCart : position found  " + ca.getClass().toString());
			ByteCart.log.info("ByteCart : Recently used ? " + recentlyUsed);
			ByteCart.log.info("ByteCart : hasTrain ? " + hasTrain );
			ByteCart.log.info("ByteCart : isTrain ? " + isTrain );
		}
		 */
        Side s = getSide(from, to);

        boolean cond = !this.getRecentlyUsed() && !this.getHasTrain();

        if (this.getPosmask() != 255 || cond) {

            switch (s) {
                case STRAIGHT:
                    ca = new StraightRouter(from, getLocation(), this.isIsOldVersion());
                    if (cond || this.ValidatePosition(ca))
                        break;
                case RIGHT:
                    ca = new RightRouter(from, getLocation(), this.isIsOldVersion());
                    if (cond || this.ValidatePosition(ca))
                        break;
                case LEFT:
                    ca = new LeftRouter(from, getLocation(), this.isIsOldVersion());
                    if (cond || this.ValidatePosition(ca))
                        break;
                case BACK:
                    ca = new BackRouter(from, getLocation(), this.isIsOldVersion());
                    if (cond || this.ValidatePosition(ca))
                        break;

                default:
                    ca = new LeftRouter(from, getLocation(), this.isIsOldVersion());
                    if (cond || this.ValidatePosition(ca))
                        break;
                    ca = this;
            }

			/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Router : position changed to " + ca.getClass().toString());
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Router : really going to " + ca.getTo());
			 */
            // save router in collision avoider map
            ByteCart.plugin.getCollisionAvoiderManager().setCollisionAvoider(this.getLocation(), ca);

            //activate primary levers
            ca.route(from);

            // activate secondary levers
            ca.getOutput(1).setAmount(ca.getSecondpos());
            ca.getOutput(2).setAmount(ca.getSecondpos());
        }

        ca.Book(isTrain);

        return ca.getTo();
    }

    @Override
    public final BlockFace getFrom() {
        return From;
    }

    /**
     * @param from the from to set
     */
    private void setFrom(BlockFace from) {
        From = from;
    }

    /**
     * Tell if a transition is necessary for the router to satisfy direction request
     *
     * @param ca the collision avoider against which to check the state
     * @return false if a transition is needed
     */
    private boolean ValidatePosition(Router ca) {
        Side side = getSide(this.getFrom(), ca.getFrom());

        if (ByteCart.debug) {
            ByteCart.log.info("ByteCart : pos value before rotation : " + Integer.toBinaryString(getSecondpos()));
            ByteCart.log.info("ByteCart : rotation of bits          : " + side.Value());
        }

        int value = AbstractRouter.leftRotate8(getSecondpos(), side.Value());
        int mask = AbstractRouter.leftRotate8(getPosmask(), side.Value());

        if (ByteCart.debug) {
            ByteCart.log.info("ByteCart : pos value after rotation  : " + Integer.toBinaryString(value));
            ByteCart.log.info("ByteCart : mask after rotation       : " + Integer.toBinaryString(mask));
        }

        ca.setSecondpos(value | ca.getSecondpos());
        ca.setPosmask(mask | ca.getPosmask());

        if (ByteCart.debug) {
            ByteCart.log.info("ByteCart : value after OR            : " + Integer.toBinaryString(ca.getSecondpos()));
            ByteCart.log.info("ByteCart : mask after OR             : " + Integer.toBinaryString(ca.getPosmask()));
            ByteCart.log.info("ByteCart : compatible ?              : " + (((value ^ ca.getSecondpos()) & mask) == 0));
        }
        return ((value ^ ca.getSecondpos()) & mask) == 0;
    }

    /**
     * Get the relative direction of an absolute direction
     *
     * @param to the absolute direction
     * @return the relative direction
     */
    @SuppressWarnings("unused")
    private Side getSide(BlockFace to) {
        return getSide(getFrom(), to);
    }

    /**
     * Registers levers as output
     *
     * @param from   the origin axis
     * @param center the center of the router
     */
    private void addIO(BlockFace from, Block center) {

        BlockFace face = from;
        BlockFace face_cw = MathUtil.clockwise(from);

        // Main output
        OutputPin[] main = new OutputPin[4];

        if (this.isIsOldVersion()) {

            /** fixme rework {@link nl.jboi.minecraft.bytecart.sign.BC7009#addIO}*/
            // East
            main[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST, 3).getRelative(BlockFace.SOUTH));
            // North
            main[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST, 3).getRelative(BlockFace.NORTH));
            // South
            main[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH, 3).getRelative(BlockFace.EAST));
            // West
            main[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH, 3).getRelative(BlockFace.WEST));
        } else {
            // East
            main[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH, 3).getRelative(BlockFace.EAST));
            // North
            main[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH, 3).getRelative(BlockFace.WEST));
            // South
            main[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST, 3).getRelative(BlockFace.SOUTH));
            // West
            main[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST, 3).getRelative(BlockFace.NORTH));
        }

        checkIOPresence(main);

        // output[0] is main levers
        this.addOutputRegistry(new PinRegistry<>(main));

        // Secondary output to make U-turn
        OutputPin[] secondary = new OutputPin[8];
        // Alternate secondary output to make U-turn (for new BC8011 router)
        OutputPin[] secondary_alt = new OutputPin[8];

        for (int i = 0; i < 7; i++) {
            // the first is Back
            secondary[i] = OutputPinFactory.getOutput(center.getRelative(face, 4).getRelative(face_cw, 2));
            secondary_alt[i] = OutputPinFactory.getOutput(center.getRelative(face, 3).getRelative(face_cw, 1));

            i++;
            secondary[i] = OutputPinFactory.getOutput(center.getRelative(face, 6));
            secondary_alt[i] = OutputPinFactory.getOutput(center.getRelative(face, 5).getRelative(face_cw, 1));

            face = face_cw;
            face_cw = MathUtil.clockwise(face_cw);
        }

        checkIOPresence(secondary, secondary_alt);

        // output[1] is second and third levers
        this.addOutputRegistry(new PinRegistry<>(secondary));

        // output[2] is alternate second and third levers
        this.addOutputRegistry(new PinRegistry<>(secondary_alt));
    }

    /**
     * Check if there are levers as expected
     *
     * @param pins     an array of levers
     * @param alt_pins alternative array of levers
     */
    private void checkIOPresence(OutputPin[] pins, OutputPin[] alt_pins) {
        for (int i = 0; i < pins.length; i++)
            if (pins[i] == null && alt_pins[i] == null) {
                ByteCart.log.log(Level.SEVERE, "ByteCart : Lever missing or wrongly positioned in router " + this.getLocation());
                throw new NullPointerException();
            }
    }

    /**
     * Check if there are levers as expected
     *
     * @param pins an array of levers
     */
    private void checkIOPresence(OutputPin[] pins) {
        for (OutputPin pin : pins)
            if (pin == null) {
                ByteCart.log.log(Level.SEVERE, "ByteCart : Lever missing or wrongly positioned in router " + this.getLocation());
                throw new NullPointerException();
            }
    }

    @Override
    public final int getSecondpos() {
        return secondpos;
    }

    @Override
    public final void setSecondpos(int secondpos) {
        this.secondpos = secondpos;
    }

    @Override
    public final int getPosmask() {
        return posmask;
    }

    @Override
    public final void setPosmask(int posmask) {
        this.posmask = posmask;
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
