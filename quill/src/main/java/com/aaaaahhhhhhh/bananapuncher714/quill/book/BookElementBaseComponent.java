package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import net.md_5.bungee.api.chat.BaseComponent;

public class BookElementBaseComponent extends BookElement {
	protected BaseComponent component;

	public BookElementBaseComponent( BaseComponent component ) {
		this.component = component;
	}
	
	public BaseComponent getComponent() {
		return component;
	}
}
