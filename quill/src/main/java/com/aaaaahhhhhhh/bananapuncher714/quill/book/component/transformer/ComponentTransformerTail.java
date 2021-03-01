package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentTail;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class ComponentTransformerTail implements ComponentTransformer {
	public ComponentTransformerTail( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isEndComponent() ) {
			BookComponentTail tail = component.getAsEndComponent();
			
			System.out.println( "Reached the end of " + tail.getHead().getTagName() );
		}
		return false;
	}
}
