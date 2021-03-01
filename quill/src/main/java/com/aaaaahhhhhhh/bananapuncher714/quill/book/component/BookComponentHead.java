package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

import java.util.ArrayList;
import java.util.List;

public class BookComponentHead extends BookComponentObject {
	public final String id;
	
	private List< String > styles = new ArrayList< String >();
	
	public BookComponentHead( String id ) {
		super( "component" );
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public List< String > getStyles() {
		return styles;
	}
}
