package nl.jboi.minecraft.bytecart.commands;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.util.LogUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CommandBCReload implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ByteCart.myPlugin.reloadConfig();
        ByteCart.myPlugin.loadConfig();

        String s = "Configuration file reloaded.";

        if (!(sender instanceof Player)) {
            sender.sendMessage(s);
        } else {
            Player player = (Player) sender;
            LogUtil.sendError(player, s);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
