package nl.jboi.minecraft.bytecart.updater;

import nl.jboi.minecraft.bytecart.ByteCart;
import nl.jboi.minecraft.bytecart.file.InventoryFile;
import nl.jboi.minecraft.bytecart.util.LogUtil;
import nl.jboi.minecraft.bytecart.wanderer.BCWandererManager;
import nl.jboi.minecraft.bytecart.api.event.UpdaterRemoveEvent;
import nl.jboi.minecraft.bytecart.api.sign.BCSign;
import nl.jboi.minecraft.bytecart.api.wanderer.InventoryContent;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Level;
import nl.jboi.minecraft.bytecart.api.wanderer.Wanderer.Scope;
import nl.jboi.minecraft.bytecart.api.wanderer.WandererFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;

public final class UpdaterFactory implements WandererFactory {

    // A map with ephemeral elements and timers
    private UpdaterSet updaterset = new UpdaterSet();

    @Override
    public Wanderer getWanderer(BCSign bc, Inventory inv) {
        UpdaterContent rte;
        final BCWandererManager wm = ByteCart.myPlugin.getWandererManager();
        if (wm.isWanderer(inv, "Updater"))
            updaterset.addUpdater(bc.getVehicle().getEntityId());
        if (wm.isWanderer(inv, Level.REGION, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterRegion(bc, rte);

        if (wm.isWanderer(inv, Level.RESET_REGION, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterResetRegion(bc, rte);

        if (wm.isWanderer(inv, Level.BACKBONE, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterBackBone(bc, rte);

        if (wm.isWanderer(inv, Level.RESET_BACKBONE, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterResetBackbone(bc, rte);

        if (wm.isWanderer(inv, Level.RESET_LOCAL, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterResetLocal(bc, rte);

        if (wm.isWanderer(inv, Level.LOCAL, "Updater")
                && (rte = getUpdaterContent(inv)) != null)
            return new UpdaterLocal(bc, rte);
        return null;
    }

    @Override
    public final void removeAllWanderers() {
        updaterset.clear();
    }

    @Override
    public boolean areAllRemoved() {
        return updaterset.getMap().isEmpty();
    }

    private UpdaterContent getUpdaterContent(Inventory inv) {
        UpdaterContent rte = null;
        InventoryFile file = null;
        if (InventoryFile.isInventoryFile(inv, "Updater")) {
            file = new InventoryFile(inv, true, "Updater");
        }
        if (file == null) {
            return null;
        }
        if (!file.isEmpty()) {
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(file.getInputStream());
                rte = (UpdaterContent) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        rte.setInventory(inv);
        return rte;
    }

    @Override
    public void updateTimestamp(InventoryContent rte) {
        // Update the expiration time to have twice the spent time left
        long initial;
        long expiration;
        if ((initial = rte.getCreationtime()) == (expiration = rte.getExpirationTime()))
            return;
        long last = Calendar.getInstance().getTimeInMillis();
        long update = last + ((last - initial) << 1);
        if (update > expiration) {
            rte.setExpirationTime(update);
            updaterset.getMap().reset(update - last / 50, ((Vehicle) rte.getInventory().getHolder()).getEntityId());
        }
    }

    @Override
    public void createWanderer(int id, Inventory inv, int region, Level level, Player player
            , boolean isfullreset, boolean isnew) {
        UpdaterContent rte;
        if (level.scope.equals(Scope.LOCAL))
            rte = new UpdaterContent(inv, level, region, player, isfullreset);
        else
            rte = new UpdaterContent(inv, level, region, player, isfullreset, isnew);
        try {
            ByteCart.myPlugin.getWandererManager().saveContent(rte, "Updater", level);
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        updaterset.getMap().add(id);
        LogUtil.sendError(player, ByteCart.myPlugin.getConfig().getString("Info.SetUpdater"));
    }

    @Override
    public void destroyWanderer(Inventory inv) {
        int id = ((Vehicle) inv.getHolder()).getEntityId();
        Bukkit.getServer().getPluginManager().callEvent(new UpdaterRemoveEvent(id));
        updaterset.getMap().remove(id);
        inv.clear();
    }
}
