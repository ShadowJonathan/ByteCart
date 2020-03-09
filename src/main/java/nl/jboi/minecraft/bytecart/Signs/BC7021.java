package nl.jboi.minecraft.bytecart.Signs;

/**
 * Power the lever on the train head but not on wagons
 */
final class BC7021 extends BC7020 implements Triggable {

    BC7021(org.bukkit.block.Block block,
           org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    @Override
    protected void actionWagon() {
        this.getOutput(0).setAmount(0);    // deactivate levers
    }

    @Override
    public final String getName() {
        return "BC7021";
    }

    @Override
    public final String getFriendlyName() {
        return "Train head";
    }

}
