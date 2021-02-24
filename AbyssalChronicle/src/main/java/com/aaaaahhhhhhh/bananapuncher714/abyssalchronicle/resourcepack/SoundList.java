package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;

public class SoundList {
	protected Map< NamespacedKey, Sound > sounds = new HashMap< NamespacedKey, Sound >();
	
	public void addSound( NamespacedKey name, Sound sound ) {
		sounds.put( name, sound );
	}
	
	public Sound getSound( NamespacedKey name ) {
		return sounds.get( name );
	}
	
	public boolean contains( NamespacedKey name ) {
		return sounds.containsKey( name );
	}
	
	public void remove( NamespacedKey name ) {
		sounds.remove( name );
	}
	
	public boolean isEmpty() {
		return sounds.isEmpty();
	}
}
