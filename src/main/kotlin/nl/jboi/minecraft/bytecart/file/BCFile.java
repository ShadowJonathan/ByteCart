package nl.jboi.minecraft.bytecart.file;

import org.bukkit.inventory.Inventory;

import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a file with various operations
 */
public interface BCFile extends Flushable {

    /**
     * Get the capacity, in bytes, of the file
     *
     * @return the capacity
     */
    int getCapacity();

    /**
     * clear the file content
     */
    void clear();

    /**
     * Tell if a file is empty
     *
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Get an output stream for this file
     *
     * @return the stream
     * @throws IOException
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Get an input stream
     *
     * @return the stream
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     * Get the inventory containing this file
     *
     * @return the inventory
     */
    Inventory getContainer();

    /**
     * Get the title for this file
     *
     * @return the title
     * @throws IOException
     */
    String getDescription() throws IOException;

    /**
     * Set a title to this file
     *
     * @param s the title to set
     * @throws IOException
     */
    void setDescription(String s) throws IOException;

    /**
     * Get all the pages in one string
     *
     * @return all the pages
     */
    String getPages();
}
