package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class StyleFormatterComponentUnderline implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "underline" ) || component.getTagName().equalsIgnoreCase( "u" ) ) {
			style.getValues().put( "underline", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "derline" ) || component.getTagName().equalsIgnoreCase( "n" ) ) {
			style.getValues().put( "underline", "false" );
		} else {
			return false;
		}
		return true;
	}
}
