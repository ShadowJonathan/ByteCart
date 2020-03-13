package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.address.ReturnAddressFactory;
import nl.jboi.minecraft.bytecart.api.address.Address;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

/**
 * A block that makes the cart return to its origin using return address
 */
public final class BC7017 extends AbstractTriggeredSign implements Triggable {

    BC7017(Block block,
           Vehicle vehicle) {
        super(block, vehicle);
    }

    public BC7017(Block block, Player player) {
        super(block, null);
        this.setInventory(player.getInventory());
    }

    @Override
    public String getName() {
        return "BC7017";
    }

    @Override
    public String getFriendlyName() {
        return "Return back";
    }

    @Override
    public void trigger() {
        Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());

        if (returnAddress == null || !returnAddress.isReturnable())
            return;

        String returnAddressString = returnAddress.toString();
        AddressRouted targetAddress = AddressFactory.getAddress(getInventory());
        if (ByteCart.debug)
            ByteCart.log.info("ByteCart: 7017 : Writing address " + returnAddressString);
        returnAddress.remove();
        returnAddress.finalizeAddress();
        boolean isTrain = targetAddress.isTrain();
        targetAddress.setAddress(returnAddressString);
        targetAddress.setTrain(isTrain);
        if (this.getInventory().getHolder() instanceof Player)
            ((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.YELLOW + ByteCart.plugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + returnAddressString + ")");
        targetAddress.initializeTTL();
        targetAddress.finalizeAddress();
    }
}
