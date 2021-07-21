package com.aaaaahhhhhhh.bananapuncher714.quill.book;

public abstract class BookElement {
	public boolean isTextElement() {
		return this instanceof BookElementText;
	}
	
	public BookElementText asTextElement() {
		return ( BookElementText ) this;
	}
	
	public boolean isBaseComponentElement() {
		return this instanceof BookElementBaseComponent;
	}
	
	public BookElementBaseComponent asBaseComponentElement() {
		return ( BookElementBaseComponent ) this;
	}
	
	public boolean isMarkElement() {
		return this instanceof BookElementMarker;
	}
	
	public BookElementMarker asMarkerElement() {
		return ( BookElementMarker ) this;
	}
	
	public boolean isDirectiveElement() {
		return this instanceof BookElementDirective;
	}
	
	public BookElementDirective asDirectiveElement() {
		return ( BookElementDirective ) this;
	}
}
