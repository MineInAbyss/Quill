package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElement;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookElementBaseComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFont;
import com.aaaaahhhhhhh.bananapuncher714.quill.util.Util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

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
		Map< String, Integer > markTracker = new HashMap< String, Integer >();
		Map< String, ParsedElement > elements = new HashMap< String, ParsedElement >();
		String header = object.getHeader();
		String footer = object.getFooter();
		
		for ( Entry< String, List< BookPage > > entry : object.getComponents().entrySet() ) {
			List< BookPage > pages = entry.getValue();
			
			List< Word > words = splitToWords( pages.get( 0 ).getComponents() );
			ParsedElement element = new ParsedElement( words );
			elements.put( entry.getKey(), element );
		}
		
		int contentHeight = lines;
		boolean padHeader = false;
		ParsedElement headerElement = header == null ? null : elements.get( header );
		if ( headerElement != null ) {
			contentHeight -= headerElement.height;
			padHeader = headerElement.width > 0;
		}
		ParsedElement footerElement = footer == null ? null : elements.get( footer );
		if ( footerElement != null ) {
			contentHeight -= footerElement.height;
		}
		if ( padHeader ) {
			contentHeight--;
		}
		
		// Properly account for the header and footer
		for ( BookPage page : object.getPages() ) {
			List< Word > content = new ArrayList< Word >();
			
			int pos = 0;
			int line = 0;
			
			boolean lastNewline = true;
			
			Deque< Word > contentQueue = new ArrayDeque< Word >( splitToWords( page.getComponents() ) );
			while ( !contentQueue.isEmpty() ) {
				// At this point, determine if the current word will fit within the page
				// If not, then move it onto the next page
				Word word = contentQueue.poll();
				
				if ( word instanceof WordMark ) {
					String mark = ( ( WordMark ) word ).getMark();
					markTracker.put( mark, book.getPages().size() + 1 );
					// Skip all the regular page processing whatever
					continue;
				} else if ( word instanceof WordDirective ) {
					WordDirective direc = ( WordDirective ) word;
					String command = direc.getCommand();
					Map< String, String > attrib = direc.getAttributes();
					
					if ( command.equalsIgnoreCase( "nofooter" ) ) {
						footer = null;
					} else if ( command.equalsIgnoreCase( "noheader" ) ) {
						header = null;
					} else if ( command.equalsIgnoreCase( "footer" ) ) {
						String newFooter = attrib.get( "value" );
						if ( newFooter != null ) {
							footer = newFooter;
						}
					} else if ( command.equalsIgnoreCase( "header" ) ) {
						String newHeader = attrib.get( "value" );
						if ( newHeader != null ) {
							header = newHeader;
						}
					}
					
					continue;
				}
				
				int nextPos = pos;
				int nextLine = line;
				if ( word instanceof WordNewline ) {
					nextPos = 0;
					nextLine++;
				} else if ( word instanceof WordSpace ) {
					WordSpace space = ( WordSpace ) word;
					List< TextComponent > components = space.getComponents();

					/*
					 * Space behavior:
					 * If it exceeds the width, then move onto the next line and don't add it to the length
					 * Otherwise, add the space to the total length
					 */
					Deque< BaseComponent > queue = new ArrayDeque< BaseComponent >( components );
					while ( !queue.isEmpty() ) {
						BaseComponent base = queue.poll().duplicate();
						
						List< BaseComponent > extra = base.getExtra();
						if ( extra != null ) {
							for ( int i = extra.size() - 1; i >= 0; i-- ) {
								queue.addFirst( extra.get( i ) );
							}
							base.setExtra( new ArrayList< BaseComponent >() );
						}
						
						if ( base instanceof TextComponent ) {
							TextComponent text = ( TextComponent ) base;
							String textContent = text.getText();
							BananaFont font = fontFetcher.apply( text.getFont() );
							
							for ( char c : textContent.toCharArray() ) {
								int charLength = font.getCharWidth( c, text.isBold() );
								nextPos += charLength;

								if ( nextPos > width ) {
									nextPos = 0;
									nextLine++;
								}
							}
						}
					}
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
					
					/*
					 * Word behavior
					 * If the pos exceeds the width, then move it onto a newline
					 * Keep adding each char until it exceeds the width and pos != char length
					 */
					Deque< BaseComponent > queue = new ArrayDeque< BaseComponent >( components );
					while ( !queue.isEmpty() ) {
						BaseComponent base = queue.poll().duplicate();
						
						List< BaseComponent > extra = base.getExtra();
						if ( extra != null ) {
							for ( int i = extra.size() - 1; i >= 0; i-- ) {
								queue.addFirst( extra.get( i ) );
							}
							base.setExtra( new ArrayList< BaseComponent >() );
						}
						
						if ( base instanceof TextComponent ) {
							TextComponent text = ( TextComponent ) base;
							String textContent = text.getText();
							BananaFont font = fontFetcher.apply( text.getFont() );
							
							for ( char c : textContent.toCharArray() ) {
								int charLength = font.getCharWidth( c, text.isBold() );
								nextPos += charLength;

								if ( nextPos > width && nextPos != charLength ) {
									nextPos = charLength;
									nextLine++;
								}
							}
						}
					}
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
					if ( headerElement != null ) {
						pageWords.addAll( headerElement.words );
					}
					if ( padHeader ) {
						pageWords.add( new WordNewline( new TextComponent( "\n" ) ) );
					}
					pageWords.addAll( content );
					if ( footerElement != null ) {
						pageWords.addAll( footerElement.words );
					}
					List< TextComponent > extra = combine( pageWords );
					TextComponent pageComponent = new TextComponent();
					for ( TextComponent extraComponent : extra ) {
						pageComponent.addExtra( extraComponent );
					}
					book.getPages().add( pageComponent );
					
					lastNewline = true;
					content.clear();
					
					// Recalculate the total lines of content that are availble
					contentHeight = lines;
					headerElement = header == null ? null : elements.get( header );
					if ( headerElement != null ) {
						contentHeight -= headerElement.height;
						padHeader = headerElement.width > 0;
					} else {
						padHeader = false;
					}
					footerElement = footer == null ? null : elements.get( footer );
					if ( footerElement != null ) {
						contentHeight -= footerElement.height;
					}
					if ( padHeader ) {
						contentHeight--;
					}
					
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
				if ( headerElement != null ) {
					pageWords.addAll( headerElement.words );
				}
				if ( padHeader ) {
					pageWords.add( new WordNewline( new TextComponent( "\n" ) ) );
				}
				pageWords.addAll( content );
				if ( footerElement != null ) {
					pageWords.addAll( footerElement.words );
				}
				List< TextComponent > extra = combine( pageWords );
				TextComponent pageComponent = new TextComponent();
				for ( TextComponent extraComponent : extra ) {
					pageComponent.addExtra( extraComponent );
				}
				book.getPages().add( pageComponent );
				
				contentHeight = lines;
				headerElement = header == null ? null : elements.get( header );
				if ( headerElement != null ) {
					contentHeight -= headerElement.height;
					padHeader = headerElement.width > 0;
				} else {
					padHeader = false;
				}
				footerElement = footer == null ? null : elements.get( footer );
				if ( footerElement != null ) {
					contentHeight -= footerElement.height;
				}
				if ( padHeader ) {
					contentHeight--;
				}
			}
		}
		
		// Update all page click elements with the proper page
		Deque< BaseComponent > check = new ArrayDeque< BaseComponent >();
		check.addAll( book.getPages() );
		while ( !check.isEmpty() ) {
			BaseComponent first = check.poll();
			
			ClickEvent click = first.getClickEvent();
			if ( click != null && click.getAction() == Action.CHANGE_PAGE ) {
				String mark = click.getValue();
				if ( markTracker.containsKey( mark ) ) {
					first.setClickEvent( new ClickEvent( click.getAction(), Integer.toString( markTracker.get( mark ) ) ) );
				} else {
					// Warning?!
				}
			}
			
			if ( first.getExtra() != null ) {
				check.addAll( first.getExtra() );
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
	private List< Word > splitToWords( Collection< ? extends BookElement > components ) {
		List< Word > words = new ArrayList< Word >();
		
		int whitespace = -1;
		Word currentWord = null;

		Deque< BookElement > queue = new ArrayDeque< BookElement >( components );
		while ( !queue.isEmpty() ) {
			BookElement parsingElement = queue.poll();
			
			if ( parsingElement.isBaseComponentElement() ) {
				BaseComponent parsing = parsingElement.asBaseComponentElement().getComponent();
				// Expand the base component if it contains any subcomponents
				List< BaseComponent > extra = parsing.getExtra();
				if ( extra != null ) {
					for ( int i = extra.size() - 1; i >= 0; i-- ) {
						queue.addFirst( new BookElementBaseComponent( extra.get( i ) ) );
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
			} else if ( parsingElement.isMarkElement() ) {
				words.add( new WordMark( parsingElement.asMarkerElement().getMark() ) );
			} else if ( parsingElement.isDirectiveElement() ) {
				words.add( parsingElement.asDirectiveElement().toWord() );
			}
		}
		
		if ( currentWord != null ) {
			words.add( currentWord );
		}
		
		return words;
	}
	
	private int[] getDimensions( Collection< ? extends Word > words ) {
		int pos = 0;
		int line = 1;
		for ( Word word : words ) {
			if ( word instanceof WordNewline ) {
				pos = 0;
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
	
	private int getLength( Collection< ? extends BaseComponent > components ) {
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
	
	private class ParsedElement {
		Collection< Word > words;
		int width;
		int height;
		
		ParsedElement( Collection< Word > words ) {
			this.words = words;
			int[] dim = getDimensions( words );
			width = dim[ 0 ];
			height = dim[ 1 ];
		}
	}
}
