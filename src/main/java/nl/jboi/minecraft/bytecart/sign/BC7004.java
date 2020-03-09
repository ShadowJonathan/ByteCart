package nl.jboi.minecraft.bytecart.sign;

import nl.jboi.minecraft.bytecart.address.AddressFactory;
import nl.jboi.minecraft.bytecart.address.AddressString;
import nl.jboi.minecraft.bytecart.address.TicketFactory;
import nl.jboi.minecraft.bytecart.api.address.Address;
import nl.jboi.minecraft.bytecart.api.util.MathUtil;
import nl.jboi.minecraft.bytecart.hal.AbstractIC;
import nl.jboi.minecraft.bytecart.hal.PinRegistry;
import nl.jboi.minecraft.bytecart.io.InputFactory;
import nl.jboi.minecraft.bytecart.io.InputPin;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * A cart spawner
 */
final class BC7004 extends AbstractIC implements Powerable {

    private final String type;
    private final String address;

    public BC7004(Block block, String type, String address) {
        super(block);
        this.type = type;
        this.address = address;
    }

    @Override
    public void power() {
        Block block = this.getBlock();
        // check if we are really powered
        if (!block.getRelative(MathUtil.clockwise(getCardinal())).isBlockPowered() && !block.getRelative(MathUtil.anticlockwise(getCardinal())).isBlockPowered()) {
            return;
        }

        // add input command = redstone

        InputPin[] wire = new InputPin[2];

        // Right
        wire[0] = InputFactory.getInput(block.getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
        // left
        wire[1] = InputFactory.getInput(block.getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

        // InputRegistry[0] = wire
        this.addInputRegistry(new PinRegistry<InputPin>(wire));

        // if wire is on, we spawn a cart
        if (this.getInput(0).getAmount() != 0) {
            Block rail = block.getRelative(BlockFace.UP, 2);
            Location loc = rail.getLocation();
            // check that it is a track, and no cart is there
            if ((rail.getBlockData() instanceof Rail) && MathUtil.getVehicleByLocation(loc) == null) {
                Entity entity = block.getWorld().spawnEntity(loc, getType());
                // put a ticket in the inventory if necessary
                if (entity instanceof InventoryHolder && AddressString.isResolvableAddressOrName(address)) {
                    Inventory inv = ((InventoryHolder) entity).getInventory();
                    TicketFactory.getOrCreateTicket(inv);
                    Address dst = AddressFactory.getAddress(inv);
                    dst.setAddress(address);
                    dst.finalizeAddress();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "BC7004";
    }

    @Override
    public String getFriendlyName() {
        return "Cart spawner";
    }

    /**
     * Get the type of cart to spawn
     *
     * @return the type
     */
    private EntityType getType() {
        if (type.equalsIgnoreCase("storage")) {
            return EntityType.MINECART_CHEST;
        }
        if (type.equalsIgnoreCase("furnace")) {
            return EntityType.MINECART_FURNACE;
        }
        if (type.equalsIgnoreCase("hopper")) {
            return EntityType.MINECART_HOPPER;
        }

        return EntityType.MINECART;
    }
}
