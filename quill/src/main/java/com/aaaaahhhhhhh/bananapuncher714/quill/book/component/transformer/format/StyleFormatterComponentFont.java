package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentFont implements StyleFormatter< BookComponentObject > {

	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "font" ) ) {
			String value = component.getAttributes().get( "value" );
			if ( value == null ) {
				value = component.getAttributes().get( "v" );
			}
			
			if ( value == null ) {
				style.getValues().remove( "font" );
			} else {
				style.getValues().put( "font", value );
			}
			
			return true;
		}
		return false;
	}

}
