package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.configuration;

public abstract class YamlKey {
	public abstract YamlKey copyOf();
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals( Object obj );
}
