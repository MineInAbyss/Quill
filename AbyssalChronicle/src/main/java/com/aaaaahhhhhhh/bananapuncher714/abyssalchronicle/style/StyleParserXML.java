package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.util.Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StyleParserXML implements StyleParser< Element > {
	private static JsonParser JSON_PARSER = new JsonParser();
	
	@Override
	public Style parse( Element root ) {
		Style style = new Style( root.getAttribute( "id" ) );
		// Add parents for both extends and inherits attributes
		addParents( style, root.getAttribute( "extends" ) );
		addParents( style, root.getAttribute( "inherits" ) );
		
		// Load the properties
		NodeList propertyNodes = root.getElementsByTagName( "properties" );
		JsonObject propertyObject = new JsonObject();
		for ( int i = 0; i < propertyNodes.getLength(); i++ ) {
			// Loop over each properties node, even though there should technically be only 1
			String propertyString = Util.nodeToString( propertyNodes.item( 0 ) );
			// Strip the outer tags
			propertyString = Util.stripOuterTag( propertyString );
			// Decode the XML entities
			propertyString = StringEscapeUtils.unescapeXml( propertyString );
			// Convert it into JSON
			JsonObject json = JSON_PARSER.parse( propertyString ).getAsJsonObject();
			
			Util.mergeJson( propertyObject, json );
		}
		
		// Get the default properties
		NodeList defaultNodes = root.getElementsByTagName( "defaults" );
		JsonObject defaultObject = new JsonObject();
		for ( int i = 0; i < defaultNodes.getLength(); i++ ) {
			// Loop over each properties node, even though there should technically be only 1
			String propertyString = Util.nodeToString( defaultNodes.item( 0 ) );
			// Strip the outer tags
			propertyString = Util.stripOuterTag( propertyString );
			// Decode the XML entities
			propertyString = StringEscapeUtils.unescapeXml( propertyString );
			// Convert it into JSON
			JsonObject json = JSON_PARSER.parse( propertyString ).getAsJsonObject();
			
			Util.mergeJson( defaultObject, json );
		}
		
		// Construct the properties for the given style
		StyleProperty property = new StyleProperty( propertyObject, defaultObject );
		style.setProperties( property );
		
		NodeList formattingList = root.getElementsByTagName( "formatting" );
		for ( int i = 0; i < formattingList.getLength(); i++ ) {
			Node formattingNode = formattingList.item( i );
			NodeList formattingNodeList = formattingNode.getChildNodes();
			for ( int j = 0; j < formattingNodeList.getLength(); j++ ) {
				Node formatNode = formattingNodeList.item( j );
				if ( formatNode instanceof Element ) {
					Element formatElement = ( Element ) formatNode;
					if ( formatElement.getTagName().equalsIgnoreCase( "format" ) ) {
						String key = formatElement.getAttribute( "type" );
						String value = formatElement.getTextContent();
						style.getValues().put( key, value );
					} else {
						// Not format tag?
					}
				}
			}
		}

		return style;
	}
	
	private static void addParents( Style style, String attribute ) {
		String[] parents = attribute.split( "\\s+" );
		for ( String parent : parents ) {
			if ( !parent.isEmpty() ) {
				style.getParents().add( NamespacedKey.fromString( parent ) );
			}
		}
	}
}
