package com.aaaaahhhhhhh.bananapuncher714.quill.api;

/*
 * Namespaced keys, forced lowercase
 */
public class NamespacedKey {
	public final String namespace;
	public final String key;
	
	private NamespacedKey( String id, String key ) {
		this.namespace = id;
		this.key = key;
	}
	
	@Override
	public String toString() {
		if ( namespace != null ) {
			return String.format( "%s:%s", namespace, key );
		}
		return key;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamespacedKey other = (NamespacedKey) obj;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public static NamespacedKey of( String namespace, String key ) {
		return new NamespacedKey( namespace, key );
	}
	
	public static NamespacedKey of( String key ) {
		return new NamespacedKey( null, key );
	}
	
	public static NamespacedKey fromString( String value ) {
		int index = value.indexOf( ':' );
		if ( index < 0 ) {
			return of( value );
		} else {
			return of( value.substring( 0, index ), value.substring( index + 1 ) );
		}
	}
}
