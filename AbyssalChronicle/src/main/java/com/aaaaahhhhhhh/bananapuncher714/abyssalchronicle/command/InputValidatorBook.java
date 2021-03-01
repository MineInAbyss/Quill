package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Library;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.validator.InputValidator;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.Catalog;

public class InputValidatorBook implements InputValidator< NamespacedKey > {
	private Supplier< Library > librarySupplier;
	private String defaultCatalogId;
	
	public InputValidatorBook( String defaultCatalogId, Supplier< Library > supplier ) {
		this.defaultCatalogId = defaultCatalogId;
		librarySupplier = supplier;
	}
	
	@Override
	public Collection< String > getTabCompletes( CommandSender sender, String[] input ) {
		Set< String > completions = new HashSet< String >();
		Library library = librarySupplier.get();
		if ( library != null ) {
			for ( Catalog catalog : library.getCatalogs() ) {
				for ( String str : catalog.getAvailableBooks( sender ) ) {
					completions.add( NamespacedKey.of( catalog.getId(), str ).toString() );
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
				return catalog.getAvailableBooks( sender ).contains( key.key );
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
