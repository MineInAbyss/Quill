package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import java.util.HashMap;
import java.util.Map;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.binder.WordDirective;

public class BookElementDirective extends BookElement {
	protected String command;
	protected Map< String, String > attributes = new HashMap< String, String >();
	
	public BookElementDirective( String command ) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public Map< String, String > getAttributes() {
		return attributes;
	}
	
	public WordDirective toWord() {
		WordDirective word = new WordDirective( command );
		word.getAttributes().putAll( attributes );
		return word;
	}
}
