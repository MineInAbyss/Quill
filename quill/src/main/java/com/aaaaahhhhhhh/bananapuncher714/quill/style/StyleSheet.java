package com.aaaaahhhhhhh.bananapuncher714.quill.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.IncludeSource;

public class StyleSheet {
	private final String id;
	private List< IncludeSource > includes = new ArrayList< IncludeSource >();
	private Map< String, Style > styles = new HashMap< String, Style >();
	
	public StyleSheet( String id ) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public List< IncludeSource > getIncludes() {
		return includes;
	}

	public Map< String, Style > getStyles() {
		return styles;
	}
	
	public StyleSheet merge( StyleSheet other ) {
		this.includes.addAll( other.includes );
		
		for ( Entry< String, Style > entry : other.styles.entrySet() ) {
			String key = entry.getKey();
			if ( !styles.containsKey( key ) ) {
				styles.put( key, entry.getValue().copyOf() );
			}
		}
		
		return this;
	}
	
	public StyleSheet copyOf() {
		StyleSheet copy = new StyleSheet( id );
		copy.includes.addAll( includes );
		copy.styles.putAll( styles );
		return copy;
	}
}
