package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class StyleFormatterComponentBold implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "bold" ) || component.getTagName().equalsIgnoreCase( "b" ) ) {
			style.getValues().put( "bold", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unbold" ) || component.getTagName().equalsIgnoreCase( "ub" ) ) {
			style.getValues().put( "bold", "false" );
		} else {
			return false;
		}
		return true;
	}
}
