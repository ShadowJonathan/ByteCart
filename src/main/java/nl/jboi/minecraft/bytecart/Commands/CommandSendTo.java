package nl.jboi.minecraft.bytecart.Commands;

import nl.jboi.minecraft.bytecart.AddressLayer.AddressFactory;
import nl.jboi.minecraft.bytecart.AddressLayer.AddressRouted;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.EventManagement.ByteCartInventoryListener;
import nl.jboi.minecraft.bytecart.ModifiableRunnable;
import nl.jboi.minecraft.bytecart.Signs.BC7011;
import nl.jboi.minecraft.bytecart.Util.LogUtil;
import nl.jboi.minecraft.bytecart.api.ByteCartPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
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
        player.sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.RightClickCart"));
        new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, addressString, isTrain), false);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabComplete(0, args);
    }

    private static final class Execute implements ModifiableRunnable<Inventory> {

        private final Player player;
        private final String address;
        private Inventory inventory;
        private boolean istrain;


        private Execute(Player player, String host_or_address, boolean isTrain) {
            this.player = player;
            this.address = host_or_address;
            this.istrain = isTrain;
        }

        @Override
        public void run() {
            if ((new BC7011(player.getLocation().getBlock(), ((org.bukkit.entity.Vehicle) inventory.getHolder()))).setAddress(address, null, this.istrain)) {
                LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " " + address);
                LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL());
            } else
                LogUtil.sendError(player, ByteCart.myPlugin.getConfig().getString("Error.SetAddress"));

        }


        /**
         * @param inventory
         * @param inventory the inventory to set
         */

        @Override
        public void SetParam(Inventory inventory) {
            this.inventory = inventory;
        }

    }
}
