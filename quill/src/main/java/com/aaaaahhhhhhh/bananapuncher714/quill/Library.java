package com.aaaaahhhhhhh.bananapuncher714.quill;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.Catalog;

/*
 * This class is for adding, removing and searching for books from catalogs.
 * 
 * Use NamespacedKeys for fetching books from catalogs
 */
public class Library {
	protected Map< String, Catalog > catalogs = new HashMap< String, Catalog >();
	
	public Optional< Book > getBook( CommandSender sender, NamespacedKey key ) {
		Catalog catalog = catalogs.get( key.namespace );
		if ( catalog != null ) {
			return catalog.getBookFor( sender, key.key );
		}
		throw new IllegalArgumentException( String.format( "Catalog '%s' cannot be found!", key.namespace ) );
	}
	
	public Catalog getCatalog( String id ) {
		return catalogs.get( id );
	}
	
	public Optional< Catalog > addCatalog( Catalog catalog ) {
		return Optional.ofNullable( catalogs.put( catalog.getId(), catalog ) );
	}
	
	public Collection< Catalog > getCatalogs() {
		return Collections.unmodifiableCollection( catalogs.values() );
	}
}
