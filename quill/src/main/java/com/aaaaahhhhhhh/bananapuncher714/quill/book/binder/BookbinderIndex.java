package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElement;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;

import net.md_5.bungee.api.chat.TextComponent;

public class BookbinderIndex implements Bookbinder< BookIndex > {
	@Override
	public Book parse( BookIndex object ) {
		Book book = new Book( object.id ).setTitle( object.title ).setAuthor( object.author );

		// Properly account for the header and footer
		
		// Each book only has 15 lines
		for ( BookPage page : object.getPages() ) {
			TextComponent component = new TextComponent();
			for ( BookElement extra : page.getComponents() ) {
				if ( extra.isTextElement() ) {
					component.addExtra( extra.asTextElement().getComponent() );
				}
			}
			book.getPages().add( component );
		}
		
		return book;
	}
}
