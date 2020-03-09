package nl.jboi.minecraft.bytecart.Routing;

import nl.jboi.minecraft.bytecart.api.HAL.RegistryBoth;
import nl.jboi.minecraft.bytecart.api.HAL.VirtualRegistry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A time metric
 */
class Delay implements Externalizable {

    /**
     *
     */
    private static final long serialVersionUID = 970735722356168776L;

    // hard coded value, if modified, change RouteProperty.writeObject() and readObject()
    private final RegistryBoth value = new VirtualRegistry(12);

    public Delay() {
    }

    Delay(int value) {
        this.value.setAmount(value);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeShort(this.getValue());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.value.setAmount(in.readUnsignedShort());
    }

    /**
     * @return the value
     */
    int getValue() {
        return value.getAmount();
    }
}
