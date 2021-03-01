package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import net.md_5.bungee.api.chat.TextComponent;

public class WordNewline extends Word {
	private TextComponent component;
	
	public WordNewline( TextComponent component ) {
		if ( !component.getText().equals( "\n" ) ) {
			throw new IllegalArgumentException( "The provided text component must only be a newline!" );
		} else if ( component.getExtra() != null && !component.getExtra().isEmpty() ) {
			throw new IllegalArgumentException( "The provided text component must not have any extra components!" );
		}
		
		this.component = component;
	}
	
	public TextComponent getComponent() {
		return component;
	}
	
	@Override
	public String toString() {
		return component.toPlainText();
	}
}
