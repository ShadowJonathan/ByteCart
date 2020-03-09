package nl.jboi.minecraft.bytecart;

import nl.jboi.minecraft.bytecart.api.ByteCartAPI;
import nl.jboi.minecraft.bytecart.api.ByteCartPlugin;
import nl.jboi.minecraft.bytecart.api.address.Resolver;
import nl.jboi.minecraft.bytecart.collision.CollisionAvoiderManager;
import nl.jboi.minecraft.bytecart.commands.*;
import nl.jboi.minecraft.bytecart.data.IsTrainManager;
import nl.jboi.minecraft.bytecart.event.ByteCartListener;
import nl.jboi.minecraft.bytecart.event.ConstantSpeedListener;
import nl.jboi.minecraft.bytecart.event.MinecartChunkListener;
import nl.jboi.minecraft.bytecart.plugins.BCDynmapPlugin;
import nl.jboi.minecraft.bytecart.plugins.BCHostnameResolutionPlugin;
import nl.jboi.minecraft.bytecart.plugins.BCWandererTracker;
import nl.jboi.minecraft.bytecart.util.MinecartChunkManager;
import nl.jboi.minecraft.bytecart.wanderer.BCWandererManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.MissingResourceException;
import java.util.logging.Logger;

/**
 * Main class
 */
public final class ByteCart extends JavaPlugin implements ByteCartPlugin {

    public static Logger log = Logger.getLogger("Minecraft");
    public static ByteCart myPlugin;
    public static boolean debug;
    public int Lockduration;
    private BCHostnameResolutionPlugin hostnamePlugin;
    private MinecartChunkListener chunkListener;
    private int cleanerTask = -1;
    private ConstantSpeedListener constantspeedlistener;
    private CollisionAvoiderManager cam;
    private BCWandererManager wf;
    private IsTrainManager it;
    private boolean keepitems;
    private Resolver resolver;

    @Override
    public void onEnable() {

        myPlugin = this;

        ByteCartAPI.setPlugin(this);

        this.saveDefaultConfig();

        this.loadConfig();

        this.setCam(new CollisionAvoiderManager());
        this.setWf(new BCWandererManager());
        this.setIt(new IsTrainManager());

        getServer().getPluginManager().registerEvents(new ByteCartListener(), this);

        getCommand("mego").setExecutor(new CommandMeGo(this));
        getCommand("sendto").setExecutor(new CommandSendTo(this));
        getCommand("bcreload").setExecutor(new CommandBCReload());
        getCommand("bcupdater").setExecutor(new CommandBCUpdater());
        getCommand("bcticket").setExecutor(new CommandBCTicket(this));
        getCommand("bcback").setExecutor(new CommandBCBack());
        getCommand("bcdmapsync").setExecutor(new CommandBCDMapSync());

        if (Bukkit.getPluginManager().isPluginEnabled("dynmap") && this.getConfig().getBoolean("dynmap", true)) {
            log.info("[ByteCart] loading dynmap plugin.");
            try {
                getServer().getPluginManager().registerEvents(new BCDynmapPlugin(), this);
            } catch (MissingResourceException e) {
                log.info("[ByteCart] Failed to load dynmap plugin.");
            }
        }

        if (this.getConfig().getBoolean("hostname_resolution", true)) {
            log.info("[ByteCart] loading BCHostname plugin.");
            hostnamePlugin = new BCHostnameResolutionPlugin();
            try {
                hostnamePlugin.onLoad();
                ByteCartAPI.setResolver(hostnamePlugin);
                getServer().getPluginManager().registerEvents(hostnamePlugin, this);
                getCommand("host").setExecutor(hostnamePlugin);
            } catch (MissingResourceException e) {
                log.info("[ByteCart] Failed to load BCHostname plugin.");
            }
        }

        BCWandererTracker updatertrackerplugin = new BCWandererTracker();
        getServer().getPluginManager().registerEvents(updatertrackerplugin, this);
        getCommand("bctracker").setExecutor(updatertrackerplugin);

		/* Uncomment to launch storage test
		Block block = this.getServer().getWorld("plat").getBlockAt(0, 61, 0);
		Inventory inventory = ((Chest)block.getState()).getInventory();
		(new FileStorageTest(inventory)).runTest();
		*/

        log.info("[ByteCart] plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        log.info("Your plugin has been disabled.");

        myPlugin = null;
        ByteCartAPI.setPlugin(null);
        log = null;
    }

    /**
     * Load the configuration file
     */
    public final void loadConfig() {
        debug = this.getConfig().getBoolean("debug", false);
        keepitems = this.getConfig().getBoolean("keepitems", true);

        Lockduration = this.getConfig().getInt("Lockduration", 44);

        if (debug) {
            log.info("ByteCart : debug mode is on.");
        }

        if (this.getConfig().getBoolean("loadchunks")) {
            if (chunkListener == null) {
                chunkListener = new MinecartChunkListener();
                getServer().getPluginManager().registerEvents(chunkListener, this);
            }
            if (cleanerTask == -1) {
                cleanerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                    int cleaned = MinecartChunkManager.clean();
                    if (ByteCart.debug && cleaned > 0)
                        ByteCart.log.warning("ByteCart : ChunkCleaner : cleaned " + cleaned + " leftover chunks.");
                }, 1000, 200);
            }
        } else {
            if (chunkListener != null) {
                HandlerList.unregisterAll(chunkListener);
                chunkListener = null;
            }
            if (cleanerTask != -1) {
                Bukkit.getScheduler().cancelTask(cleanerTask);
                cleanerTask = -1;
            }
        }

        if (this.getConfig().getBoolean("constantspeed", false)) {
            if (constantspeedlistener == null) {
                constantspeedlistener = new ConstantSpeedListener();
                getServer().getPluginManager().registerEvents(constantspeedlistener, this);
            }
        } else if (constantspeedlistener != null) {
            HandlerList.unregisterAll(constantspeedlistener);
            constantspeedlistener = null;
        }
    }

    /**
     * @return the cam
     */
    public CollisionAvoiderManager getCollisionAvoiderManager() {
        return cam;
    }

    /**
     * @param cam the cam to set
     */
    private void setCam(CollisionAvoiderManager cam) {
        this.cam = cam;
    }

    /**
     * @return the it
     */
    public IsTrainManager getIsTrainManager() {
        return it;
    }

    /**
     * @param it the it to set
     */
    private void setIt(IsTrainManager it) {
        this.it = it;
    }

    /**
     * @return true if we must keep items while removing carts
     */
    public boolean keepItems() {
        return keepitems;
    }

    /**
     * @return the resolver registered
     */
    @Override
    public Resolver getResolver() {
        return resolver;
    }

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    @Override
    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public final Logger getLog() {
        return log;
    }

    /**
     * @return the wf
     */
    @Override
    public BCWandererManager getWandererManager() {
        return wf;
    }

    /**
     * @param wf the wf to set
     */
    private void setWf(BCWandererManager wf) {
        this.wf = wf;
    }
}
