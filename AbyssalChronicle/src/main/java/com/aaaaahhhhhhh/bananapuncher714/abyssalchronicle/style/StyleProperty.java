package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Util;
import com.google.gson.JsonObject;

public class StyleProperty {
	protected JsonObject properties;
	protected JsonObject defaults;
	
	public StyleProperty() {
		this( new JsonObject(), new JsonObject() );
	}
	
	public StyleProperty( JsonObject properties, JsonObject defaults ) {
		this.properties = properties;
		this.defaults = defaults;
	}

	public JsonObject getProperties() {
		return properties;
	}

	public void setProperties( JsonObject properties ) {
		this.properties = properties;
	}

	public JsonObject getDefaults() {
		return defaults;
	}

	public void setDefaults( JsonObject defaults ) {
		this.defaults = defaults;
	}
	
	public StyleProperty merge( StyleProperty other ) {
		Util.mergeJsonNoOverwrite( properties, other.properties );
		Util.mergeJsonNoOverwrite( defaults, other.defaults );
		
		return this;
	}
	
	public JsonObject flatten() {
		JsonObject copy = Util.deepCopy( properties, JsonObject.class );
		Util.mergeJsonNoOverwrite( copy, defaults );

		return copy;
	}
	
	public StyleProperty copyOf() {
		return new StyleProperty( Util.deepCopy( properties, JsonObject.class ), Util.deepCopy( properties, JsonObject.class ) );
	}
}
