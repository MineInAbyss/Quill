package com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack;

import org.bukkit.NamespacedKey;

public class Sound {
	public enum SoundType {
		SOUND, EVENT;
	}
	
	protected final NamespacedKey path;
	protected boolean setVolume = false;
	protected double volume = 1;
	protected boolean setPitch = false;
	protected double pitch = 1;
	protected boolean setWeight = false;
	protected int weight = 1;
	protected boolean setStream = false;
	protected boolean stream = false;
	protected boolean setAttenuationDistance = false;
	protected int attenuationDistance = 0;
	protected boolean setPreload = false;
	protected boolean preload = false;
	protected boolean setType = false;
	protected SoundType type = SoundType.SOUND;
	
	public Sound( NamespacedKey path ) {
		this.path = path;
	}
	
	public NamespacedKey getPath() {
		return path;
	}

	public boolean isSetVolume() {
		return setVolume;
	}
	
	public void setIncludeVolume( boolean setVolume ) {
		this.setVolume = setVolume;
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void setVolume( double volume ) {
		this.volume = volume;
	}
	
	public boolean isSetPitch() {
		return setPitch;
	}
	
	public void setIncludePitch( boolean setPitch ) {
		this.setPitch = setPitch;
	}
	
	public double getPitch() {
		return pitch;
	}
	
	public void setPitch( double pitch ) {
		this.pitch = pitch;
	}
	
	public boolean isSetWeight() {
		return setWeight;
	}
	
	public void setIncludeWeight( boolean setWeight ) {
		this.setWeight = setWeight;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight( int weight ) {
		this.weight = weight;
	}
	
	public boolean isSetStream() {
		return setStream;
	}
	
	public void setIncludeStream( boolean setStream ) {
		this.setStream = setStream;
	}
	
	public boolean isStream() {
		return stream;
	}
	
	public void setStream( boolean stream ) {
		this.stream = stream;
	}
	
	public boolean isSetAttenuationDistance() {
		return setAttenuationDistance;
	}
	
	public void setIncludeAttenuationDistance( boolean setAttenuationDistance ) {
		this.setAttenuationDistance = setAttenuationDistance;
	}
	
	public int getAttenuationDistance() {
		return attenuationDistance;
	}
	
	public void setAttenuationDistance( int attenuationDistance ) {
		this.attenuationDistance = attenuationDistance;
	}
	
	public boolean isSetPreload() {
		return setPreload;
	}
	
	public void setIncludePreload( boolean setPreload ) {
		this.setPreload = setPreload;
	}
	
	public boolean isPreload() {
		return preload;
	}
	
	public void setPreload( boolean preload ) {
		this.preload = preload;
	}
	
	public boolean isSetType() {
		return setType;
	}
	
	public void setIncludeType( boolean setType ) {
		this.setType = setType;
	}
	
	public SoundType getType() {
		return type;
	}
	
	public void setType( SoundType type ) {
		this.type = type;
	}
}
