package nl.jboi.minecraft.bytecart.thread;

import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * HashMap that keeps entries during "duration" ticks
 */

public abstract class Expirable<K> {

    private final long Duration;
    private final String name;
    private final boolean IsSync;
    private Map<K, BukkitTask> ThreadMap = Collections.synchronizedMap(new HashMap<K, BukkitTask>());

    /**
     * @param duration the timeout value
     * @param isSync   true if the element must be removed synchronously in the main thread
     * @param name     a name for the set
     */
    public Expirable(long duration, boolean isSync, String name) {
        super();
        this.Duration = duration;
        this.name = name;
        this.IsSync = isSync;
    }

    public abstract void expire(Object... objects);

    public void reset(K key, Object... objects) {
        if (Duration != 0)
            (new BCBukkitRunnable<K>(this, key)).renewTaskLater(objects);
    }

    public void reset(long duration, K key, Object... objects) {
        if (duration != 0)
            (new BCBukkitRunnable<K>(this, key)).renewTaskLater(duration, objects);
    }

    public final void cancel(K key) {
        if (Duration != 0)
            (new BCBukkitRunnable<K>(this, key)).cancel();
    }

    protected final Map<K, BukkitTask> getThreadMap() {
        return ThreadMap;
    }

    public final long getDuration() {
        return Duration;
    }

    public final boolean isSync() {
        return IsSync;
    }

    public final String getName() {
        return name;
    }
}
