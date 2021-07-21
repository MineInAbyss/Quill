package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentText;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

import net.md_5.bungee.api.chat.ClickEvent.Action;

public class ComponentTransformerRedirect implements ComponentTransformer {
	public ComponentTransformerRedirect( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "redirect" ) ) {
				String bookId = object.getAttributes().get( "to" );
				if ( bookId == null ) {
					throw new NullPointerException( "No book id found!" );
				} else {
					BookComponentObject click = new BookComponentObject( ComponentTransformerClick.ID );
					click.getAttributes().put( ComponentTransformerClick.ACTION, Action.RUN_COMMAND.name() );
					click.getAttributes().put( ComponentTransformerClick.VALUE, "/book read " + bookId );
					components.addFirst( click );
					if ( object.getSubElements().isEmpty() ) {
						click.getSubElements().add( new BookComponentText( bookId ) );
					} else {
						click.getSubElements().addAll( object.getSubElements() );
					}
					return true;
				}
			}
		}
		return false;
	}
}
