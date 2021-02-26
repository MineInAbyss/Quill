package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentTail;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheet;

public class ComponentTransformerStyle implements ComponentTransformer {
	private Set< String > tags = new HashSet< String >();
	private Set< BookComponent > components = new HashSet< BookComponent >();
	private Deque< Style > styles = new ArrayDeque< Style >();
	private StyleSheet sheet;
	
	public ComponentTransformerStyle( CommandSender sender, CatalogBuildable cache, BookPart part ) {
		sheet = cache.flatten( part );
		
		tags.add( "component" );
		tags.add( "div" );
		tags.add( "click" );
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			String tag = object.getTagName();
			if ( tags.contains( tag ) ) {
				Style finalStyle = null;
				
				// Build the style
				String styleAttrib = object.getAttributes().getOrDefault( "styles", "" );
				String[] listedStyles = styleAttrib.split( "\\s+" );
				for ( String inheritedStyle : listedStyles ) {
					if ( !inheritedStyle.isEmpty() ) {
						Style found = sheet.getStyles().get( inheritedStyle );
						if ( found != null ) {
							if ( finalStyle == null ) {
								finalStyle = found.copyOf();
							} else {
								finalStyle.merge( found );
							}
						} else {
							throw new NullPointerException( String.format( "Missing style '%s'!", inheritedStyle ) );
						}
					}
				}
				
				if ( finalStyle != null && this.components.add( object ) ) {
					// Add the current component and
					// Merge the style with the old style
					if ( !styles.isEmpty() ) {
						finalStyle.merge( styles.peek() );
					}
					// Push it onto the stack
					styles.push( finalStyle );
				} else if ( styles.isEmpty() ) {
					push( object, new Style( "blank" ) );
				}
			} else if ( styles.isEmpty() ) {
				push( object, new Style( "blank" ) );
			}
		} else if ( component.isEndComponent() ) {
			BookComponentTail tail = component.getAsEndComponent();
			BookComponentObject head = tail.getHead();
			pop( head );
		}
		return false;
	}
	
	public Style getCurrentStyle() {
		return styles.peek();
	}
	
	public StyleSheet getStyleSheet() {
		return sheet;
	}
	
	public boolean push( BookComponent component, Style style ) {
		if ( components.add( component ) ) {
			styles.push( style );
			return true;
		}
		return false;
	}
	
	public boolean pop( BookComponent component ) {
		if ( components.remove( component ) ) {
			styles.pop();
			return true;
		}
		return false;
	}
}
