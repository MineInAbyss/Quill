package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentColor implements StyleFormatter< BookComponentObject > {

	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "color" ) ) {
			String value = component.getAttributes().get( "value" );
			if ( value == null ) {
				value = component.getAttributes().get( "v" );
			}
			
			if ( value == null ) {
				style.getValues().remove( "color" );
			} else {
				style.getValues().put( "color", value );
			}
			return true;
		}
		return false;
	}
}
