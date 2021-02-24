package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;

public class ComponentTransformerInsert implements ComponentTransformer {
	private CatalogBuildable cache;
	private BookPart part;
	
	public ComponentTransformerInsert( CommandSender sender, CatalogBuildable cache, BookPart part ) {
		this.cache = cache;
		this.part = part;
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "insert" ) ) {
				String componentId = object.getAttributes().get( "id" );
				if ( componentId == null ) {
					throw new NullPointerException( "No component id found!" );
				} else {
					BookComponentHead subComponent = cache.findComponent( part, NamespacedKey.fromString( componentId ) );
					if ( subComponent != null ) {
						components.addFirst( subComponent );
					} else {
						throw new NullPointerException( String.format( "No component with the id '%s' found!", componentId ) );
					}
				}
			}
		}
		
		return false;
	}
}
