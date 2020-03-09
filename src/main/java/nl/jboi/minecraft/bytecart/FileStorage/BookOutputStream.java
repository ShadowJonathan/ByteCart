package nl.jboi.minecraft.bytecart.FileStorage;

import nl.jboi.minecraft.bytecart.ByteCart;
import org.bukkit.inventory.meta.BookMeta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * An output stream to write in book
 */
class BookOutputStream extends ByteArrayOutputStream {

    static final int PAGESIZE = 255;
    private static final int MAXPAGE = 50;
    static final int MAXSIZE = MAXPAGE * PAGESIZE;

    private final BookMeta book;

    private boolean isClosed = false;

    BookOutputStream(BookMeta book) {
        super(book.getPageCount() * PAGESIZE);
        this.book = book;
    }

    @Override
    public void write(byte[] bytes, int off, int len) {
//		this.reset();
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart : empty data cache buffer");

        super.write(bytes, off, len);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if (isClosed)
            throw new IOException("Book has been already closed");
        this.write(bytes, 0, bytes.length);
    }

    /**
     * Get the content as a byte array
     *
     * @return the buffer
     */
    protected byte[] getBuffer() {
        return this.toByteArray();
    }

    @Override
    public void flush() throws IOException {

        if (isClosed)
            throw new IOException("Book has been already closed");

        if (this.size() == 0)
            return;

        StringBuilder sb = new StringBuilder(getEncodedString());

        int len = sb.length();
        int i, j = 1;

        // number of pages to write
        int count = 1 + (len - 1) / PAGESIZE;

        // Throw if too many pages are needed
        if (count > MAXPAGE) {
            if (ByteCart.debug)
                ByteCart.log.info(count + " pages are needed, maximum is " + MAXPAGE);
            throw new IOException();
        }

        String[] strings = new String[count];

        // loop for full pages
        count -= 1;
        for (i = 0; i < count; i++) {
            strings[i] = sb.substring(i * PAGESIZE, j * PAGESIZE);
            j++;
        }

        // last page
        strings[count] = sb.substring(i * PAGESIZE);

        this.book.setPages(strings);

        if (ByteCart.debug)
            ByteCart.log.info("ByteCart : Flushing " + len + " bytes of data to meta");
    }

    /**
     * Get the content as a string
     *
     * @return the content of the book
     */
    protected String getEncodedString() {
        return this.toString();
    }

    @Override
    public void close() throws IOException {
        if (isClosed)
            throw new IOException("Book has been already closed");
        isClosed = true;
    }

    /**
     * Get the book
     *
     * @return the book
     */
    final BookMeta getBook() {
        return book;
    }
}

