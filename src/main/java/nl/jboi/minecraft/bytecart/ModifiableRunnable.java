package nl.jboi.minecraft.bytecart;

/**
 * Represents a runnable that can updates its internal parameter
 *
 * @param <T> Type of the internal parameter
 */
public interface ModifiableRunnable<T> extends Runnable {

    /**
     * Updates the parameter variable
     *
     * @param t Object to be set as parameter
     */
    void SetParam(T t);
}
