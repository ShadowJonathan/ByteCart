package nl.jboi.minecraft.bytecart.plugins;

import nl.jboi.minecraft.bytecart.Util.LogUtil;
import nl.jboi.minecraft.bytecart.api.Event.UpdaterCreateEvent;
import nl.jboi.minecraft.bytecart.api.Event.UpdaterMoveEvent;
import nl.jboi.minecraft.bytecart.api.Event.UpdaterRemoveEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BCWandererTracker implements Listener, CommandExecutor, TabCompleter {
    private Map<Integer, Location> locations = new HashMap<>();

    @EventHandler
    public void onUpdaterCreate(UpdaterCreateEvent event) {
        locations.put(event.getVehicleId(), event.getLocation());
    }

    @EventHandler
    public void onUpdaterMove(UpdaterMoveEvent event) {
        VehicleMoveEvent e = event.getEvent();
        locations.put(e.getVehicle().getEntityId(), e.getTo());
    }

    @EventHandler
    public void onUpdaterRemove(UpdaterRemoveEvent event) {
        locations.remove(event.getVehicleId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (locations.isEmpty()) {
            LogUtil.sendSuccess(sender, "No updaters found");
            return true;
        }
        sender.sendMessage("List of locations of updaters:");
        for (Location loc : locations.values()) {
            String s = "World: " + loc.getWorld().getName() + " " +
                    "X: " + loc.getBlockX() + " " +
                    "Y: " + loc.getBlockY() + " " +
                    "Z: " + loc.getBlockZ();
            LogUtil.sendSuccess(sender, s);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
