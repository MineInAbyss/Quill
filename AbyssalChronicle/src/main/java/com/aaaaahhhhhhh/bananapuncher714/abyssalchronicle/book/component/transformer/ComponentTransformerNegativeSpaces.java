package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.Space;

public class ComponentTransformerNegativeSpaces implements ComponentTransformer {
	public ComponentTransformerNegativeSpaces( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			String tagName = object.getTagName().toLowerCase();
			if ( tagName.matches( "_add\\d+" ) ) {
				int value = Integer.parseInt( tagName.substring( 4 ) );
				
				pages.get( pages.size() - 1 ).addNewComponent().setText( Space.getSpaceFor( value ) );
				
				return true;
			} else if ( tagName.matches( "_sub\\d+" ) ) {
				int value = Integer.parseInt( tagName.substring( 4 ) );
				
				pages.get( pages.size() - 1 ).addNewComponent().setText( Space.getSpaceFor( -value ) );
				
				return true;
			}
		}
		return false;
	}

}
