package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BookComponentHeadParserXML implements BookComponentHeadParser< Element > {
	protected BookComponentParser< Node > parser;
	
	public BookComponentHeadParserXML( BookComponentParser< Node > parser ) {
		this.parser = parser;
	}
	
	@Override
	public BookComponentHead parse( Element root ) {
		BookComponentHead component = new BookComponentHead( root.getAttribute( "id" ) );

		BookComponent parsedComponent = parser == null ? null : parser.parse( root );
		if ( parsedComponent != null && parsedComponent.isObjectComponent() ) {
			BookComponentObject object = parsedComponent.asObjectComponent();
			
			String styleAttribute = object.getAttributes().getOrDefault( "styles", "" );
			String[] styles = styleAttribute.split( "\\s+" );
			for ( String style : styles ) {
				if ( !style.isEmpty() ) {
					component.getStyles().add( style );
				}
			}
			
			component.getAttributes().putAll( object.getAttributes() );
			component.getSubElements().addAll( object.getSubElements() );
		} else {
			NamedNodeMap nodeMap = root.getAttributes();
			for ( int i = 0; i < nodeMap.getLength(); i++ ) {
				Node attr = nodeMap.item( i );
				component.getAttributes().put( attr.getNodeName(), attr.getNodeValue() );
			}

			NodeList subNodes = root.getChildNodes();
			for ( int i = 0; i < subNodes.getLength(); i++ ) {
				Node subNode = subNodes.item( i );
				component.getSubElements().add( parser.parse( subNode ) );
			}
			
			String styleAttribute = root.getAttribute( "styles" );
			String[] styles = styleAttribute.split( "\\s+" );
			for ( String style : styles ) {
				if ( !style.isEmpty() ) {
					component.getStyles().add( style );
				}
			}
		}
		
		return component;
	}
}
