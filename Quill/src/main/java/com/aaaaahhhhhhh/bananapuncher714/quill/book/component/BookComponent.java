package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

public abstract class BookComponent {
	public boolean isTextComponent() {
		return this instanceof BookComponentText;
	}
	
	public BookComponentText getAsTextComponent() {
		return ( BookComponentText ) this;
	}
	
	public boolean isObjectComponent() {
		return this instanceof BookComponentObject;
	}
	
	public BookComponentObject getAsObjectComponent() {
		return ( BookComponentObject ) this;
	}
	
	public boolean isEndComponent() {
		return this instanceof BookComponentTail;
	}
	
	public BookComponentTail getAsEndComponent() {
		return ( BookComponentTail ) this;
	}
}
