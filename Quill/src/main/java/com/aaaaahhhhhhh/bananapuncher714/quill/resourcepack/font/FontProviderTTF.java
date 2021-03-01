package com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FontProviderTTF extends FontProvider {
	protected NamespacedKey file;
	protected int size;
	protected double oversample;
	protected double[] shift = new double[] { 0, 0 };
	
	public FontProviderTTF( NamespacedKey path ) {
		super( "ttf" );
		file = path;
	}
	
	public NamespacedKey getFile() {
		return file;
	}

	public int getSize() {
		return size;
	}

	public void setSize( int size ) {
		this.size = size;
	}

	public double getOversample() {
		return oversample;
	}

	public void setOversample( double oversample ) {
		this.oversample = oversample;
	}

	public double[] getShift() {
		return shift;
	}

	public void setShift( double x, double y ) {
		this.shift = new double[] { x, y };
	}
	
	public JsonObject toJsonObject() {
		JsonObject object = super.toJsonObject();
		object.addProperty( "file", file.toString() );
		object.addProperty( "size", size );
		object.addProperty( "oversample", oversample );
		JsonArray arr = new JsonArray();
		arr.add( shift[ 0 ] );
		arr.add( shift[ 1 ] );
		object.add( "shift", arr );
		
		return object;
	}
}
