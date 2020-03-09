package nl.jboi.minecraft.bytecart.FileStorage;

import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class InventoryFile implements BCFile {

    private final Inventory inventory;
    private final boolean binarymode;
    private final String name;
    private InventoryOutputStream outputstream;
    private String title = "";

    public InventoryFile(Inventory inventory, boolean binary, String name) {
        this.inventory = inventory;
        this.binarymode = binary;
        this.name = name;
    }

    public static boolean isInventoryFile(Inventory inv, String name) {
        return InventoryInputStream.isInventoryInputStream(inv, name);
    }

    @Override
    public void flush() throws IOException {
        if (outputstream != null)
            outputstream.flush();
    }

    @Override
    public int getCapacity() {
        return BookFile.MAXSIZE * inventory.getSize();
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.firstEmpty() == 0;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (outputstream != null)
            return outputstream;

        return outputstream = new InventoryOutputStream(inventory, binarymode, title, name);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (outputstream != null && outputstream.toByteArray().length != 0) {
            return new InventoryInputStream(outputstream);
        }
        return new InventoryInputStream(inventory, binarymode, name);
    }

    @Override
    public Inventory getContainer() {
        return inventory;
    }

    @Override
    public String getDescription() throws IOException {
        return this.title;
    }

    @Override
    public void setDescription(String s) throws IOException {
        this.title = s;
    }

    @Override
    public String getPages() {
        return InventoryInputStream.readPages(inventory, name, binarymode);
    }

}
