package com.aaaaahhhhhhh.bananapuncher714.quill.api.configuration;

public class YamlKeyFactory {
	public static YamlKey construct( Object object ) {
		if ( object instanceof String ) {
			return new YamlKeyString( ( String ) object );
		} if ( object instanceof Integer ) {
			return new YamlKeyInteger( ( int ) object );
		}
		return null;
	}
}
