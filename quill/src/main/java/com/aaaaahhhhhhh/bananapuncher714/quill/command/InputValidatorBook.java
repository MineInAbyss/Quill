package com.aaaaahhhhhhh.bananapuncher714.quill.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.Library;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.InputValidator;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.Catalog;

public class InputValidatorBook implements InputValidator< NamespacedKey > {
	private Supplier< Library > librarySupplier;
	private String defaultCatalogId;
	private boolean usePerms = true;
	
	public InputValidatorBook( String defaultCatalogId, Supplier< Library > supplier, boolean usePerms ) {
		this.defaultCatalogId = defaultCatalogId;
		librarySupplier = supplier;
		this.usePerms = usePerms;
	}
	
	@Override
	public Collection< String > getTabCompletes( CommandSender sender, String[] input ) {
		Set< String > completions = new HashSet< String >();
		Library library = librarySupplier.get();
		if ( library != null ) {
			for ( Catalog catalog : library.getCatalogs() ) {
				for ( String str : catalog.getAvailableBooks( sender ) ) {
					if ( !usePerms || sender.hasPermission( "quill.book.read." + catalog.getId() + "." + str ) ) {
						completions.add( NamespacedKey.of( catalog.getId(), str ).toString() );
					}
				}
			}
		}
		return completions;
	}

	@Override
	public boolean isValid( CommandSender sender, String[] input, String[] args ) {
		NamespacedKey key = NamespacedKey.fromString( input[ 0 ] );
		Library library = librarySupplier.get();
		if ( library != null ) {
			String catalogId = key.namespace == null ? defaultCatalogId : key.namespace;
			Catalog catalog = library.getCatalog( catalogId );
			if ( catalog != null ) {
				if ( !usePerms || sender.hasPermission( "quill.book.read." + catalog.getId() + "." + key.key ) ) {
					return catalog.getAvailableBooks( sender ).contains( key.key );
				}
				return false;
			}
		}
		return false;
	}

	@Override
	public NamespacedKey get( CommandSender sender, String[] input ) {
		NamespacedKey key = NamespacedKey.fromString( input[ 0 ] );
		if ( key.namespace == null ) {
			key = NamespacedKey.of( defaultCatalogId, key.key );
		}
		return key;
	}
}
