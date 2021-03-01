package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentTail;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class ComponentTransformerExpand implements ComponentTransformer {
	public ComponentTransformerExpand( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			
			components.push( new BookComponentTail( object ) );
			for ( int i = object.getSubElements().size(); i > 0; i-- ) {
				components.push( object.getSubElements().get( i - 1 ) );
			}
		}
		
		return false;
	}
}
