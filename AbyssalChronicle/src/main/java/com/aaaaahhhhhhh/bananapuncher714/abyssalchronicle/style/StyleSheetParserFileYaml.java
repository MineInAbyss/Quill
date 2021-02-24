package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.Yaml;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Util;

public class StyleSheetParserFileYaml implements StyleSheetParserFile {
	protected static final Yaml YAML = new Yaml();
	protected StyleSheetParser< Object > parser;
	
	public StyleSheetParserFileYaml( StyleSheetParser< Object > parser ) {
		this.parser = parser;
	}
	
	@Override
	public StyleSheet parse( Path path ) {
		try ( BufferedReader reader = Files.newBufferedReader( path ) ) {
			Object object = YAML.load( reader );
			return parser.parse( object );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean matches( Path file ) {
		return Util.matchesFile( file, Util.FILE_MATCHER_YAML );
	}

}
