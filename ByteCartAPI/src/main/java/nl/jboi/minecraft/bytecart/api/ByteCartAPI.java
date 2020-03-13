package nl.jboi.minecraft.bytecart.api;

import nl.jboi.minecraft.bytecart.api.address.Resolver;
import org.jetbrains.annotations.Nullable;


public final class ByteCartAPI {

    public static final int MAXSTATION = 256;
    public static final int MAXSTATIONLOG = 8;
    public static final int MAXRING = 2048;
    public static final int MAXRINGLOG = 11;
    @Nullable
    private static ByteCartPlugin plugin;

    /**
     * @return the plugin
     */
    @Nullable
    public static ByteCartPlugin getPlugin() {
        return plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    public static void setPlugin(@Nullable ByteCartPlugin plugin) throws IllegalStateException {
        if (ByteCartAPI.plugin != null && plugin != null) {
            throw new IllegalStateException("Cannot redefine singleton Plugin");
        }

        ByteCartAPI.plugin = plugin;
    }

    /**
     * @return the resolver registered
     */
    @Nullable
    public static Resolver getResolver() {
        if (plugin == null) return null;
        return plugin.getResolver();
    }

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    public static void setResolver(@Nullable Resolver resolver) throws NullPointerException {
        if (plugin == null) throw new NullPointerException("Cannot set resolver: Plugin is null");
        plugin.setResolver(resolver);
    }
}
