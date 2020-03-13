package nl.jboi.minecraft.bytecart.routing;

import nl.jboi.minecraft.bytecart.api.hal.RegistryBoth;
import nl.jboi.minecraft.bytecart.api.hal.VirtualRegistry;

/**
 * A raw routing table entry, i.e a registry
 *
 * @param <T> the type of content that will be stored
 */
abstract class RoutingTableContent<T extends RoutingTableContent<T>> implements Comparable<T> {

    private final int length;
    private final RegistryBoth data;

    RoutingTableContent(int length) {
        this.length = length;
        data = new VirtualRegistry(length);
    }

    RoutingTableContent(int amount, int length) {
        this(length);
        this.data.setAmount(amount);
    }

    /**
     * @return the size of the registry in bits
     */
    final int size() {
        return length;
    }

    /**
     * @param amount the value to store
     */
    protected void setValue(int amount) {
        data.setAmount(amount);
    }

    /**
     * @return the value stored
     */
    public final int value() {
        return data.getAmount();
    }

    @Override
    public int compareTo(T o) {
        return Integer.compare(value(), o.value());
    }

    @Override
    public int hashCode() {
        return value();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (!(o instanceof RoutingTableContent<?>))
            return false;

        return value() == ((RoutingTableContent<?>) o).value();
    }
}