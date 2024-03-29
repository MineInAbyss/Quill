package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElement;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentTail;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentTransformerClick implements ComponentTransformer {
	public static final String ID = "click";
	public static final String ACTION = "action";
	public static final String VALUE = "value";
	
	private BookComponentObject currentComponent;
	private List< BookPage > oldPages;
	private ClickEvent clickEvent;
	
	public ComponentTransformerClick( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() && currentComponent == null ) {
			BookComponentObject object = component.asObjectComponent();
			if ( object.getTagName().equalsIgnoreCase( ID ) ) {
				currentComponent = object;

				// Clear and re-fill the current list of pages
				oldPages = new ArrayList< BookPage >( pages );
				
				pages.clear();
				pages.add( new BookPage() );
				
				// Set the current click actions
				Action action;
				if ( object.getAttributes().containsKey( ACTION ) ) {
					action = Action.valueOf( object.getAttributes().get( ACTION ) );
				} else {
					throw new NullPointerException( "Missing click event action!" );
				}
				
				String option;
				if ( object.getAttributes().containsKey( VALUE ) ) {
					option = object.getAttributes().get( VALUE );
				} else {
					throw new NullPointerException( "Missing click event value!" );
				}
				
				clickEvent = new ClickEvent( action, option );
			}
		} else if ( component.isEndComponent() && currentComponent != null ) {
			BookComponentTail end = component.asEndComponent();
			if ( end.getHead() == currentComponent ) {
				for ( BookPage page : pages ) {
					for ( BookElement subComponent : page.getComponents() ) {
						if ( subComponent.isTextElement() ) {
							subComponent.asTextElement().getComponent().setClickEvent( clickEvent );
						}
					}
				}
				
				// Merge the last page of the old pages and the first page of the new pages, if it exists
				if ( !oldPages.isEmpty() && !pages.isEmpty() ) {
					oldPages.get( oldPages.size() - 1 ).getComponents().addAll( pages.remove( 0 ).getComponents() );
				}
				pages.addAll( 0, oldPages );
				
				// Reset
				oldPages = null;
				currentComponent = null;
				clickEvent = null;
			}
		}
		return false;
	}

}
