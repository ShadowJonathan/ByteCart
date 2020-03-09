package nl.jboi.minecraft.bytecart.routing;

import nl.jboi.minecraft.bytecart.api.ByteCartAPI;
import nl.jboi.minecraft.bytecart.api.wanderer.RouteValue;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * A track number on ByteCartAPI.MAXRINGLOG bits
 */
final class RouteNumber extends RoutingTableContent<RouteNumber>
        implements Comparable<RouteNumber>, Externalizable, RouteValue {

    private static final int rlength = ByteCartAPI.MAXRINGLOG;
    /**
     *
     */
    private static final long serialVersionUID = -8112012047943458459L;

    public RouteNumber() {
        super(rlength);
    }

    RouteNumber(int route) {
        super(route, rlength);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeShort(this.value());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setValue(in.readShort() & ((1 << rlength) - 1));
    }
}
