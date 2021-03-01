package com.aaaaahhhhhhh.bananapuncher714.quill.style;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.IncludeSourceSupplier;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class IncludeSourceSupplierStyleSheet implements IncludeSourceSupplier< StyleSheet > {
	private CatalogBuildable catalog;
	
	public IncludeSourceSupplierStyleSheet( CatalogBuildable catalog ) {
		this.catalog = catalog;
	}
	
	@Override
	public StyleSheet fetch( IncludeSource source ) {
		if ( source.type.equalsIgnoreCase( "stylesheet" ) ) {
			return catalog.getCachedStyleSheet( source.id );
		}
		return null;
	}
}
