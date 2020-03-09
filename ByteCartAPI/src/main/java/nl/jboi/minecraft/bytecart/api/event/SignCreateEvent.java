package nl.jboi.minecraft.bytecart.api.event;

import nl.jboi.minecraft.bytecart.api.hal.IC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;


/**
 * Event triggered when a player creates a valid sign.
 */
public class SignCreateEvent extends BCEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String[] strings;

    public SignCreateEvent(IC ic, Player player, String[] strings) {
        super(ic);
        this.player = player;
        this.strings = strings;
    }

    /**
     * Needed for Bukkit Event API usage
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return the player that created the block
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the lines of the sign
     */
    public String[] getStrings() {
        return strings;
    }
}
