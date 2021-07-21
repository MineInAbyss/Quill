package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElementText;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.Space;

import net.md_5.bungee.api.chat.TextComponent;

public class ComponentTransformerNegativeSpaces implements ComponentTransformer {
	private Supplier< String > fontSupplier;
	
	public ComponentTransformerNegativeSpaces( Supplier< String > fontSupplier ) {
		this.fontSupplier = fontSupplier;
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.asObjectComponent();
			String tagName = object.getTagName().toLowerCase();
			if ( tagName.matches( "_add\\d+" ) ) {
				int value = Integer.parseInt( tagName.substring( 4 ) );
				
				BookElementText element = pages.get( pages.size() - 1 ).addNewTextElement();
				TextComponent spaceComp = element.getComponent();
				spaceComp.setText( Space.getSpaceFor( value ) );
				spaceComp.setFont( fontSupplier.get() );
				
				return true;
			} else if ( tagName.matches( "_sub\\d+" ) ) {
				int value = Integer.parseInt( tagName.substring( 4 ) );
				
				BookElementText element = pages.get( pages.size() - 1 ).addNewTextElement();
				TextComponent spaceComp = element.getComponent();
				spaceComp.setText( Space.getSpaceFor( -value ) );
				spaceComp.setFont( fontSupplier.get() );
				
				return true;
			}
		}
		return false;
	}

}
