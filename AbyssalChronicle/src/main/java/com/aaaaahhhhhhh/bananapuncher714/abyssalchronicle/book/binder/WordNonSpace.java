package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder;

import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

public class WordNonSpace extends Word {
	private List< TextComponent > components;
	
	public WordNonSpace( List< TextComponent > components ) {
		for ( TextComponent component : components ) {
			if ( component.getText().contains( " " ) || component.getText().contains( "\n" ) ) {
				throw new IllegalArgumentException( "The provided text component must not contain spaces or newlines!!" );
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
