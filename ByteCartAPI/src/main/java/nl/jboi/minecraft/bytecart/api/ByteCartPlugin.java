package nl.jboi.minecraft.bytecart.api;

import nl.jboi.minecraft.bytecart.api.address.Resolver;
import nl.jboi.minecraft.bytecart.api.wanderer.WandererManager;

import java.util.logging.Logger;


public interface ByteCartPlugin {
    /**
     * @return the resolver registered
     */
    Resolver getResolver();

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    void setResolver(Resolver resolver);

    /**
     * Get the logger
     *
     * @return the logger
     */
    Logger getLog();

    /**
     * @return the wanderer factory
     */
    WandererManager getWandererManager();
}
