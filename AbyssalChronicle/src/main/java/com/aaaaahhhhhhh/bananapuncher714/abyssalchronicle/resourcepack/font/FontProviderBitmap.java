package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FontProviderBitmap extends FontProvider {
	protected NamespacedKey file;
	protected String[] chars;
	protected int ascent = 0;
	protected int height = 16;
	
	public FontProviderBitmap( NamespacedKey path, String[] chars ) {
		super( "bitmap" );
		file = path;
		this.chars = chars;
	}
	
	public NamespacedKey getFile() {
		return file;
	}

	public String[] getChars() {
		return chars;
	}

	public int getAscent() {
		return ascent;
	}

	public FontProviderBitmap setAscent( int ascent ) {
		this.ascent = ascent;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public FontProviderBitmap setHeight( int height ) {
		this.height = height;
		return this;
	}
	
	public JsonObject toJsonObject() {
		JsonObject object = super.toJsonObject();
		object.addProperty( "file", file.toString() );
		object.addProperty( "ascent", ascent );
		object.addProperty( "height", height );
		JsonArray arr = new JsonArray();
		for ( String s : chars ) {
			arr.add( s );
		}
		object.add( "chars", arr );
		
		return object;
	}
}
