package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class BookComponentParserXML implements BookComponentParser< Node > {
	protected BookComponentParser< Node > parser;
	
	public BookComponentParserXML() {
		parser = this;
	}
	
	public BookComponentParserXML( BookComponentParser< Node > parser ) {
		this.parser = parser;
	}
	
	@Override
	public BookComponent parse( Node node ) {
		if ( node instanceof Element ) {
			Element element = ( Element ) node;
			BookComponentObject object = new BookComponentObject( element.getTagName() );
			
			NamedNodeMap nodeMap = element.getAttributes();
			for ( int i = 0; i < nodeMap.getLength(); i++ ) {
				Node attr = nodeMap.item( i );
				object.getAttributes().put( attr.getNodeName(), attr.getNodeValue() );
			}

			NodeList subNodes = element.getChildNodes();
			for ( int i = 0; i < subNodes.getLength(); i++ ) {
				Node subNode = subNodes.item( i );
				BookComponent component = parser.parse( subNode );
				if ( component != null ) {
					object.getSubElements().add( component );
				}
			}
			
			return object;
		} else if ( node instanceof Text ) {
			return new BookComponentText( node.getTextContent() );
		}
		return null;
	}
}
