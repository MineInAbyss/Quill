package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class StyleFormatterComponentColor implements StyleFormatter< BookComponentObject > {

	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "color" ) ) {
			String value = component.getAttributes().get( "value" );
			if ( value == null ) {
				value = component.getAttributes().get( "v" );
			}
			
			if ( value == null ) {
				return false;
			}
			
			style.getValues().put( "color", value );
			
			return true;
		}
		return false;
	}
}
