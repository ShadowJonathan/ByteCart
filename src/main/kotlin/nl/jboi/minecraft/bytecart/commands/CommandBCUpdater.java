package nl.jboi.minecraft.bytecart.commands;

import com.google.common.collect.Lists;
import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.util.ModifiableRunnable;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.event.ByteCartInventoryListener;
import nl.jboi.minecraft.bytecart.event.ByteCartUpdaterMoveListener;
import nl.jboi.minecraft.bytecart.updater.UpdaterFactory;
import nl.jboi.minecraft.bytecart.util.LogUtil;
import nl.jboi.minecraft.bytecart.wanderer.BCWandererManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class CommandBCUpdater implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
            BCWandererManager wm = ByteCart.plugin.getWandererManager();
            wm.getFactory("Updater").removeAllWanderers();
            wm.unregister("Updater");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
        } else {
            Player player = (Player) sender;

            int region = 0;

            if (args.length == 0 || args.length > 4 || !Wanderer.Level.isMember(args[0].toLowerCase()))
                return false;

            if (args.length == 1
                    && !args[0].equalsIgnoreCase("backbone")
                    && !args[0].equalsIgnoreCase("reset_backbone"))
                return false;

            boolean full_reset = false;
            boolean isnew = false;

            if (!ByteCart.plugin.getWandererManager().isRegistered("Updater"))
                ByteCart.plugin.getWandererManager().register("Updater", new UpdaterFactory());

            if (args.length >= 2) {

                if (!args[0].equalsIgnoreCase("region")
                        && !args[0].equalsIgnoreCase("local")
                        && !args[0].equalsIgnoreCase("reset_region")
                        && !args[0].equalsIgnoreCase("reset_local"))
                    return false;

                region = Integer.parseInt(args[1]);

                if (region < 1 || region > 2047)
                    return false;

                if (args.length == 3) {
                    if (args[0].startsWith("reset")) {
                        if (args[2].equalsIgnoreCase("full"))
                            full_reset = true;
                        else
                            return false;
                    } else if (args[2].equalsIgnoreCase("new"))
                        isnew = true;
                    else
                        return false;
                }
            }

            final class UpdaterInventoryRunnable implements ModifiableRunnable<Inventory> {

                private final Player player;
                private final Wanderer.Level level;
                private final int region;
                private Inventory inventory;
                private boolean isfullreset;
                private boolean isnew;

                private UpdaterInventoryRunnable(Player player, Wanderer.Level level, int region, boolean isfullreset, boolean isnew) {
                    this.player = player;
                    this.level = level;
                    this.region = region;
                    this.isfullreset = isfullreset;
                    this.isnew = isnew;
                }

                @Override
                public void run() {
                    int id = ((StorageMinecart) inventory.getHolder()).getEntityId();
                    // register updater factory
                    final BCWandererManager wm = ByteCart.plugin.getWandererManager();
                    if (!wm.isRegistered("Updater"))
                        wm.register("Updater", new UpdaterFactory());
                    wm.getFactory("Updater").createWanderer(id, inventory, region, level, player, isfullreset, isnew);
                    if (!ByteCartUpdaterMoveListener.isExist()) {
                        Listener updatermove = new ByteCartUpdaterMoveListener();
                        ByteCart.plugin.getServer().getPluginManager().registerEvents(updatermove, ByteCart.plugin);
                        ByteCartUpdaterMoveListener.setExist(true);
                    }
                }

                /**
                 * @param t
                 * @param t the inventory to set
                 */

                @Override
                public void setParam(Inventory t) {
                    this.inventory = t;
                }
            }

            LogUtil.sendSuccess(player, ByteCart.plugin.getConfig().getString("Info.RightClickCart"));

            new ByteCartInventoryListener(ByteCart.plugin, player,
                    new UpdaterInventoryRunnable(player, Wanderer.Level.valueOf(args[0].toUpperCase()), region, full_reset, isnew),
                    true);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 1 && StringUtil.startsWithIgnoreCase("remove", args[0])) {
                return Lists.newArrayList("remove");
            } else {
                return Collections.emptyList();
            }
        }
        if (args.length == 1) {
            List<String> options = Lists.newArrayList("remove");
            for (Wanderer.Level level : Wanderer.Level.values()) {
                options.add(level.name);
            }
            return StringUtil.copyPartialMatches(args[0], options, Lists.newArrayList());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reset_backbone")) {
                if (StringUtil.startsWithIgnoreCase("full", args[1])) {
                    return Lists.newArrayList("full");
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("region") || args[0].equalsIgnoreCase("local")) {
                if (StringUtil.startsWithIgnoreCase("new", args[2])) {
                    return Lists.newArrayList("new");
                }
            } else if (args[0].equalsIgnoreCase("reset_region") || args[0].equalsIgnoreCase("reset_local")) {
                if (StringUtil.startsWithIgnoreCase("full", args[2])) {
                    return Lists.newArrayList("full");
                }
            }
        }
        return Collections.emptyList();
    }
}
