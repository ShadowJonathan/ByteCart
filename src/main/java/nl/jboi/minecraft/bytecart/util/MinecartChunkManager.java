package nl.jboi.minecraft.bytecart.util;

import nl.jboi.minecraft.bytecart.ByteCart;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Map;

public final class MinecartChunkManager {
    private static final Vector NullVector = new Vector(0, 0, 0);

    public static int clean() {
        // todo add debug print
        int i = 0;
        for (World world : Bukkit.getWorlds()) {
            Map<Plugin, Collection<Chunk>> tickets = world.getPluginChunkTickets();
            if (tickets.containsKey(ByteCart.myPlugin)) {
                for (Chunk chunk : tickets.get(ByteCart.myPlugin)) {
                    if (checkAndUnregister(chunk)) {
                        i++;
                    }
                }
            }
        }
        return i;
    }

    public static boolean checkAndUnregister(Chunk chunk) {
        if (chunk.getPluginChunkTickets().contains(ByteCart.myPlugin) || !chunk.isLoaded())
            return false;

        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof Minecart && !entity.getVelocity().equals(NullVector)) {
                return false; // Call off the unload
            }
        }

        if (ByteCart.debug)
            ByteCart.log.info(String.format("ByteCart: ChunkManager : remove @ x%d z%d", chunk.getX(), chunk.getZ()));

        chunk.removePluginChunkTicket(ByteCart.myPlugin);
        return true;
    }

    public static void register(Chunk chunk) {
        if (chunk.getPluginChunkTickets().contains(ByteCart.myPlugin))
            return;

        if (ByteCart.debug)
            ByteCart.log.info(String.format("ByteCart: ChunkManager : add @ x%d z%d", chunk.getX(), chunk.getZ()));

        chunk.addPluginChunkTicket(ByteCart.myPlugin);
    }
}
