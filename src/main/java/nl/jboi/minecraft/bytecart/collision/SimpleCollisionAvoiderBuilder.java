package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.sign.Triggable;
import org.bukkit.Location;

/**
 * A builder for simple collision avoider, i.e for a T cross-roads
 */
public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

    public SimpleCollisionAvoiderBuilder(Triggable ic, Location loc) {
        super(ic, loc);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends CollisionAvoider> T getCollisionAvoider() {
        return (T) new SimpleCollisionAvoider(this.ic, this.loc);
    }
}
