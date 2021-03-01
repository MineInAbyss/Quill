package com.aaaaahhhhhhh.bananapuncher714.quill.catalog;

import java.util.Optional;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.Book;

/*
 * The catalog provides books. They can be saved, or constructed on the fly.
 * 
 */
public abstract class Catalog {
	private final String id;
	
	public Catalog( String id ) {
		this.id = id;
	}
	
	public final String getId() {
		return id;
	}
	
	public abstract Set< String > getAvailableBooks( CommandSender sender );
	public abstract Optional< Book > getBookFor( CommandSender sender, String id );
}
