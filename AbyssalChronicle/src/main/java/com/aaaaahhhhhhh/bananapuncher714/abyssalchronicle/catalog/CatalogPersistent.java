package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;

public class CatalogPersistent extends Catalog {
	protected Path directory;
	
	public CatalogPersistent( String id, Path directory ) {
		super( id );
		
		this.directory = directory;
	}
	
	public void load() {
		// TODO Load books
	}
	
	public void save() {
		// TODO Save books
	}

	@Override
	public Set< String > getAvailableBooks( CommandSender sender ) {
		return null;
	}

	@Override
	public Optional< Book > getBookFor( CommandSender sender, String id ) {
		return null;
	}
}
