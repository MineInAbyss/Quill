package com.aaaaahhhhhhh.bananapuncher714.quill.style;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/*
 * It says object, but I really don't know what else it should be
 * Snakeyaml only gives me an object
 * 
 * Either way, convert it to a json object and parse it there.
 * There's nothing YAML has over JSON anyways
 */
public class StyleSheetParserYamlToJson implements StyleSheetParser< Object > {
	private static final Gson GSON = new GsonBuilder().create();
	
	private StyleSheetParser< JsonObject > parser;
	
	public StyleSheetParserYamlToJson( StyleSheetParser< JsonObject > parser ) {
		this.parser = parser;
	}
	
	@Override
	public StyleSheet parse( Object object ) {
		JsonElement json = GSON.toJsonTree( object );
		
		if ( json.isJsonObject() ) {
			return parser.parse( json.getAsJsonObject() );
		}
		
		return null;
	}
}
