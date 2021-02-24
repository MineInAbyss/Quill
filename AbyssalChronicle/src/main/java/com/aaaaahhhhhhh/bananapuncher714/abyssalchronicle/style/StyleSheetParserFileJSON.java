package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import java.nio.file.Path;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Util;
import com.google.gson.JsonObject;

public class StyleSheetParserFileJSON implements StyleSheetParserFile {
	protected StyleSheetParser< JsonObject > parser;
	
	public StyleSheetParserFileJSON( StyleSheetParser< JsonObject > parser ) {
		this.parser = parser;
	}
	
	@Override
	public StyleSheet parse( Path object ) {
		// TODO Json converter...
		return null;
	}

	@Override
	public boolean matches( Path file ) {
		return Util.matchesFile( file, Util.FILE_MATCHER_JSON );
	}
}
