package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;

public class Style {
	private final String classId;
	private List< NamespacedKey > inherits = new ArrayList< NamespacedKey >();
	private Map< String, String > values = new HashMap< String, String >();
	private StyleProperty properties = new StyleProperty();
	
	public Style( String classId ) {
		this.classId = classId;
	}
	
	public String getId() {
		return classId;
	}
	
	public List< NamespacedKey > getParents() {
		return inherits;
	}
	
	public void setProperties( StyleProperty property ) {
		this.properties = property;
	}
	
	public StyleProperty getProperties() {
		return properties;
	}
	
	public Map< String, String > getValues() {
		return values;
	}
	
	public Style merge( Style other ) {
		inherits.addAll( other.getParents() );
		for ( Entry< String, String > entry : other.values.entrySet() ) {
			String key = entry.getKey();
			if ( !values.containsKey( key ) ) {
				values.put( key, entry.getValue() );
			}
		}
		
		properties.merge( other.properties );
		
		return this;
	}
	
	public Style copyOf() {
		Style copy = new Style( classId );
		copy.inherits.addAll( inherits );
		copy.values.putAll( values );
		copy.properties = properties.copyOf();
		return copy;
	}
}
