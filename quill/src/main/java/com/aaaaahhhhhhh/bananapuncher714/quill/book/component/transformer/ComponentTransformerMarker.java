package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class ComponentTransformerMarker implements ComponentTransformer {
	public ComponentTransformerMarker( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.asObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "mark" ) ) {
				String mark = object.getAttributes().get( "id" );
				if ( mark == null ) {
					throw new NullPointerException( "No mark id found!" );
				} else {
					pages.get( pages.size() - 1 ).addMarker( mark );
					return true;
				}
			}
		}
		return false;
	}

}
