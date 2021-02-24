package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

public class BookPage {
	protected List< TextComponent > components = new ArrayList< TextComponent >();
	
	public TextComponent getLastComponent() {
		return components.isEmpty() ? null : components.get( components.size() - 1 );
	}
	
	public TextComponent addNewComponent() {
		TextComponent component = new TextComponent();
		components.add( component );
		return component;
	}
	
	public List< TextComponent > getComponents() {
		return components;
	}
}
