package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.address.ReturnAddressFactory;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.address.Address;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * A return address setter
 */
final class BC7015 extends BC7011 implements Triggable {

    BC7015(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected AddressRouted getTargetAddress() {
        return ReturnAddressFactory.getAddress(this.getInventory());
    }

    @Override
    protected final boolean getIsTrain() {
        Address address;
        if ((address = AddressFactory.getAddress(this.getInventory())) != null)
            return address.isTrain();
        return false;
    }

    @Override
    public String getName() {
        return "BC7015";
    }

    @Override
    public String getFriendlyName() {
        return "Set Return";
    }

    @Override
    protected boolean forceTicketReuse() {
        return true;
    }

    @Override
    protected void infoPlayer(String address) {
        ((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetReturnAddress") + " " + ChatColor.RED + address);
    }
}
