package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder;

import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

public class WordNonWhitespace extends Word {
	private List< TextComponent > components;
	
	public WordNonWhitespace( List< TextComponent > components ) {
		for ( TextComponent component : components ) {
			if ( !component.getText().contains( " " ) ) {
				throw new IllegalArgumentException( "The provided text component must not contain whitespace!" );
			} else if ( component.getExtra() != null && !component.getExtra().isEmpty() ) {
				throw new IllegalArgumentException( "The provided text component must not have any extra components!" );
			}
		}
		
		this.components = components;
	}
	
	public List< TextComponent > getComponent() {
		return components;
	}
}
