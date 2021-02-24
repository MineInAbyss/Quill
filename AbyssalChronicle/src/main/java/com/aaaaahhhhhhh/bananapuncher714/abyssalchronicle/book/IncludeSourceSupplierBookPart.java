package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSourceSupplier;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;

public class IncludeSourceSupplierBookPart implements IncludeSourceSupplier< BookPart > {
	protected CatalogBuildable cache;
	
	public IncludeSourceSupplierBookPart( CatalogBuildable catalog ) {
		this.cache = catalog;
	}
	
	@Override
	public BookPart fetch( IncludeSource source ) {
		if ( source.type.equalsIgnoreCase( "book" ) ) {
			return cache.getCachedBook( source.id );
		}
		return null;
	}
}
