package com.github.catageek.ByteCart.IO;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import com.github.catageek.ByteCart.ByteCart;

public final class BookProperties {

	private final Properties Properties = new Properties();
	private final Conf PageNumber;
	private final int Index;
	private Reader Reader = null;
	private OutputStream OutputStream = null;
	private final Inventory Inventory;

	public enum Conf {
		NETWORK(1, "Network"),
		BILLING(2, "Billing"),
		ACCESS(3, "Access"),
		PROTECTION(4, "Protection"),
		HISTORY(5, "History");

		private final int page;
		private final String name;

		Conf(int page, String name) {
			this.page = page;
			this.name = name;
		}
	}

	public BookProperties(Inventory inventory, int index, Conf page) {
		super();
		Inventory = inventory;
		Index = index;
		PageNumber = page;
		ItemStack stack = inventory.getItem(index);

		BookMeta meta = (BookMeta) (stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(Material.BOOK_AND_QUILL));
		if (! meta.hasPages())
			meta.addPage((String) null);

		try {
			BookOutputStream bookoutputstream = new BookOutputStream(meta, PageNumber.page);
			ItemStackOutputStream stackoutputstream = new ItemStackMetaWriter(stack, bookoutputstream);
			InventoryItemStackOutputStream inventoryoutputstream = new InventoryItemStackOutputStream(inventory, index, stackoutputstream);
			OutputStream = new BufferedOutputStream(inventoryoutputstream, 256);

		} catch (NullPointerException e) {
		}
	}

	public void setProperty(String key, String value) {
		try {
			readPrepare();
			Properties.setProperty(key, value);
			Properties.store(OutputStream, PageNumber.name);
			OutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearProperty(String key) {
		try {
			readPrepare();
			Properties.remove(key);
			Properties.store(OutputStream, PageNumber.name);
			OutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getString(String key) {
		try {
			readPrepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Properties.getProperty(key);
	}

	private void readPrepare() throws IOException {
		ItemStack stack = Inventory.getItem(Index);
		BookMeta meta = (BookMeta) (stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(Material.BOOK_AND_QUILL));
		if (! meta.hasPages())
			meta.addPage((String) null);

		{
			if (ByteCart.debug)
				ByteCart.log.info("ByteCart: read page " + PageNumber.page);
			Reader = new BookReader(meta, PageNumber.page);
			Properties.load(Reader);
		}
	}

	public String getString(String key, String defaultvalue) {
		try {
			readPrepare();
		} catch (IOException e) {
			return defaultvalue;
		}
		return Properties.getProperty(key, defaultvalue);
	}

	public int getInt(String key) {
		try {
			readPrepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.getInteger(Properties.getProperty(key));

	}

	public int getInt(String key, int defaultvalue) {
		try {
			readPrepare();
		} catch (IOException e) {
			return 0;
		}

		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: property string : "+ Properties.getProperty(key, ""+defaultvalue));
		return Integer.parseInt(Properties.getProperty(key, ""+defaultvalue));

	}


}