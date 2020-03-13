package nl.jboi.minecraft.bytecart.util

/**
 * Represents a runnable that can updates its internal parameter
 *
 * @param T Type of the internal parameter
 */
interface ModifiableRunnable<T> : Runnable {
    /**
     * Updates the parameter variable
     *
     * @param t Object to be set as parameter
     */
    fun setParam(t: T)
}