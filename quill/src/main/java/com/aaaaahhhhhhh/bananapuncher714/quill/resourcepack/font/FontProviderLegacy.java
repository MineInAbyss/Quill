package com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.google.gson.JsonObject;

public class FontProviderLegacy extends FontProvider {
	protected NamespacedKey sizes;
	protected NamespacedKey template;
	
	public FontProviderLegacy( NamespacedKey sizes, NamespacedKey template ) {
		super( "legacy_unicode" );
		this.sizes = sizes;
		this.template = template;
	}

	public NamespacedKey getSizes() {
		return sizes;
	}

	public NamespacedKey getTemplate() {
		return template;
	}
	
	public JsonObject toJsonObject() {
		JsonObject object = super.toJsonObject();
		object.addProperty( "sizes", sizes.toString() );
		object.addProperty( "template", template.toString() );
		
		return object;
	}
}
