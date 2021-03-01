package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHeadParser;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleSheet;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleSheetParser;

public class BookPartParserXML implements BookPartParser< Element > {
	protected BookComponentHeadParser< Element > componentParser;
	protected StyleSheetParser< Element > styleParser;
	
	public BookPartParserXML( BookComponentHeadParser< Element > componentParser, StyleSheetParser< Element > styleParser ) {
		this.componentParser = componentParser;
		this.styleParser = styleParser;
	}
	
	@Override
	public BookPart parse( Element root ) {
		String id = root.getAttribute( "id" );
		String title = root.getAttribute( "title" );
		String author = root.getAttribute( "author" );
		
		BookPart book = new BookPart( id ).setTitle( title ).setAuthor( author );
		
		// Limit the amount of content elements to 1
		NodeList contentNodes = root.getElementsByTagName( "content" );
		if ( contentNodes.getLength() > 1 ) {
			throw new IllegalArgumentException( String.format( "More than one content element defined in book part '%s'!", id ) );
		} else if ( contentNodes.getLength() < 1 ) {
			throw new IllegalArgumentException( String.format( "No content element is defined in book part '%s'!", id ) );
		}
		
		Node contentNode = contentNodes.item( 0 );
		if ( contentNode instanceof Element ) {
			Element contentElement = ( Element ) contentNode;
			if ( contentElement.hasAttribute( "header" ) ) {
				book.setHeaderId( contentElement.getAttribute( "header" ) );
			}
			if ( contentElement.hasAttribute( "footer" ) ) {
				book.setFooterId( contentElement.getAttribute( "footer" ) );
			}
			
			book.setContent( componentParser.parse( contentElement ) );
		} else {
			// TODO Do something here, the content node isn't an element
		}
		
		// Fetch the components
		NodeList componentsNodes = root.getElementsByTagName( "components" );
		if ( componentsNodes.getLength() > 1 ) {
			throw new IllegalArgumentException( String.format( "More than one components element defined in book part '%s'!", id ) );
		}
		
		for ( int j = 0; j < componentsNodes.getLength(); j++ ) {
			Node componentsNode = componentsNodes.item( j );
			NodeList componentNodes = componentsNode.getChildNodes();
			for ( int i = 0; i < componentNodes.getLength(); i++ ) {
				Node componentNode = componentNodes.item( i );
				if ( componentNode instanceof Element ) {
					Element componentElement = ( Element ) componentNode;
					
					if ( componentElement.getTagName().equalsIgnoreCase( "component" ) ) {
						BookComponentHead component = componentParser.parse( componentElement );
						book.getComponents().put( component.getId(), component );
					} else {
						// TODO Incorrect tagname?
					}
				} else {
					// TODO It's not an element?
				}
			}
		}
		
		NodeList includes = root.getElementsByTagName( "includes" );
		for ( int i = 0; i < includes.getLength(); i++ ) {
			Node includesNode = includes.item( i );
			if ( includesNode instanceof Element ) {
				Element includesElement = ( Element ) includesNode;
				NodeList includeList = includesElement.getElementsByTagName( "include" );
				for ( int j = 0; j < includeList.getLength(); j++ ) {
					Node includeNode = includeList.item( j );
					if ( includeNode instanceof Element ) {
						Element includeElement = ( Element ) includeNode;
						if ( includeElement.getTagName().equalsIgnoreCase( "include" ) ) {
							String type = includeElement.getAttribute( "type" );
							String includeId = includeElement.getTextContent();

							IncludeSource source = new IncludeSource( type, includeId );
							book.getIncludes().add( source );
						} else {
							// TODO It's not an include element
						}
					}
				}
			}
		}
		
		// Fetch the styles and include
		// Technically, the book is a stylesheet
		// There's a bunch of extra tags though
		// I don't know if I'm supposed to have strict parsing or not
		// But that would be terrible
		StyleSheet sheet = styleParser.parse( root );
		book.setStylesheet( sheet );
		
		return book;
	}
}
