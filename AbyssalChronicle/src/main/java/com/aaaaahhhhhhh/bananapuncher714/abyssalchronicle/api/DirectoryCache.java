package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * Watch a directory for changes
 * 
 * Essentially, it should provide a list of changed files, sort of like git
 */
public class DirectoryCache {
	protected Path baseDir;
	protected Map< Path, FileTime > paths = new HashMap< Path, FileTime >();
	
	public DirectoryCache( Path path ) throws IOException {
		Files.createDirectories( path );
		this.baseDir = path;
	}
	
	public Set< Path > getCachedPaths() {
		return Collections.unmodifiableSet( paths.keySet() );
	}
	
	public DirectoryDifference getUpdates() {
		Set< Path > newPaths = new HashSet< Path >();
		try {
			getPathsRecursively( newPaths, baseDir );
		} catch ( IOException e1 ) {
			e1.printStackTrace();
		}
		
		DirectoryDifference difference = new DirectoryDifference();
		for ( Iterator< Entry< Path, FileTime > > iterator = paths.entrySet().iterator(); iterator.hasNext(); ) {
			Entry< Path, FileTime > entry = iterator.next();
			Path path = entry.getKey();
			FileTime lastModified = entry.getValue();
			
			// Attempt to remove it from the current list of paths
			if ( newPaths.remove( path ) ) {
				try {
					// Check when it was last modified
					FileTime modificationTime = Files.getLastModifiedTime( path );
					if ( modificationTime.compareTo( lastModified ) > 0 ) {
						// It's more recent than the currently stored file
						difference.updated.add( path );
						
						// Update it in the map
						entry.setValue( modificationTime );
					}
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			} else {
				// It's been deleted!
				difference.removed.add( path );
				iterator.remove();
			}
		}
		
		// Our path should only contain new items at this point
		for ( Path path : newPaths ) {
			difference.added.add( path );
			try {
				FileTime modificationTime = Files.getLastModifiedTime( path );
				paths.put( path, modificationTime );
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		
		return difference;
	}
	
	protected static void getPathsRecursively( Collection< Path > paths, Path baseDir ) throws IOException {
		paths.add( baseDir );
		if ( Files.isDirectory( baseDir ) ) {
			try ( DirectoryStream< Path > dirStream = Files.newDirectoryStream( baseDir ) ) {
				for ( Path subfile : dirStream ) {
					getPathsRecursively( paths, subfile );
				}
			}
		}
	}
	
	public class DirectoryDifference {
		Set< Path > added = new HashSet< Path >();
		Set< Path > removed = new HashSet< Path >();
		Set< Path > updated = new HashSet< Path >();
		
		public Set< Path > getAdded() {
			return added;
		}
		public Set< Path > getRemoved() {
			return removed;
		}
		public Set< Path > getUpdated() {
			return updated;
		}

		public boolean isUnchanged() {
			return added.isEmpty() && removed.isEmpty() && updated.isEmpty();
		}
	}
}
