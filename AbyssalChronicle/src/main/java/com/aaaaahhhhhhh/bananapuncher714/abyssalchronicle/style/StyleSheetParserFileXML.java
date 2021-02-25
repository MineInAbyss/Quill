package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.util.Util;

public class StyleSheetParserFileXML implements StyleSheetParserFile {
	protected StyleSheetParser< Element > parser;
	protected DocumentBuilderFactory factory;
	protected DocumentBuilder builder;

	public StyleSheetParserFileXML( StyleSheetParser< Element > parser ) throws ParserConfigurationException {
		this.parser = parser;
		
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}

	@Override
	public StyleSheet parse( Path object ) {
		try {
			Document doc = builder.parse( object.toFile() );
			return parser.parse( doc.getDocumentElement() );
		} catch ( SAXException | IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean matches( Path file ) {
		return Util.matchesFile( file, Util.FILE_MATCHER_XML );
	}
}
