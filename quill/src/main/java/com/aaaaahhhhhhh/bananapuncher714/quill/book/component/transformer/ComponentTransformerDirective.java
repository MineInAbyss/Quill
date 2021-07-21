package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElementDirective;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class ComponentTransformerDirective implements ComponentTransformer {
	public ComponentTransformerDirective( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.asObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "direc" ) ) {
				String command = object.getAttributes().get( "command" );
				if ( command == null ) {
					throw new NullPointerException( "No command found!" );
				} else {
					BookElementDirective direc = new BookElementDirective( command );
					direc.getAttributes().putAll( object.getAttributes() );
					
					pages.get( pages.size() - 1 ).addElement( direc );
					return true;
				}
			}
		}
		return false;
	}
}
