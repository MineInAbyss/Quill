package com.aaaaahhhhhhh.bananapuncher714.quill.style;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.IncludeSource;

public class StyleSheetParserXML implements StyleSheetParser< Element > {
	private StyleParser< Element > parser;
	
	public StyleSheetParserXML( StyleParser< Element > parser ) {
		this.parser = parser;
	}
	
	@Override
	public StyleSheet parse( Element document ) {
		StyleSheet sheet = new StyleSheet( document.getAttribute( "id" ) );
		// Don't misspell the variables...
		// Add the list of includes
		NodeList includesList = document.getElementsByTagName( "includes" );
		for ( int i = 0; i < includesList.getLength(); i++ ) {
			Node includes = includesList.item( i );
			NodeList includeList = includes.getChildNodes();
			for ( int j = 0; j < includeList.getLength(); j++ ) {
				Node include = includeList.item( j );
				if ( include instanceof Element ) {
					Element includeElement = ( Element ) include;
					String name = includeElement.getTagName();
					try {
						if ( name.equalsIgnoreCase( "include" ) ) {
							String includeId = includeElement.getTextContent();
							String type = includeElement.getAttribute( "type" );
							sheet.getIncludes().add( new IncludeSource( type, includeId ) );
						} else {
							throw new IllegalArgumentException( String.format( "Unknown include tag '%s'", name ) );
						}
					} catch ( IllegalArgumentException e ) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// Parse the various styles
		NodeList stylesNodes = document.getElementsByTagName( "styles" );
		for ( int i = 0; i < stylesNodes.getLength(); i++ ) {
			Node stylesNode = stylesNodes.item( i );
			if ( stylesNode instanceof Element ) {
				Element stylesElement = ( Element ) stylesNode;
				
				NodeList styleNodes = stylesElement.getElementsByTagName( "style" );
				for ( int j = 0; j < styleNodes.getLength(); j++ ) {
					Node styleNode = styleNodes.item( j );
					if ( styleNode instanceof Element ) {
						Element styleElement = ( Element ) styleNode;
						Style style = parser.parse( styleElement );
						sheet.getStyles().put( style.getId(), style );
					}
					
				}
				
			}
		}
		
		return sheet;
	}
}
