package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.api.ByteCartAPI;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide;
import nl.jboi.minecraft.bytecart.api.collision.IntersectionSide.Side;
import nl.jboi.minecraft.bytecart.api.event.SignPostStationEvent;
import nl.jboi.minecraft.bytecart.api.event.SignPreStationEvent;
import nl.jboi.minecraft.bytecart.api.sign.Station;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.InputFactory;
import nl.jboi.minecraft.bytecart.io.InputPin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.io.IOException;

/**
 * A station sign
 */
public final class BC9001 extends AbstractBC9000 implements Station, Powerable, Triggable {

    BC9001(Block block, Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = ByteCartAPI.MAXSTATIONLOG;
    }

    @Override
    public void trigger() {
        try {

            Address sign = AddressFactory.getAddress(this.getBlock(), 3);

            this.addIO();

            // input[6] = redstone for "full station" signal

            InputPin[] wire = new InputPin[2];

            // Right
            wire[0] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.clockwise(getCardinal())));
            // left
            wire[1] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.anticlockwise(getCardinal())));

            // InputRegistry[0] = start/stop command
            this.addInputRegistry(new PinRegistry<InputPin>(wire));

            triggerBC7003();

            if (!ByteCart.plugin.getWandererManager().isWanderer(getInventory())) {

                // if this is a cart in a train
                if (this.wasTrain(this.getLocation())) {
                    ByteCart.plugin.getIsTrainManager().getMap().reset(getLocation());
                    //				this.getOutput(0).setAmount(3);	// push buttons
                    return;
                }

                // if this is the first car of a train
                // we keep the state during 2 s
                if (AbstractTriggeredSign.isTrain(getDestinationAddress())) {
                    this.setWasTrain(this.getLocation(), true);
                }

                this.route();

                if (this.isAddressMatching() && this.getName().equals("BC9001") && this.getInventory().getHolder() instanceof Player) {
                    ((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.GREEN + ByteCart.plugin.getConfig().getString("Info.Destination") + " " + this.getFriendlyName() + " (" + sign + ")");
                }
                return;
            }

            // it's an wanderer
            Wanderer wanderer;
            try {
                wanderer = ByteCart.plugin.getWandererManager().getFactory(this.getInventory()).getWanderer(this, this.getInventory());
                // here we perform wanderer action
                wanderer.doAction(IntersectionSide.Side.LEVER_OFF);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // routing
            this.getOutput(0).setAmount(0); // unpower levers
        } catch (ClassCastException e) {
            if (ByteCart.debug)
                ByteCart.log.info("ByteCart : " + e.toString());

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

    @Override
    public void power() {
        this.powerBC7003();
    }

    /**
     * Manage the red light signal when triggered
     */
    private void triggerBC7003() {
        (new BC7003(this.getBlock())).trigger();
    }

    /**
     * Manage the red light signal when powered
     */
    private void powerBC7003() {
        (new BC7003(this.getBlock())).power();
    }

    @Override
    protected Side route() {
        SignPreStationEvent event;
        SignPostStationEvent event1;
        // test if every destination field matches sign field
        if (this.isAddressMatching() && this.getInput(6).getAmount() == 0)
            event = new SignPreStationEvent(this, Side.LEVER_ON); // power levers if matching
        else
            event = new SignPreStationEvent(this, Side.LEVER_OFF); // unpower levers if not matching
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.getSide().equals(Side.LEVER_ON) && this.getInput(6).getAmount() == 0) {
            this.getOutput(0).setAmount(Side.LEVER_ON.Value()); // power levers if matching
            event1 = new SignPostStationEvent(this, Side.LEVER_ON);
        } else {
            this.getOutput(0).setAmount(0); // unpower levers if not matching
            event1 = new SignPostStationEvent(this, Side.LEVER_ON);
        }
        Bukkit.getServer().getPluginManager().callEvent(event1);
        return null;
    }

    @Override
    public final String getName() {
        return "BC9001";
    }

    @Override
    public String getFriendlyName() {
        return "Station";
    }

    @Override
    public final String getStationName() {
        return ((Sign) this.getBlock().getState()).getLine(2);
    }
}
