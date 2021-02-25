package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class StyleFormatterComponentMagic implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "magic" ) || component.getTagName().equalsIgnoreCase( "m" ) ) {
			style.getValues().put( "magic", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unmagic" ) || component.getTagName().equalsIgnoreCase( "um" ) ) {
			style.getValues().remove( "magic" );
		} else {
			return false;
		}
		return true;
	}
}
