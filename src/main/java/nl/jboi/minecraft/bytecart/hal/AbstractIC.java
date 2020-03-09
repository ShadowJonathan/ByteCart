package nl.jboi.minecraft.bytecart.hal;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.io.ComponentSign;
import nl.jboi.minecraft.bytecart.api.hal.IC;
import nl.jboi.minecraft.bytecart.api.hal.RegistryInput;
import nl.jboi.minecraft.bytecart.api.hal.RegistryOutput;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;

import java.util.Map;
import java.util.WeakHashMap;


// All ICs must inherit from this class

/**
 * An abstract class implementing common methods for all ICs
 */
public abstract class AbstractIC implements IC {

    private static final Map<String, Boolean> icCache = new WeakHashMap<String, Boolean>();
    private static org.bukkit.Location emptyLocation = new org.bukkit.Location(null, 0, 0, 0);
    private final Block Block;
    private final org.bukkit.Location Location;
    private RegistryInput[] input = new RegistryInput[9];
    private int input_args = 0;
    private RegistryOutput[] output = new RegistryOutput[6];
    private int output_args = 0;
    public AbstractIC(Block block) {
        this.Block = block;
        if (block != null) {
            this.Location = block.getLocation();
        } else {
            this.Location = new org.bukkit.Location(null, 0, 0, 0);
        }
    }

    public static void removeFromCache(Block block) {
        icCache.remove(block.getLocation(emptyLocation).toString());
    }

    // This function checks if we have a ByteCart sign at this location
    public static boolean checkEligibility(Block b) {

        if (!(b.getState() instanceof Sign)) {
            return false;
        }

        Boolean ret;
        String s;
        if ((ret = icCache.get(s = b.getLocation(emptyLocation).toString())) != null)
            return ret;

        String line_content = ((Sign) b.getState()).getLine(1);

        if (ByteCart.myPlugin.getConfig().getBoolean("FixBroken18", false)) {
            if (ret = AbstractIC.checkLooseEligibility(line_content)) {
                (new ComponentSign(b)).setLine(1, "[" + line_content + "]");
            } else {
                ret = AbstractIC.checkEligibility(line_content);
            }
        } else {
            ret = AbstractIC.checkEligibility(line_content);
        }
        icCache.put(s, ret);
        return ret;
    }

    public static boolean checkEligibility(String s) {

        if (!(s.matches("^\\[BC[0-9]{4,4}\\]$"))) {
            return false;
        }

        return true;

    }

    private static boolean checkLooseEligibility(String s) {

        if (!(s.matches("^BC[0-9]{4,4}$"))) {
            return false;
        }

        return true;

    }

    @Override
    public String getFriendlyName() {
        return ((Sign) this.getBlock().getState()).getLine(2);
    }

    @Override
    public final void addInputRegistry(RegistryInput reg) {
        this.input[this.input_args++] = reg;
    }

    @Override
    public final void addOutputRegistry(RegistryOutput reg) {
        this.output[this.output_args++] = reg;
    }

    @Override
    public final RegistryInput getInput(int index) {
        return input[index];
    }

    @Override
    public final RegistryOutput getOutput(int index) {
        return output[index];
    }

    @Override
    public final BlockFace getCardinal() {
        final BlockData blockdata = this.getBlock().getState().getBlockData();
        if (blockdata instanceof Directional) {
            return ((Directional) blockdata).getFacing().getOppositeFace();
        }
        if (blockdata instanceof Rotatable) {
            BlockFace f = ((Rotatable) blockdata).getRotation().getOppositeFace();
            f = MathUtil.straightUp(f);
            if (f == BlockFace.UP) {
                ByteCart.log.severe("ByteCart: Tilted sign found at " + this.getLocation() + ". Please straight it up in the axis of the track");
                return null;
            }
            return f;
        }
        return null;
    }

    @Override
    public final Block getBlock() {
        return Block;
    }

    @Override
    public final String getBuildPermission() {
        return "bytecart." + getName();
    }

    @Override
    public final int getTriggertax() {
        return ByteCart.myPlugin.getConfig().getInt("usetax." + this.getName());
    }

    @Override
    public final int getBuildtax() {
        return ByteCart.myPlugin.getConfig().getInt("buildtax." + this.getName());
    }

    @Override
    public org.bukkit.Location getLocation() {
        return Location;
    }
}
