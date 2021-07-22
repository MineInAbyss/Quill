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

public class ComponentTransformerGoto implements ComponentTransformer {
	public ComponentTransformerGoto( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.asObjectComponent();
			
			if ( object.getTagName().equalsIgnoreCase( "go" ) ) {
				String mark = object.getAttributes().get( "to" );
				if ( mark == null ) {
					throw new NullPointerException( "No book id found!" );
				} else {
					BookComponentObject click = new BookComponentObject( ComponentTransformerClick.ID );
					click.getAttributes().putAll( object.getAttributes() );
					click.getAttributes().put( ComponentTransformerClick.ACTION, Action.CHANGE_PAGE.name() );
					click.getAttributes().put( ComponentTransformerClick.VALUE, mark );
					components.addFirst( click );
					if ( object.getSubElements().isEmpty() ) {
						click.getSubElements().add( new BookComponentText( mark ) );
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
