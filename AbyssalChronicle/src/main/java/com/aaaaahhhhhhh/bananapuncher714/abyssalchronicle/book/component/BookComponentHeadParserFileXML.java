package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Util;

public class BookComponentHeadParserFileXML implements BookComponentHeadParserFile {
	private BookComponentHeadParser< Element > parser;
	private Function< Path, String > idCreator;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	
	public BookComponentHeadParserFileXML( Function< Path, String > idCreator, BookComponentHeadParser< Element > parser ) throws ParserConfigurationException {
		this.idCreator = idCreator;
		this.parser = parser;	
		
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}
	
	@Override
	public BookComponentHead parse( Path path ) {
		String name = idCreator.apply( path );
		
		StringBuilder contentBuilder = new StringBuilder( String.format( "<component id=\"%s\">", name ) );
		
		try {
			Files.lines( path, StandardCharsets.UTF_8 ).forEach( s -> contentBuilder.append( s ).append( "\n" ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		contentBuilder.append( "</component>" );
		
		try {
			Document doc = builder.parse( new ByteArrayInputStream( contentBuilder.toString().getBytes() ) );
			return parser.parse( doc.getDocumentElement() );
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean matches( Path file ) {
		return Util.matchesFile( file, Util.FILE_MATCHER_XML );
	}
}
