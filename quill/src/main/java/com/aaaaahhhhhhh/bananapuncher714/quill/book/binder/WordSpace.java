package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

public class WordSpace extends Word {
	private List< TextComponent > components;
	
	public WordSpace( List< TextComponent > components ) {
		for ( TextComponent component : components ) {
			if ( !component.getText().matches( " *" ) ) {
				throw new IllegalArgumentException( "The provided text component must only contain spaces!" );
			} else if ( component.getExtra() != null && !component.getExtra().isEmpty() ) {
				throw new IllegalArgumentException( "The provided text component must not have any extra components!" );
			}
		}
		
		this.components = components;
	}
	
	public List< TextComponent > getComponents() {
		return components;
	}
	
	@Override
	public String toString() {
		TextComponent component = new TextComponent();
		for ( TextComponent c : components ) {
			component.addExtra( c );
		}
		return component.toPlainText();
	}
}
