package nl.jboi.minecraft.bytecart.data;

import nl.jboi.minecraft.bytecart.thread.Expirable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set in which elements are deleted after a timeout
 *
 * @param <K> the type of elements of the set
 */
public final class ExpirableSet<K> extends Expirable<K> {

    private final Set<K> Set = Collections.synchronizedSet(new HashSet<K>());

    public ExpirableSet(long duration, boolean isSync, String name) {
        super(duration, isSync, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void expire(Object... objects) {
        ((Set<K>) objects[1]).remove(objects[0]);
    }

    /**
     * Add an element to the set
     *
     * @param key the element to add
     * @return true if the element was added
     */
    public boolean add(K key) {
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart: create ephemeral key (" + key +") in " + this.getName() + " for " + this.getDuration() + " ticks");
        this.reset(key, key, Set);
        return Set.add(key);
    }

    @Override
    public void reset(K key, Object... objects) {
        super.reset(key, key, Set);
    }

    @Override
    public void reset(long duration, K key, Object... objects) {
        super.reset(duration, key, key, Set);
    }

    /**
     * Remove the element from the set
     *
     * @param key the element to remove
     */
    public final void remove(K key) {
        Set.remove(key);
        this.cancel(key);
    }

    /**
     * Tells if the set contains an element
     *
     * @param key the element to check
     * @return true if the element is in the set
     */
    public boolean contains(K key) {
        return Set.contains(key);
    }

    /**
     * Tells if the set is empty
     *
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return Set.isEmpty();
    }

    /**
     * Empty the set
     */
    public void clear() {
        Iterator<K> it = Set.iterator();
        while (it.hasNext())
            this.cancel(it.next());
        Set.clear();
    }

    public Iterator<K> getIterator() {
        return Set.iterator();
    }
}