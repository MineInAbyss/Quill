package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import net.md_5.bungee.api.chat.TextComponent;

public class BookElementText extends BookElementBaseComponent {
	protected TextComponent component;
	
	public BookElementText() {
		this( new TextComponent() );
	}
	
	public BookElementText( TextComponent component ) {
		super( component );
		this.component = component;
	}

	@Override
	public TextComponent getComponent() {
		return component;
	}
}
