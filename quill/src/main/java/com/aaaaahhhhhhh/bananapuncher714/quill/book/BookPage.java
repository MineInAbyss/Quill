package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import java.util.ArrayList;
import java.util.List;

public class BookPage {
	protected List< BookElement > components = new ArrayList< BookElement >();
	
	public BookElement getLastComponent() {
		return components.isEmpty() ? null : components.get( components.size() - 1 );
	}
	
	public BookElementText addNewTextElement() {
		BookElementText component = new BookElementText();
		components.add( component );
		return component;
	}
	
	public void addElement( BookElement element ) {
		components.add( element );
	}
	
	public BookElementMarker addMarker( String mark ) {
		BookElementMarker marker = new BookElementMarker( mark );
		components.add( marker );
		return marker;
	}
	
	public List< BookElement > getComponents() {
		return components;
	}
}
