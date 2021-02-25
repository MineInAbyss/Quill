package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable.UpdateState;

public class CatalogCallbackPlugin implements CatalogCallback {
	private Plugin plugin;
	
	public CatalogCallbackPlugin( Plugin plugin ) {
		this.plugin = plugin;
	}
	
	@Override
	public void log( String message ) {
		plugin.getLogger().info( message );
	}

	@Override
	public void warn( String message ) {
		plugin.getLogger().warning( message );
	}

	/*
	 * I know it looks very copy and paste, and it definitely is
	 * but they're fundamentally different. I just didn't
	 * feel like writing different code for each.
	 */
	
	@Override
	public void onUpdateStyleSheets( Map< String, UpdateState > update ) {
		if ( !update.isEmpty() ) {
			log( "Detected stylesheet updates" );
			for ( Entry< String, UpdateState > entry : update.entrySet() ) {
				String path = entry.getKey();
				UpdateState state = entry.getValue();
				if ( state == UpdateState.ADDED ) {
					log( String.format( "Added stylesheet '%s'", path ) );
				} else if ( state == UpdateState.REMOVED ) {
					log( String.format( "Removed stylesheet '%s'", path ) );
				} else if ( state == UpdateState.UPDATED ) {
					log( String.format( "Updated stylesheet '%s'", path ) );
				}
			}
		}
	}

	@Override
	public void onUpdateComponents( Map< String, UpdateState > update ) {
		if ( !update.isEmpty() ) {
			log( "Detected component updates" );
			for ( Entry< String, UpdateState > entry : update.entrySet() ) {
				String path = entry.getKey();
				UpdateState state = entry.getValue();
				if ( state == UpdateState.ADDED ) {
					log( String.format( "Added component '%s'", path ) );
				} else if ( state == UpdateState.REMOVED ) {
					log( String.format( "Removed component '%s'", path ) );
				} else if ( state == UpdateState.UPDATED ) {
					log( String.format( "Updated component '%s'", path ) );
				}
			}
		}

	}

	@Override
	public void onUpdateBooks( Map< String, UpdateState > update ) {
		if ( !update.isEmpty() ) {
			log( "Detected book updates" );
			for ( Entry< String, UpdateState > entry : update.entrySet() ) {
				String path = entry.getKey();
				UpdateState state = entry.getValue();
				if ( state == UpdateState.ADDED ) {
					log( String.format( "Added book '%s'", path ) );
				} else if ( state == UpdateState.REMOVED ) {
					log( String.format( "Removed book '%s'", path ) );
				} else if ( state == UpdateState.UPDATED ) {
					log( String.format( "Updated book '%s'", path ) );
				}
			}
		}
	}

}
