package nl.jboi.minecraft.bytecart.wanderer;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.api.wanderer.InventoryContent;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Level;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Scope;
import nl.jboi.minecraft.bytecart.api.wanderer.WandererFactory;
import nl.jboi.minecraft.bytecart.api.wanderer.WandererManager;
import nl.jboi.minecraft.bytecart.file.InventoryFile;
import nl.jboi.minecraft.bytecart.util.LogUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BCWandererManager implements WandererManager {

    private static final Map<String, WandererFactory> map = new HashMap<String, WandererFactory>();

    private static InventoryFile createWanderer(Inventory inv, int region, Level level, Player player, String name, Level type) throws IOException {
        InventoryFile file = new InventoryFile(inv, true, name);
        String dot = ".";
        StringBuilder match = new StringBuilder();
        match.append(name).append(dot).append(level.scope.name);
        match.append(dot).append(type.type).append(dot);
        file.setDescription(match.toString());
        return file;
    }

    @Override
    public boolean register(String key, WandererFactory factory) {
        if (map.containsKey(key))
            return false;
        map.put(key, factory);
        return true;
    }

    /**
     * Get a wanderer
     *
     * @param bc  the sign that request the wanderer
     * @param inv the inventory where to extract the wanderercontent from
     * @return the wanderer
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @Override
    public WandererFactory getFactory(Inventory inv) throws ClassNotFoundException, IOException {
        if (isWanderer(inv)
                && getWandererContent(inv) != null)
            return map.get(getType(inv));
        return null;
    }

    public WandererFactory getFactory(String type) {
        if (isRegistered(type))
            return map.get(type);
        return null;
    }

    @Override
    public boolean isWanderer(Inventory inv, Scope scope) {
        return isWanderer(inv, scope, null, null);
    }

    @Override
    public boolean isWanderer(Inventory inv, Level level, String type) {
        return isWanderer(inv, level.scope, level.type, type);
    }

    @Override
    public boolean isWanderer(Inventory inv, String type) {
        return isWanderer(inv, null, null, type);
    }

    private boolean isWanderer(Inventory inv, Scope scope, String suffix, String type) {
        if (!isWanderer(inv))
            return false;
        ItemStack stack = inv.getItem(0);
        if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
            final BookMeta book = (BookMeta) stack.getItemMeta();
            final String booktitle = book.getTitle();
            final String dot = "\\.";
            final StringBuilder match = new StringBuilder();

            match.append("^");

            final String alphanums = "[a-zA-Z]{1,}";

            if (type != null)
                match.append(type).append(dot);
            else
                match.append(alphanums).append(dot);

            if (scope != null)
                match.append(scope.name).append(dot);
            else
                match.append(alphanums).append(dot);

            if (suffix != null)
                match.append(suffix).append(dot);

            match.append(".*");
            return InventoryFile.isInventoryFile(inv, type)
                    && booktitle.matches(match.toString());
        }
        return false;
    }

    public boolean isWanderer(Inventory inv) {
        String prefix = getType(inv);
        if (prefix != null)
            return isRegistered(prefix);
        return false;
    }

    private String getType(Inventory inv) {
        ItemStack stack = inv.getItem(0);
        String prefix = null;
        if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
            BookMeta book = (BookMeta) stack.getItemMeta();
            String booktitle = book.getTitle();
            int index = booktitle.indexOf(".");
            if (index > 0)
                prefix = booktitle.substring(0, index);
        }
        return prefix;
    }

    @Override
    public boolean isRegistered(String type) {
        return map.containsKey(type);
    }

    @Override
    public WandererContent getWandererContent(Inventory inv)
            throws IOException, ClassNotFoundException {
        WandererContent rte = null;
        InventoryFile file = null;
        if (InventoryFile.isInventoryFile(inv, null)) {
            file = new InventoryFile(inv, true, null);
        }
        if (file == null) {
            throw new IOException("No book found");
        }
        if (!file.isEmpty()) {
            ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
            rte = (WandererContent) ois.readObject();
        }
        rte.setInventory(inv);
        return rte;
    }

    @Override
    public void saveContent(InventoryContent rte, String suffix, Level level) throws ClassNotFoundException, IOException {
        Inventory inv = rte.getInventory();

        // delete content if expired
        long creation = rte.getCreationtime();
        long expiration = rte.getExpirationTime();
        if (creation != expiration && Calendar.getInstance().getTimeInMillis() > expiration) {
            LogUtil.sendSuccess(rte.getPlayer(), "ByteCart : " + suffix + " created " + (new Date(rte.getCreationtime())).toString() + " expired");
            WandererFactory factory = getFactory(inv);
            if (factory != null) {
                factory.destroyWanderer(inv);
            }
            return;
        }

        InventoryFile file = createWanderer(inv, rte.getRegion(), rte.getLevel(), rte.getPlayer(), suffix, level);
        ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
        oos.writeObject(rte);
        try {
            oos.flush();
        } catch (IOException e) {
            getFactory(inv).destroyWanderer(inv);
            ByteCart.log.info("Bytecart: I/O error (maximum capacity reached), updater deleted");
        }
    }

    @Override
    public void unregister(String name) {
        map.remove(name);
    }
}
