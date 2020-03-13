package nl.jboi.minecraft.bytecart.api;

import nl.jboi.minecraft.bytecart.api.address.Resolver;
import nl.jboi.minecraft.bytecart.api.wanderer.WandererManager;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;


public interface ByteCartPlugin {
    /**
     * @return the resolver registered
     */
    @Nullable
    Resolver getResolver();

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    void setResolver(@Nullable Resolver resolver);

    /**
     * @return the wanderer factory
     */
    WandererManager getWandererManager();
}
