package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.hal.IC;
import org.bukkit.event.Event;

/**
 * A container class for all events
 */
public abstract class BCEvent extends Event {
    private final IC ic;

    public BCEvent(IC ic) {
        super();
        this.ic = ic;
    }

    /**
     * @return the component
     */
    public IC getIc() {
        return ic;
    }
}
