package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

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
			BookComponentObject object = component.asObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "insert" ) ) {
				String componentId = object.getAttributes().get( "id" );
				if ( componentId == null ) {
					throw new NullPointerException( "No component id found!" );
				} else {
					BookComponentHead subComponent = cache.findComponent( part, NamespacedKey.fromString( componentId ) );
					if ( subComponent != null ) {
						subComponent.getAttributes().putAll( object.getAttributes() );
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
