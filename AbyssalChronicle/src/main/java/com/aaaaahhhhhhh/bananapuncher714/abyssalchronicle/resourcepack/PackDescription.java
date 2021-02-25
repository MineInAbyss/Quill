package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.util.Util;
import com.google.gson.JsonObject;

public class PackDescription {
	protected int format = 6;
	protected String description = "A Resource Pack";
	
	public PackDescription() {}
	
	public PackDescription( JsonObject object ) {
		format = object.get( "pack" ).getAsJsonObject().get( "pack_format" ).getAsInt();
		description = object.get( "pack" ).getAsJsonObject().get( "description" ).getAsString();
	}
	
	public int getFormat() {
		return format;
	}
	
	public void setFormat( int format ) {
		this.format = format;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription( String description ) {
		this.description = description;
	}
	
	public JsonObject toJsonObject() {
		return toJsonObject( new JsonObject() );
	}
	
	public JsonObject toJsonObject( JsonObject object ) {
		JsonObject pack = Util.getOrCreate( object, "pack", new JsonObject() );
		pack.addProperty( "pack_format", format );
		pack.addProperty( "description", description );
		
		return object;
	}
}
