package nl.jboi.minecraft.bytecart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The metric component of a routing table entry
 */
public final class Metric implements Comparable<Metric>, Externalizable {

    /**
     *
     */
    private static final long serialVersionUID = 7625856925617432369L;
    private Delay delay;

    public Metric() {
    }

    Metric(Metric m) {
        this(m.delay.getValue());
    }

    public Metric(int delay) {
        this.delay = new Delay(delay);
    }

    @Override
    public int compareTo(Metric o) {
        return Integer.compare(value(), o.value());
    }

    /**
     * @return the value
     */
    public int value() {
        return delay.getValue();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        delay = (Delay) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(delay);
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
        if (!(o instanceof Metric))
            return false;

        return value() == ((Metric) o).value();
    }
}
