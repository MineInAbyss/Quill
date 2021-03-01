package com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;

public class SoundIndex {
	protected Map< NamespacedKey, SoundList > sounds = new HashMap< NamespacedKey, SoundList >();
	protected boolean setReplace = false;
	protected boolean replace = false;
	protected boolean setSubtitle = false;
	protected String subtitle = null;
	
	public void addSound( NamespacedKey id, NamespacedKey name, Sound sound ) {
		SoundList list = sounds.get( id );
		if ( list == null ) {
			list = new SoundList();
			sounds.put( id, list );
		}
		list.addSound( name, sound );
	}
	
	public Sound getSound( NamespacedKey id, NamespacedKey name ) {
		return contains( id ) ? sounds.get( id ).getSound( name ) : null;
	}
	
	public boolean contains( NamespacedKey id ) {
		return sounds.containsKey( id );
	}
	
	public boolean contains( NamespacedKey id, NamespacedKey name ) {
		return contains( id ) && sounds.get( id ).contains( name );
	}
	
	public void remove( NamespacedKey id ) {
		sounds.remove( id );
	}
	
	public void remove( NamespacedKey id, NamespacedKey name ) {
		if ( contains( id ) ) {
			SoundList list = sounds.get( id);
			list.remove( name );
			if ( list.isEmpty() ) {
				sounds.remove( id );
			}
		}
	}

	public boolean isSetReplace() {
		return setReplace;
	}

	public void setIncludeReplace( boolean setReplace ) {
		this.setReplace = setReplace;
	}

	public boolean isReplace() {
		return replace;
	}

	public void setReplace( boolean replace ) {
		this.replace = replace;
	}

	public boolean isSetSubtitle() {
		return setSubtitle;
	}

	public void setIncludeSubtitle( boolean setSubtitle ) {
		this.setSubtitle = setSubtitle;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle( String subtitle ) {
		this.subtitle = subtitle;
	}
}
