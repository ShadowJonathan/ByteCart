package nl.jboi.minecraft.bytecart.api.address;

import java.util.List;

/**
 * Represent a class that can get an address from a name
 */
public interface Resolver {
    /**
     * Resolve the name
     *
     * @param name the name to resolve
     * @return the address corresponding to the name, or null
     */
    String resolve(String name);

    /**
     * Gets a list of all names
     *
     * @return all available names
     */
    List<String> getNames();

    /**
     * Gets a list of names starting with the given text
     *
     * @param prefix the starting text
     * @return All matching names
     */
    List<String> getMatchingNames(String prefix);
}
