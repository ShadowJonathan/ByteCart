package nl.jboi.minecraft.bytecart.commands;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.util.ModifiableRunnable;
import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressRouted;
import nl.jboi.minecraft.bytecart.api.ByteCartPlugin;
import nl.jboi.minecraft.bytecart.event.ByteCartInventoryListener;
import nl.jboi.minecraft.bytecart.sign.BC7011;
import nl.jboi.minecraft.bytecart.util.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class CommandSendTo extends AbstractTicketCommand implements CommandExecutor, TabCompleter {

    public CommandSendTo(ByteCartPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        } else {
            return parse(sender, (Player) sender, 0, args);
        }
    }

    @Override
    protected boolean run(CommandSender sender, Player player, String addressString, boolean isTrain) {
        player.sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.YELLOW + ByteCart.plugin.getConfig().getString("Info.RightClickCart"));
        new ByteCartInventoryListener(ByteCart.plugin, player, new MinecartSenderRunnable(player, addressString, isTrain), false);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabComplete(0, args);
    }

    private static final class MinecartSenderRunnable implements ModifiableRunnable<Inventory> {

        private final Player player;
        private final String address;
        private Inventory inventory;
        private boolean istrain;

        private MinecartSenderRunnable(Player player, String host_or_address, boolean isTrain) {
            this.player = player;
            this.address = host_or_address;
            this.istrain = isTrain;
        }

        @Override
        public void run() {
            if ((new BC7011(player.getLocation().getBlock(), ((Vehicle) inventory.getHolder()))).setAddress(address, null, this.istrain)) {
                LogUtil.sendSuccess(player, ByteCart.plugin.getConfig().getString("Info.SetAddress") + " " + address);
                LogUtil.sendSuccess(player, ByteCart.plugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL());
            } else
                LogUtil.sendError(player, ByteCart.plugin.getConfig().getString("Error.SetAddress"));
        }

        /**
         * @param inventory the inventory to set
         */

        @Override
        public void setParam(Inventory inventory) {
            this.inventory = inventory;
        }
    }
}
