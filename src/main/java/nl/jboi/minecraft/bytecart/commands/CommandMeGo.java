package nl.jboi.minecraft.bytecart.commands;

import nl.jboi.minecraft.bytecart.sign.BC7010;
import nl.jboi.minecraft.bytecart.api.ByteCartPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandMeGo extends AbstractTicketCommand implements CommandExecutor, TabCompleter {

    public CommandMeGo(ByteCartPlugin plugin) {
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
        (new BC7010(player.getLocation().getBlock(), player)).setAddress(addressString, null, isTrain);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabComplete(0, args);
    }
}
