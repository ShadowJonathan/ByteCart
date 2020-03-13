package nl.jboi.minecraft.bytecart.file;

import nl.jboi.minecraft.bytecart.util.Base64;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Base64 encoder/decoder for BookOutPutStream
 */
final class Base64BookOutputStream extends BookOutputStream {

    public Base64BookOutputStream(BookMeta book) {
        super(book);
    }

    @Override
    protected String getEncodedString() {
        return Base64.encodeToString(buf, false);
    }

    @Override
    protected byte[] getBuffer() {
        return Base64.encodeToByte(buf, false);
    }
}
