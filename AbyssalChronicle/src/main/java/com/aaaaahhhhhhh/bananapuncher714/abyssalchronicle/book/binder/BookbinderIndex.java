package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;

import net.md_5.bungee.api.chat.TextComponent;

public class BookbinderIndex implements Bookbinder< BookIndex > {
	@Override
	public Book parse( BookIndex object ) {
		Book book = new Book( object.id ).setTitle( object.title ).setAuthor( object.author );

		// Properly account for the header and footer
		
		// Each book only has 15 lines
		for ( BookPage page : object.getPages() ) {
			TextComponent component = new TextComponent();
			for ( TextComponent extra : page.getComponents() ) {
				component.addExtra( extra );
			}
			book.getPages().add( component );
		}
		
		return book;
	}
}
