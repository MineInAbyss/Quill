package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFont;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.util.Util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/*
 * This class is so slow...
 */
public class BookBinderIndexMinecraft implements Bookbinder< BookIndex > {
	// The width is normally 114
	private int width = 114;
	// The amount of lines is normally 14
	private int lines = 14;
	private Function< String, BananaFont > fontFetcher;
	
	public BookBinderIndexMinecraft( Function< String, BananaFont > fontFetcher ) {
		this( 114, 14, fontFetcher );
	}
	
	public BookBinderIndexMinecraft( int width, int lines, Function< String, BananaFont > fontFetcher ) {
		this.width = width;
		this.lines = lines;
		this.fontFetcher = fontFetcher;
	}

	@Override
	public Book parse( BookIndex object ) {
		Book book = new Book( object.id ).setTitle( object.title ).setAuthor( object.author );

		List< Word > headerWords = new ArrayList< Word >();
		int headerLines = 0;
		if ( !object.getHeaders().isEmpty() ) {
			BookPage page = object.getHeaders().get( 0 );
			headerWords = splitToWords( page.getComponents() );
			int[] headerDim = getDimensions( headerWords );

			headerLines = headerDim[ 1 ];
			if ( headerDim[ 0 ] > 0 ) {
				headerWords.add( new WordNewline( new TextComponent( "\n" ) ) );
				headerLines++;
			}
		}
		
		List< Word > footerWords = new ArrayList< Word >();
		int footerLines = 0;
		if ( !object.getFooters().isEmpty() ) {
			BookPage page = object.getFooters().get( 0 );
			footerWords = splitToWords( page.getComponents() );
			int[] footerDim = getDimensions( footerWords );

			footerLines = footerDim[ 1 ];
		}
		
		int contentHeight = lines - ( headerLines + footerLines );
		
		// Properly account for the header and footer
		for ( BookPage page : object.getPages() ) {
			List< Word > content = new ArrayList< Word >();
			
			int pos = 0;
			int line = 0;
			
			boolean lastNewline = true;
			boolean eol = false;
			
			Deque< Word > contentQueue = new ArrayDeque< Word >( splitToWords( page.getComponents() ) );
			while ( !contentQueue.isEmpty() ) {
				// At this point, determine if the current word will fit within the page
				// If not, then move it onto the next page
				Word word = contentQueue.poll();
				
				int nextPos = pos;
				int nextLine = line;
				if ( word instanceof WordNewline ) {
					nextPos = 0;
					// If it's a new line, then advance but not really
					if ( !eol ) {
						nextLine++;
					}
					eol = false;
				} else if ( word instanceof WordSpace ) {
					WordSpace space = ( WordSpace ) word;
					List< TextComponent > components = space.getComponents();
					int length = getLength( components );

					if ( nextPos != 0 && nextPos + length > width ) {
						// Check if this word is not first, and exceeds the total length
						// If it does, then move it onto its own line and reset the pos
						nextPos = 0;
						nextLine++;
					}
					
					// Check if this word is now the first, and subtract a space worth of whatever
					if ( nextPos == 0 && length > 0 && !components.isEmpty() && !lastNewline ) {
						TextComponent first = components.get( 0 );
						String fontId = first.getFont();
						BananaFont font = fontFetcher.apply( fontId );
						
						// Subtract a space if it's the first character
						length -= font.getCharWidth( ' ', first.isBold() );
					}

					nextPos += length;
					eol = nextPos >= width && nextPos % width == 0;
					
					// Get the final position
					nextLine += nextPos / width;
					nextPos = nextPos % width;
				} else if ( word instanceof WordNonSpace ) {
					WordNonSpace space = ( WordNonSpace ) word;
					List< TextComponent > components = space.getComponents();
					int length = getLength( components );

					if ( nextPos != 0 && nextPos + length > width ) {
						// Check if this word is not first, and exceeds the total length
						// If it does, then move it onto its own line and reset the pos
						nextPos = 0;
						nextLine++;
					}
					
					nextPos += length;
					eol = nextPos >= width && nextPos % width == 0;
					
					// Get the final position
					nextLine += nextPos / width;
					nextPos = nextPos % width;
				}
				
				lastNewline = word instanceof WordNewline;
				
				// Make sure each page gets at least 1 word
				if ( nextLine > contentHeight && !( pos == 0 && line == 0 ) ) {
					// Push it back onto the queue for someone else's problem
					if ( !lastNewline ) {
						contentQueue.push( word );
					}
					// Fill this page to the limit with newlines
					while ( line++ <= contentHeight ) {
						content.add( new WordNewline( new TextComponent( "\n" ) ) );
					}
					
					// Add this page to the book
					List< Word > pageWords = new ArrayList< Word >();
					pageWords.addAll( headerWords );
					pageWords.addAll( content );
					pageWords.addAll( footerWords );
					List< TextComponent > extra = combine( pageWords );
					TextComponent pageComponent = new TextComponent();
					for ( TextComponent extraComponent : extra ) {
						pageComponent.addExtra( extraComponent );
					}
					book.getPages().add( pageComponent );
					
					lastNewline = true;
					content.clear();
					
					pos = 0;
					line = 0;
				} else {
					content.add( word );
					pos = nextPos;
					line = nextLine;
				}
			}
			
			if ( !content.isEmpty() ) {
				while ( line++ <= contentHeight ) {
					content.add( new WordNewline( new TextComponent( "\n" ) ) );
				}
				
				List< Word > pageWords = new ArrayList< Word >();
				pageWords.addAll( headerWords );
				pageWords.addAll( content );
				pageWords.addAll( footerWords );
				List< TextComponent > extra = combine( pageWords );
				TextComponent pageComponent = new TextComponent();
				for ( TextComponent extraComponent : extra ) {
					pageComponent.addExtra( extraComponent );
				}
				book.getPages().add( pageComponent );
			}
		}
		
		return book;
	}
	
	private List< TextComponent > combine( List< Word > words ) {
		List< TextComponent > components = new ArrayList< TextComponent >();
		
		for ( Word word : words ) {
			if ( word instanceof WordNewline ) {
				if ( components.size() > 0 ) {
					TextComponent component = components.get( components.size() - 1 );
					component = component.duplicate();
					component.setText( "\n" );
					components.add( component );
				} else {
					components.add( ( ( WordNewline ) word ).getComponent() );
				}
			} else if ( word instanceof WordSpace ) {
				components.addAll( ( ( WordSpace ) word ).getComponents() );
			} else if ( word instanceof WordNonSpace ) {
				components.addAll( ( ( WordNonSpace ) word ).getComponents() );
			}
		}
		
		return Util.simplify( components );
	}
	
	// This method is *supposed* to convert a list of basecomponents into words...
	// I have no clue if it works, or how to make it simpler
	private List< Word > splitToWords( List< ? extends BaseComponent > components ) {
		List< Word > words = new ArrayList< Word >();
		
		int whitespace = -1;
		Word currentWord = null;

		Deque< BaseComponent > queue = new ArrayDeque< BaseComponent >( components );
		while ( !queue.isEmpty() ) {
			BaseComponent parsing = queue.poll();
			
			// Expand the base component if it contains any subcomponents
			List< BaseComponent > extra = parsing.getExtra();
			if ( extra != null ) {
				for ( int i = extra.size() - 1; i >= 0; i-- ) {
					queue.addFirst( extra.get( i ) );
				}
				parsing.setExtra( new ArrayList< BaseComponent >() );
			}
			
			if ( parsing instanceof TextComponent ) {
				// Now get the current thing or whatever it is
				TextComponent component = ( TextComponent ) parsing;
				String content = component.getText();
				// Now, given content, parse it into words
				if ( !content.isEmpty() ) {
					StringBuilder builder = new StringBuilder();
					// enum that I don't want to make another class for
					for ( char c : content.toCharArray() ) {
						if ( c == ' ' ) {
							if ( whitespace == 0 ) {
								// It was whitespace, now it's not
								// Duplicate the current text component
								TextComponent clone = component.duplicate();
								// Set the text to whatever it was before
								clone.setText( builder.toString() );
								// Construct a new list of components
								List< TextComponent > componentList = new ArrayList< TextComponent >();
								// Check if the current word is also a non space word
								if ( currentWord instanceof WordNonSpace ) {
									// If so, then combine the components
									componentList.addAll( ( ( WordNonSpace ) currentWord ).getComponents() );
								} else if ( currentWord != null ) {
									// Otherwise, add the current word to the list of words
									words.add( currentWord );
								}
								// Add the cloned component
								componentList.add( clone );
								// Create our new word and set it to the current component
								currentWord = new WordNonSpace( componentList );
								
								// Reset the builder
								builder = new StringBuilder();
							}
							builder.append( c );
							whitespace = 1;
						} else if ( c == '\n' ) {
							if ( builder.length() > 0 ) {
								TextComponent clone = component.duplicate();
								clone.setText( builder.toString() );
								List< TextComponent > componentList = new ArrayList< TextComponent >();
								// If it's a non space builder
								if ( whitespace == 0 ) {
									// Check if it can be merged with the current word
									if ( currentWord instanceof WordNonSpace ) {
										// Get the components and add it to the list
										componentList.addAll( ( ( WordNonSpace ) currentWord ).getComponents() );
									} else if ( currentWord != null ) {
										// Otherwise, add the current word to the list of words and continue
										words.add( currentWord );
									}
									componentList.add( clone );
									currentWord = new WordNonSpace( componentList );
								} else if ( whitespace == 1 ) {
									if ( currentWord instanceof WordSpace ) {
										componentList.addAll( ( ( WordSpace ) currentWord ).getComponents() );
									} else if ( currentWord != null ) {
										words.add( currentWord );
									}
									componentList.add( clone );
									currentWord = new WordSpace( componentList );
								}
							}
							
							if ( currentWord != null ) {
								words.add( currentWord );
							}
							
							TextComponent newlineComponent = component.duplicate();
							newlineComponent.setText( "\n" );
							words.add( new WordNewline( newlineComponent ) );
							
							currentWord = null;
							builder = new StringBuilder();
							whitespace = -1;
						} else {
							if ( whitespace == 1 ) {
								TextComponent clone = component.duplicate();
								clone.setText( builder.toString() );
								List< TextComponent > componentList = new ArrayList< TextComponent >();
								if ( currentWord instanceof WordSpace ) {
									componentList.addAll( ( ( WordSpace ) currentWord ).getComponents() );
								} else if ( currentWord != null ) {
									words.add( currentWord );
								}
								componentList.add( clone );
								currentWord = new WordSpace( componentList );
								
								builder = new StringBuilder();
							}
							builder.append( c );
							whitespace = 0;
						}
					}
					if ( builder.length() > 0 ) {
						TextComponent clone = component.duplicate();
						clone.setText( builder.toString() );
						List< TextComponent > componentList = new ArrayList< TextComponent >();
						// If it's a non space builder
						if ( whitespace == 0 ) {
							// Check if it can be merged with the current word
							if ( currentWord instanceof WordNonSpace ) {
								// Get the components and add it to the list
								componentList.addAll( ( ( WordNonSpace ) currentWord ).getComponents() );
							} else if ( currentWord != null ) {
								// Otherwise, add the current word to the list of words and continue
								words.add( currentWord );
							}
							componentList.add( clone );
							currentWord = new WordNonSpace( componentList );
						} else if ( whitespace == 1 ) {
							if ( currentWord instanceof WordSpace ) {
								componentList.addAll( ( ( WordSpace ) currentWord ).getComponents() );
							} else if ( currentWord != null ) {
								words.add( currentWord );
							}
							componentList.add( clone );
							currentWord = new WordSpace( componentList );
						}
					}
				}
			}
		}
		
		if ( currentWord != null ) {
			words.add( currentWord );
		}
		
		return words;
	}
	
	private int[] getDimensions( List< ? extends Word > words ) {
		int pos = 0;
		int line = 1;
		for ( Word word : words ) {
			if ( word instanceof WordNewline ) {
				line++;
			} else if ( word instanceof WordSpace ) {
				WordSpace space = ( WordSpace ) word;
				List< TextComponent > components = space.getComponents();
				int length = getLength( components );

				if ( pos != 0 && pos + length > width ) {
					// Check if this word is not first, and exceeds the total length
					// If it does, then move it onto its own line and reset the pos
					pos = 0;
					line++;
				}
				
				// Check if this word is now the first, and subtract a space worth of whatever
				if ( pos == 0 && length > 0 && !components.isEmpty() ) {
					TextComponent first = components.get( 0 );
					String fontId = first.getFont();
					BananaFont font = fontFetcher.apply( fontId );
					
					// Subtract a space if it's the first character
					length -= font.getCharWidth( ' ', first.isBold() );
				}
				
				pos += length;
				// Get the final position
				line += pos / width;
				pos = pos % width;
			} else if ( word instanceof WordNonSpace ) {
				WordNonSpace space = ( WordNonSpace ) word;
				List< TextComponent > components = space.getComponents();
				int length = getLength( components );

				if ( pos != 0 && pos + length > width ) {
					// Check if this word is not first, and exceeds the total length
					// If it does, then move it onto its own line and reset the pos
					pos = 0;
					line++;
				}
				
				pos += length;
				// Get the final position
				line += pos / width;
				pos = pos % width;
			}
		}
		
		return new int[] { pos, line };
	}
	
	private int getLength( List< ? extends BaseComponent > components ) {
		int length = 0;
		for ( BaseComponent component : components ) {
			if ( component instanceof TextComponent ) {
				TextComponent text = ( TextComponent ) component;
				String fontId = text.getFont();
				BananaFont font = fontFetcher.apply( fontId );
				
				if ( font != null ) {
					length += font.getStringWidth( text.getText(), text.isBold() );
				}
			}
			
			List< BaseComponent > extra = component.getExtra();
			if ( extra != null ) {
				length += getLength( extra );
			}
		}
		return length;
	}
}
