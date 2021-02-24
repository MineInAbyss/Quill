package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.DirectoryCache;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.DirectoryCache.DirectoryDifference;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSourceSupplier;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPartParserFile;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder.BookIndex;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder.Bookbinder;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder.BookbinderIndex;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHeadParserFile;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformer;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.TransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheet;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheetParserFile;

/*
 * Uses some sort of XML based configuration for creating books dynamically.
 */
public class CatalogBuildable extends Catalog {
	private Path baseDir;
	
	private final long delay;
	
	// I could definitely convert these into some sort of generic object...
	private Path styleCachePath;
	private DirectoryCache styleCache;
	private List< StyleSheetParserFile > stylesheetParsers = new ArrayList< StyleSheetParserFile >();
	private Map< Path, StyleSheet > stylesheets = new ConcurrentHashMap< Path, StyleSheet >();
	private Map< String, StyleSheet > stylesheetIds = new ConcurrentHashMap< String, StyleSheet >();

	private Path componentCachePath;
	private DirectoryCache componentCache;
	private List< BookComponentHeadParserFile > componentParsers = new ArrayList< BookComponentHeadParserFile >();
	private Map< Path, BookComponentHead > components = new ConcurrentHashMap< Path, BookComponentHead >();
	
	private Path bookCachePath;
	private DirectoryCache bookCache;
	private List< BookPartParserFile > bookParsers = new ArrayList< BookPartParserFile >();
	private Map< Path, BookPart > books = new ConcurrentHashMap< Path, BookPart >();
	private Map< String, BookPart > bookIds = new ConcurrentHashMap< String, BookPart >();
	
	private List< TransformerSupplier< ? extends ComponentTransformer > > transformers = new ArrayList< TransformerSupplier< ? extends ComponentTransformer > >();
	private List< Function< CatalogBuildable, IncludeSourceSupplier< ? extends BookPart > > > bookPartIncludeSuppliers = new ArrayList< Function< CatalogBuildable, IncludeSourceSupplier< ? extends BookPart > > >();
	private List< Function< CatalogBuildable, IncludeSourceSupplier< ? extends StyleSheet > > > styleIncludeSuppliers = new ArrayList< Function< CatalogBuildable, IncludeSourceSupplier< ? extends StyleSheet > > >();
	
	private Bookbinder< BookIndex > bookbinder = new BookbinderIndex();
	
	private Thread thread;
	private volatile boolean running = false;
	
	private CatalogCallback callback;
	
	public CatalogBuildable( String id, Path baseDir, long delay ) {
		super( id );
		
		this.baseDir = baseDir;
		this.delay = delay;
		
		try {
			styleCachePath = baseDir.resolve( "styles" );
			styleCache = new DirectoryCache( styleCachePath );
			
			componentCachePath = baseDir.resolve( "components" );
			componentCache = new DirectoryCache( componentCachePath );
			
			bookCachePath = baseDir.resolve( "books" );
			bookCache = new DirectoryCache( bookCachePath );
		} catch ( IOException exception ) {
			exception.printStackTrace();
		}
	}
	
	public void start() {
		running = true;
		thread = new Thread( this::run );
		thread.start();
	}
	
	public void stop() {
		if ( running ) {
			running = false;
		}
	}
	
	private void run() {
		while ( running ) {
			update();
			
			try {
				Thread.sleep( delay );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}
	
	private void update() {
		// Seems like bad design here, it's just 3 copied and pasted methods
		
		// Update the styles
		Map< String, UpdateState > updatedSheets = updateStylesheets();
		
		// Update the components
		Map< String, UpdateState > updatedComponents = updateComponents();
		
		// Update the books
		Map< String, UpdateState > updatedBooks = updateBooks();
		
		// We don't need to "rebuild" the books here, since we do it each time it is requested for any particular player
		if ( callback != null ) {
			callback.onUpdateStyleSheets( updatedSheets );
			callback.onUpdateComponents( updatedComponents );
			callback.onUpdateBooks( updatedBooks );
		}
	}

	private Map< String, UpdateState > updateStylesheets() {
		Map< String, UpdateState > changedStylesheets = new HashMap< String, UpdateState >();
		
		DirectoryDifference styleDifference = styleCache.getUpdates();
		for ( Path removed : styleDifference.getRemoved() ) {
			StyleSheet sheet = stylesheets.remove( removed );
			if ( sheet != null ) {
				stylesheetIds.remove( sheet.getId() );
				changedStylesheets.put( sheet.getId(), UpdateState.REMOVED );
			}
		}
		
		// Merge the updated and added files and create stylesheets from them if possible
		Set< Path > needsUpdating = new HashSet< Path >( styleDifference.getAdded() );
		needsUpdating.addAll( styleDifference.getUpdated() );
		for ( Path path : needsUpdating ) {
			StyleSheet sheet = null;
			for ( Iterator< StyleSheetParserFile > iterator = stylesheetParsers.iterator(); iterator.hasNext() && sheet == null; ) {
				StyleSheetParserFile parser = iterator.next();
				if ( parser.matches( path ) ) {
					try {
						sheet = parser.parse( path );
					} catch ( Exception exception ) {
						if ( callback != null ) {
							callback.warn( String.format( "Unable to parse stylesheet '%s'!", path ) );
						}
						exception.printStackTrace();
					}
				}
			}
			if ( sheet != null ) {
				String id = sheet.getId();
				changedStylesheets.put( id, stylesheetIds.containsKey( id ) ? UpdateState.UPDATED : UpdateState.ADDED );
				stylesheets.put( path, sheet );
				stylesheetIds.put( id, sheet );
			}
		}
		
		return changedStylesheets;
	}
	
	private Map< String, UpdateState > updateComponents() {
		Map< String, UpdateState > changedComponents = new HashMap< String, UpdateState >();
		
		DirectoryDifference componentDifference = componentCache.getUpdates();
		for ( Path removed : componentDifference.getRemoved() ) {
			BookComponentHead component = components.remove( removed );
			if ( component != null ) {
				changedComponents.put( component.getId(), UpdateState.REMOVED );
			}
		}
		
		Set< Path > needsUpdating = new HashSet< Path >( componentDifference.getAdded() );
		needsUpdating.addAll( componentDifference.getUpdated() );
		for ( Path path : needsUpdating ) {
			BookComponentHead component = null;
			for ( Iterator< BookComponentHeadParserFile > iterator = componentParsers.iterator(); iterator.hasNext() && component == null; ) {
				BookComponentHeadParserFile parser = iterator.next();
				if ( parser.matches( path ) ) {
					try {
						component = parser.parse( path );
					} catch ( Exception exception ) {
						if ( callback != null ) {
							callback.warn( String.format( "Unable to parse component '%s'!", path ) );
						}
						exception.printStackTrace();
					}
				}
			}
			if ( component != null ) {
				changedComponents.put( component.getId(), components.containsKey( path ) ? UpdateState.UPDATED : UpdateState.ADDED );
				components.put( path, component );
			}
		}
		
		return changedComponents;
	}
	
	private Map< String, UpdateState > updateBooks() {
		Map< String, UpdateState > changedBooks = new HashMap< String, UpdateState >();
		
		DirectoryDifference bookDifference = bookCache.getUpdates();
		for ( Path removed : bookDifference.getRemoved() ) {
			BookPart bookPart = books.remove( removed );
			if ( bookPart != null ) {
				bookIds.remove( bookPart.getId() );
				changedBooks.put( bookPart.getId(), UpdateState.REMOVED );
			}
		}
		
		// Merge the updated and added files and create stylesheets from them if possible
		Set< Path > needsUpdating = new HashSet< Path >( bookDifference.getAdded() );
		needsUpdating.addAll( bookDifference.getUpdated() );
		for ( Path path : needsUpdating ) {
			BookPart bookPart = null;
			for ( Iterator< BookPartParserFile > iterator = bookParsers.iterator(); iterator.hasNext() && bookPart == null; ) {
				BookPartParserFile parser = iterator.next();
				if ( parser.matches( path ) ) {
					try {
						bookPart = parser.parse( path );
					} catch ( Exception exception ) {
						if ( callback != null ) {
							callback.warn( String.format( "Unable to parse book '%s'!", path ) );
						}
						exception.printStackTrace();
					}
				}
			}
			if ( bookPart != null ) {
				String id = bookPart.getId();
				changedBooks.put( id, bookIds.containsKey( id ) ? UpdateState.UPDATED : UpdateState.ADDED );
				books.put( path, bookPart );
				bookIds.put( id, bookPart );
			}
		}
		
		return changedBooks;
	}
	
	@Override
	public Set< String > getAvailableBooks( CommandSender sender ) {
		return bookIds.keySet();
	}

	@Override
	public Optional< Book > getBookFor( CommandSender sender, String id ) {
		BookPart part = bookIds.get( id );
		if ( part != null ) {
			try {
				BookIndex index = convert( sender, part );
				return Optional.ofNullable( bookbinder.parse( index ) );
			} catch ( Exception exception ) {
				if ( callback != null ) {
					callback.warn( String.format( "Unable to create book '%s'!", id ) );
				}
				exception.printStackTrace();
			}
		}
		
		return Optional.empty();
	}
	
	public BookComponent getCachedComponent( String id ) {
		return components.get( componentCachePath.resolve( id ) );
	}
	
	public StyleSheet getCachedStyleSheet( String id ) {
		return stylesheetIds.get( id );
	}
	
	public BookPart getCachedBook( String id ) {
		return bookIds.get( id );
	}
	
	public BookComponentHead findComponent( BookPart part, NamespacedKey key ) {
		String id = key.namespace;
		if ( id == null ) {
			// Nothing was specified, check the book, and if nothing is found, then the list of public components
			BookComponentHead component = part.getComponents().get( key.key );
			if ( component == null ) {
				component = components.get( componentCachePath.resolve( key.key ) );
			}
			
			return component;
		} else {
			// An id was specified, search the include sources for the component
			List< IncludeSourceSupplier< ? extends BookPart > > suppliers = bookPartIncludeSuppliers.stream().map( f -> f.apply( this ) ).collect( Collectors.toList() );
			for ( IncludeSource includeSource : part.getIncludes() ) {
				for ( IncludeSourceSupplier< ? extends BookPart > supplier : suppliers ) {
					BookPart bookPart = supplier.fetch( includeSource );
					if ( bookPart != null && bookPart.getId().equalsIgnoreCase( key.namespace ) ) {
						BookComponentHead component = bookPart.getComponents().get( key.key );
						if ( component != null ) {
							return component;
						}
					}
				}
			}
			
			return null;
		}
	}
	
	public StyleSheet flatten( BookPart part ) {
		return flatten( part.getStyleSheet(), part.getIncludes() );
	}
	
	public StyleSheet flatten( StyleSheet sheet ) {
		return flatten( sheet, sheet.getIncludes() );
	}
	
	private StyleSheet flatten( StyleSheet original, List< IncludeSource > sources ) {
		Set< StyleSheet > sheetSet = new HashSet< StyleSheet >();
		List< StyleSheet > sheets = new ArrayList< StyleSheet >();
		sheetSet.add( original );
		sheets.add( original );
		
		List< IncludeSourceSupplier< ? extends StyleSheet > > suppliers = styleIncludeSuppliers.stream().map( f -> f.apply( this ) ).collect( Collectors.toList() );
		Deque< IncludeSource > includeQueue = new ArrayDeque< IncludeSource >( sources );

		while ( !includeQueue.isEmpty() ) {
			IncludeSource source = includeQueue.poll();
			for ( IncludeSourceSupplier< ? extends StyleSheet > supplier : suppliers ) {
				StyleSheet supplied = supplier.fetch( source );
				if ( supplied != null && !sheetSet.contains( supplied ) ) {
					sheets.add( supplied );
					sheetSet.add( supplied );
				}
			}
		}
		
		return combine( sheets );
	}
	
	public StyleSheet combine( List< StyleSheet > sheets ) {
		StyleSheet copy = new StyleSheet( "" );

		Map< String, StyleSheet > sheetMap = new HashMap< String, StyleSheet >();
		for ( StyleSheet sheet : sheets ) {
			sheetMap.put( sheet.getId(), sheet );
		}
		
		// So, we have a list of stylesheets
		// Now, for each stylesheet, loop through the styles
		for ( int i = 0; i < sheets.size(); i++ ) {
			StyleSheet sheet = sheets.get( i );
			
			// Resolve each style, if it doesn't already exist
			for ( Entry< String, Style > entry : sheet.getStyles().entrySet() ) {
				if ( !copy.getStyles().containsKey( entry.getKey() ) ) {
					// Flatten the style
					Style style = entry.getValue().copyOf();
					for ( NamespacedKey key : style.getParents() ) {
						String sheetId = key.namespace;
						String styleId = key.key;

						StyleSheet parentSheet = sheet;
						if ( sheetId != null ) {
							parentSheet = sheetMap.get( sheetId );
						}
						
						if ( parentSheet == null ) {
							throw new NullPointerException( String.format( "Missing stylesheet '%s'!", sheetId ) );
						}
						
						Style parentStyle = parentSheet.getStyles().get( styleId );
						if ( parentStyle == null ) {
							throw new NullPointerException( String.format( "Missing style '%s' from stylesheet '%s'!", styleId, parentSheet.getId() ) );
						}
						
						style.merge( parentStyle );
					}
					
					copy.getStyles().put( style.getId(), style );
				}
			}
		}
		
		return copy;
	}
	
	public BookIndex convert( CommandSender sender, BookPart part ) {
		BookIndex index = new BookIndex( part.getId(), part.getTitle(), part.getAuthor() );
		
		// Create the content
		List< ComponentTransformer > transformers = this.transformers.stream().map( f -> f.createTransformer( sender, this, part ) ).collect( Collectors.toList() );
		index.getPages().add( new BookPage() );
		Deque< BookComponent > componentList = new ArrayDeque< BookComponent >();
		componentList.add( part.getContent() );
		while ( !componentList.isEmpty() ) {
			BookComponent subComponent = componentList.poll();
			for ( int i = 0; i < transformers.size() && !transformers.get( i ).transform( index.getPages(), subComponent, componentList ); i++ );				
		}
		
		// Create the headers
		BookComponentHead headerComponent = findComponent( part, NamespacedKey.fromString( part.getHeaderId() ) );
		if ( headerComponent != null ) {
			index.getHeaders().add( new BookPage() );
			Deque< BookComponent > headerComponents = new ArrayDeque< BookComponent >();
			headerComponents.add( headerComponent );
			while ( !headerComponents.isEmpty() ) {
				BookComponent subComponent = headerComponents.poll();
				for ( int i = 0; i < transformers.size() && !transformers.get( i ).transform( index.getHeaders(), subComponent, headerComponents ); i++ );				
			}
		}
		
		// Create the footers
		BookComponentHead footerComponent = findComponent( part, NamespacedKey.fromString( part.getFooterId() ) );
		if ( footerComponent != null ) {
			index.getFooters().add( new BookPage() );
			Deque< BookComponent > footerComponents = new ArrayDeque< BookComponent >();
			footerComponents.add( footerComponent );
			while ( !footerComponents.isEmpty() ) {
				BookComponent subComponent = footerComponents.poll();
				for ( int i = 0; i < transformers.size() && !transformers.get( i ).transform( index.getFooters(), subComponent, footerComponents ); i++ );				
			}
		}
		
		return index;
	}

	public List< StyleSheetParserFile > getStylesheetParsers() {
		return stylesheetParsers;
	}

	public List< BookComponentHeadParserFile > getComponentParsers() {
		return componentParsers;
	}

	public List< BookPartParserFile > getBookParsers() {
		return bookParsers;
	}

	public List< TransformerSupplier< ? extends ComponentTransformer > > getTransformers() {
		return transformers;
	}

	public List< Function<CatalogBuildable, IncludeSourceSupplier< ? extends BookPart > > > getBookPartIncludeSuppliers() {
		return bookPartIncludeSuppliers;
	}

	public List< Function< CatalogBuildable, IncludeSourceSupplier< ? extends StyleSheet > > > getStyleIncludeSuppliers() {
		return styleIncludeSuppliers;
	}

	public Bookbinder< BookIndex > getBookbinder() {
		return bookbinder;
	}

	public void setBookbinder( Bookbinder< BookIndex > bookbinder ) {
		this.bookbinder = bookbinder;
	}
	
	public Path getStyleBaseDirectory() {
		return styleCachePath;
	}
	
	public Path getComponentBaseDirectory() {
		return componentCachePath;
	}
	
	public Path getBookBaseDirectory() {
		return bookCachePath;
	}
	
	public void setCallback( CatalogCallback callback ) {
		this.callback = callback;
	}
	
	public CatalogCallback getCallback() {
		return callback;
	}
	
	public enum UpdateState {
		ADDED, REMOVED, UPDATED;
	}
}
