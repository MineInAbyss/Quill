package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import java.util.HashMap;
import java.util.Map;

public class WordDirective extends Word {
	protected String command;
	protected Map< String, String > attributes = new HashMap< String, String >();
	
	public WordDirective( String command ) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	public Map< String, String > getAttributes() {
		return attributes;
	}
}
