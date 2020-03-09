package nl.jboi.minecraft.bytecart.collision;

import nl.jboi.minecraft.bytecart.sign.Triggable;
import org.bukkit.Location;

/**
 * A builder for router collision avoider
 */
public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

    private boolean IsOldVersion;

    public RouterCollisionAvoiderBuilder(Triggable ic, Location loc, boolean isOldVersion) {
        super(ic, loc);
        this.IsOldVersion = isOldVersion;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CollisionAvoider> T getCollisionAvoider() {
        return (T) new StraightRouter(this.ic.getCardinal().getOppositeFace(), this.loc, this.IsOldVersion);
    }


}
