package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFont;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BookBinderIndexMinecraft implements Bookbinder< BookIndex > {
	private int width = 114;
	private int lines = 15;
	private Function< String, BananaFont > fontFetcher;
	
	public BookBinderIndexMinecraft( Function< String, BananaFont > fontFetcher ) {
		this( 114, 15, fontFetcher );
	}
	
	public BookBinderIndexMinecraft( int width, int lines, Function< String, BananaFont > fontFetcher ) {
		this.width = width;
		this.lines = lines;
		this.fontFetcher = fontFetcher;
	}

	@Override
	public Book parse( BookIndex object ) {
		Book book = new Book( object.id ).setTitle( object.title ).setAuthor( object.author );

		/*
		 * So, to simplify the things
		 * First, convert every page into a list of word components
		 * Do this for the header and footer too.
		 * Let the header and footer be measured in line length
		 * Split the words into subpages
		 * Add them together and merge what can be merged.
		 * 
		 * Firstly, convert the header and footer to a list of words
		 * Then, calculate the total line size for both
		 * 
		 * Determine the leftover amount for the actual content
		 * Then, build up the words for whatever
		 */
		
		// Properly account for the header and footer
		
		// Each book only has 15 pages
		for ( BookPage page : object.getPages() ) {
			int pos = 0;
			int line = 0;
			boolean word = false;
			TextComponent component = new TextComponent();
			Deque< BaseComponent > components = new ArrayDeque< BaseComponent >();
			components.addAll( page.getComponents() );
			while ( !components.isEmpty() ) {
				BaseComponent parsing = components.poll();
				
				// Expand the base component if it contains any subcomponents
				List< BaseComponent > extra = parsing.getExtra();
				if ( extra != null ) {
					for ( int i = extra.size() - 1; i >= 0; i++ ) {
						components.addFirst( extra.get( i ) );
					}
					extra.clear();
				}
				
				// Check if the current component can be added
				if ( parsing instanceof TextComponent ) {
					TextComponent text = ( TextComponent ) parsing;
					String content = text.getText();
					BananaFont font = fontFetcher.apply( text.getFont() );
					int spaceWidth = font.getCharWidth( ' ', text.isBold() );
					
					// Split the content into words and whitespace
					Deque< SomeWord > words = new ArrayDeque< SomeWord >();
					splitAndFill( content, words );
					
					// The next step is to loop through the strings and calculate the new position
					// If it's about to break, then split the current component
					int strLength = 0;
					for ( SomeWord str : words ) {
						int length = font.getStringWidth( str.text, text.isBold() );
						pos += length;
						strLength += length;
						
						if ( str.isNewline() ) {
							pos = 0;
							line++;
						} else if ( pos > width ) {
							// So, it splits the string into words. Either it is a full whitespace, or non-whitespaced word
							// If the line starts with a whitespace character, then remove one space
							// Also need to connect components that don't have a space in between
							
							// Anyways, get the amount of lines incremented
							// as well as the new position
							// Check if it's a whitespace, if so, then decrease length by a space
							if ( str.isWhitespace() ) {
								length -= spaceWidth;
							}
							
							pos = length % width;
							line += ( length / width ) + 1;
						} else {
							// No, it didn't
						}
						
						// Check if lines exceeds the max size
						if ( line > lines ) {
							// Split the current text component into a new page
							String first = content.substring( 0, strLength );
							String last = content.substring( strLength );
							TextComponent second = text.duplicate();
							text.setText( last );
							second.setText( first );
							
							component.addExtra( second );
							book.getPages().add( component );
							component = new TextComponent();
							
							line -= lines;
						} else {
							// Nothing, I guess
						}
						
						word = !( str.isNewline() || str.isWhitespace() );
					}
					component.addExtra( parsing );
				}
			}
			if ( component.getExtra() != null ) {
				book.getPages().add( component );
			}
		}
		
		return book;
	}
	
	private List< Word > splitToWords( List< TextComponent > components ) {
		List< Word > words = new ArrayList< Word >();
		
		return words;
	}
	
	private void splitAndFill( String content, Collection< SomeWord > words ) {
		if ( !content.isEmpty() ) {
			StringBuilder builder = new StringBuilder();
			int whitespace = -1;
			for ( char c : content.toCharArray() ) {
				if ( c == ' ' ) {
					if ( whitespace == 0 ) {
						words.add( new SomeWord( builder.toString() ) );
						builder = new StringBuilder();
					}
					builder.append( c );
				} else if ( c == '\n' ) {
					words.add( new SomeWord( builder.toString() ) );
					words.add( new SomeWord( "\n" ) );
					builder = new StringBuilder();
					whitespace = -1;
				} else {
					if ( whitespace == 1 ) {
						words.add( new SomeWord( builder.toString() ) );
						builder = new StringBuilder();
					}
					builder.append( c );
					whitespace = 0;
				}
			}
		}
	}
	
	private class SomeWord {
		public final String text;
		
		public SomeWord( String text ) {
			this.text = text;
		}
		
		public boolean isWhitespace() {
			return text.startsWith( " " );
		}
		
		public boolean isNewline() {
			return text.equals( "\n" );
		}
	}
}
