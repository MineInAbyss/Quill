package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Nowhere near as comprehensive or well thought out as the default java xml api...
 */
public class BookComponentObject extends BookComponent {
	public final String tagName;
	private Map< String, String > attributes = new HashMap< String, String >();
	private List< BookComponent > subElements = new ArrayList< BookComponent >();
	
	public BookComponentObject( String tagName ) {
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public Map< String, String > getAttributes() {
		return attributes;
	}
	
	public List< BookComponent > getSubElements() {
		return subElements;
	}
}
