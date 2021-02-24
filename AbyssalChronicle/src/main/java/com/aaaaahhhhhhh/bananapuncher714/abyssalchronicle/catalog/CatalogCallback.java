package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog;

import java.util.Map;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable.UpdateState;

public interface CatalogCallback {
	void log( String message );
	void warn( String message );
	
	void onUpdateStyleSheets( Map< String, UpdateState > update );
	void onUpdateComponents( Map< String, UpdateState > update );
	void onUpdateBooks( Map< String, UpdateState > update );
}
