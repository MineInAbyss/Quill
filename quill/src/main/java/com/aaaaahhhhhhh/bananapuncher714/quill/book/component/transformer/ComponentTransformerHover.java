package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentTail;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleSheet;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/*
 * This entire class is a black box that shouldn't work.
 * 
 * It has so many random dependencies on other classes when it shouldn't,
 * 
 * And if anything is slightly out of place, then everything will break.
 * 
 * But it's not like anyone's ever going to bother reading this, right?!
 */
public class ComponentTransformerHover implements ComponentTransformer, Consumer< Supplier< ? extends ComponentTransformerStyle > > {
	private CatalogBuildable cache;
	private BookPart part;
	
	private Supplier< ? extends ComponentTransformerStyle > supplier;

	private BookComponentObject currentComponent;
	private BookComponentHead endComponent;
	private List< BookPage > oldPages;
	private TextComponent hover;
	
	public ComponentTransformerHover( CommandSender sender, CatalogBuildable cache, BookPart part ) {
		this.cache = cache;
		this.part = part;
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		// Check if the component is a hover component
		if ( component.isObjectComponent() && currentComponent == null ) {
			BookComponentObject object = component.getAsObjectComponent();
			if ( object.getTagName().equalsIgnoreCase( "hover" ) ) {
				String componentId = object.getAttributes().get( "component" );
				if ( componentId == null ) {
					throw new NullPointerException( "No component id found!" );
				} else {
					BookComponentHead subComponent = cache.findComponent( part, NamespacedKey.fromString( componentId ) );
					if ( subComponent != null ) {
						currentComponent = object;
						endComponent = subComponent;
						components.addFirst( subComponent );
						
						// Get the styles from the hover component
						// This is a lot of copy and paste...
						// There must be a better way to do this
						StyleSheet sheet = supplier.get().getStyleSheet();
						String styleAttrib = object.getAttributes().getOrDefault( "styles", "" );
						String[] listedStyles = styleAttrib.split( "\\s+" );
						Style finalStyle = new Style( componentId );
						for ( String inheritedStyle : listedStyles ) {
							if ( !inheritedStyle.isEmpty() ) {
								Style found = sheet.getStyles().get( inheritedStyle );
								if ( found != null ) {
									finalStyle.merge( found );
								} else {
									throw new NullPointerException( String.format( "Missing style '%s'!", inheritedStyle ) );
								}
							}
						}
						
						// Merge the style properties
						finalStyle.getProperties().merge( supplier.get().getCurrentStyle().getProperties() );
						
						// Given the final style, push it onto the current transformer
						supplier.get().push( currentComponent, finalStyle );
						
						// Clear and re-fill the current list of pages
						oldPages = new ArrayList< BookPage >( pages );
						
						pages.clear();
						pages.add( new BookPage() );
					} else {
						throw new NullPointerException( String.format( "No component with the id '%s' found!", componentId ) );
					}
				}
			}
		} else if ( component.isEndComponent() ) {
			BookComponentTail end = component.getAsEndComponent();
			if ( end.getHead() == currentComponent ) {
				// Apply the component to all text components added since the start of the hover event
				for ( BookPage page : pages ) {
					for ( TextComponent subComponent : page.getComponents() ) {
						subComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text( new BaseComponent[] { hover } ) ) );
					}
				}
				
				// Merge the last page of the old pages and the first page of the new pages, if it exists
				if ( !oldPages.isEmpty() && !pages.isEmpty() ) {
					oldPages.get( oldPages.size() - 1 ).getComponents().addAll( pages.remove( 0 ).getComponents() );
				}
				pages.addAll( 0, oldPages );
				
				// Reset
				currentComponent = null;
				oldPages = null;
				hover = null;
			} else if ( end.getHead() == endComponent ) {
				// Combine the pages into a single text component
				hover = new TextComponent();
				for ( BookPage page : pages ) {
					for ( TextComponent subComponent : page.getComponents() ) {
						hover.addExtra( subComponent );
					}
				}
				
				// Clear the current pages
				pages.clear();
				pages.add( new BookPage() );
				
				// Pop the current style
				supplier.get().pop( currentComponent );
				
				// Remove the end component
				endComponent = null;
			}
		}
		return false;
	}
	
	@Override
	public void accept( Supplier< ? extends ComponentTransformerStyle > supplier ) {
		this.supplier= supplier;
	}
}
