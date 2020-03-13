package nl.jboi.minecraft.bytecart

import nl.jboi.minecraft.bytecart.api.ByteCartAPI
import nl.jboi.minecraft.bytecart.api.ByteCartPlugin
import nl.jboi.minecraft.bytecart.api.address.Resolver
import nl.jboi.minecraft.bytecart.collision.CollisionAvoiderManager
import nl.jboi.minecraft.bytecart.commands.*
import nl.jboi.minecraft.bytecart.data.IsTrainManager
import nl.jboi.minecraft.bytecart.event.ByteCartListener
import nl.jboi.minecraft.bytecart.event.ConstantSpeedListener
import nl.jboi.minecraft.bytecart.event.MinecartChunkListener
import nl.jboi.minecraft.bytecart.plugins.BCDynmapPlugin
import nl.jboi.minecraft.bytecart.plugins.BCHostnameResolutionPlugin
import nl.jboi.minecraft.bytecart.plugins.BCWandererTracker
import nl.jboi.minecraft.bytecart.util.MinecartChunkManager
import nl.jboi.minecraft.bytecart.wanderer.BCWandererManager
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.logging.Logger

/**
 * Main class
 */
class ByteCart : JavaPlugin(), ByteCartPlugin {
    @JvmField
    var Lockduration = 0
    private var hostnamePlugin: BCHostnameResolutionPlugin? = null
    private var chunkListener: MinecartChunkListener? = null
    private var cleanerTask = -1
    private var constantspeedlistener: ConstantSpeedListener? = null

    /**
     * @return the cam
     */
    var collisionAvoiderManager: CollisionAvoiderManager? = null
        private set
    private var wf: BCWandererManager? = null

    /**
     * @return the it
     */
    var isTrainManager: IsTrainManager? = null
        private set

    private var keepitems = false

    private var resolver: Resolver? = null
    override fun getResolver(): Resolver? = resolver
    override fun setResolver(resolver: Resolver?) {
        this.resolver = resolver
    }

    override fun onEnable() {
        plugin = this
        ByteCartAPI.setPlugin(this)

        saveDefaultConfig()
        loadConfig()

        setCam(CollisionAvoiderManager())
        setWf(BCWandererManager())
        setIt(IsTrainManager())
        server.pluginManager.registerEvents(ByteCartListener(), this)
        getCommand("mego")!!.setExecutor(CommandMeGo(this))
        getCommand("sendto")!!.setExecutor(CommandSendTo(this))
        getCommand("bcreload")!!.setExecutor(CommandBCReload())
        getCommand("bcupdater")!!.setExecutor(CommandBCUpdater())
        getCommand("bcticket")!!.setExecutor(CommandBCTicket(this))
        getCommand("bcback")!!.setExecutor(CommandBCBack())
        getCommand("bcdmapsync")!!.setExecutor(CommandBCDMapSync())

        if (Bukkit.getPluginManager().isPluginEnabled("dynmap") && this.config.getBoolean("dynmap", true)) {
            Companion.log.info("[ByteCart] loading dynmap plugin.")
            try {
                server.pluginManager.registerEvents(BCDynmapPlugin(), this)
            } catch (e: MissingResourceException) {
                Companion.log.info("[ByteCart] Failed to load dynmap plugin.")
            }
        }
        if (this.config.getBoolean("hostname_resolution", true)) {
            Companion.log.info("[ByteCart] loading BCHostname plugin.")
            hostnamePlugin = BCHostnameResolutionPlugin()
            try {
                hostnamePlugin!!.onLoad()
                ByteCartAPI.setResolver(hostnamePlugin)
                server.pluginManager.registerEvents(hostnamePlugin!!, this)
                getCommand("host")!!.setExecutor(hostnamePlugin)
            } catch (e: MissingResourceException) {
                Companion.log.info("[ByteCart] Failed to load BCHostname plugin.")
            }
        }
        val updatertrackerplugin = BCWandererTracker()
        server.pluginManager.registerEvents(updatertrackerplugin, this)
        getCommand("bctracker")!!.setExecutor(updatertrackerplugin)

        /* Uncomment to launch storage test
		Block block = this.getServer().getWorld("plat").getBlockAt(0, 61, 0);
		Inventory inventory = ((Chest)block.getState()).getInventory();
		(new FileStorageTest(inventory)).runTest();
		*/
        log.info("[ByteCart] plugin has been enabled.")
    }

    override fun onDisable() {
        Companion.log.info("Your plugin has been disabled.")
        plugin = null
        ByteCartAPI.setPlugin(null)
        Companion.log = null
    }

    /**
     * Load the configuration file
     */
    fun loadConfig() {
        debug = this.config.getBoolean("debug", false)
        keepitems = this.config.getBoolean("keepitems", true)
        Lockduration = this.config.getInt("Lockduration", 44)
        if (debug) {
            Companion.log.info("ByteCart : debug mode is on.")
        }
        if (this.config.getBoolean("loadchunks")) {
            if (chunkListener == null) {
                chunkListener = MinecartChunkListener()
                server.pluginManager.registerEvents(chunkListener!!, this)
            }
            if (cleanerTask == -1) {
                cleanerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
                    val cleaned = MinecartChunkManager.clean()
                    if (debug && cleaned > 0) Companion.log.warning("ByteCart : ChunkCleaner : cleaned $cleaned leftover chunks.")
                }, 1000, 200)
            }
        } else {
            if (chunkListener != null) {
                HandlerList.unregisterAll(chunkListener!!)
                chunkListener = null
            }
            if (cleanerTask != -1) {
                Bukkit.getScheduler().cancelTask(cleanerTask)
                cleanerTask = -1
            }
        }
        if (this.config.getBoolean("constantspeed", false)) {
            if (constantspeedlistener == null) {
                constantspeedlistener = ConstantSpeedListener()
                server.pluginManager.registerEvents(constantspeedlistener!!, this)
            }
        } else if (constantspeedlistener != null) {
            HandlerList.unregisterAll(constantspeedlistener!!)
            constantspeedlistener = null
        }
    }

    /**
     * @param cam the cam to set
     */
    private fun setCam(cam: CollisionAvoiderManager) {
        collisionAvoiderManager = cam
    }

    /**
     * @param it the it to set
     */
    private fun setIt(it: IsTrainManager) {
        isTrainManager = it
    }

    /**
     * @return true if we must keep items while removing carts
     */
    fun keepItems(): Boolean {
        return keepitems
    }

    /**
     * @return the wf
     */
    override fun getWandererManager(): BCWandererManager {
        return wf!!
    }

    /**
     * @param wf the wf to set
     */
    private fun setWf(wf: BCWandererManager) {
        this.wf = wf
    }

    companion object {
        @JvmField
        var log = Logger.getLogger("Minecraft")

        @JvmField
        var plugin: ByteCart? = null

        @JvmField
        var debug = false
    }
}