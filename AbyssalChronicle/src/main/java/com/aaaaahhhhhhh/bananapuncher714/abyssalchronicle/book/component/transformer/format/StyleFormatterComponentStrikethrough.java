package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class StyleFormatterComponentStrikethrough implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "strikethrough" ) || component.getTagName().equalsIgnoreCase( "s" ) ) {
			style.getValues().put( "strikethrough", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unstrikethrough" ) || component.getTagName().equalsIgnoreCase( "us" ) ) {
			style.getValues().put( "strikethrough", "false" );
		} else {
			return false;
		}
		return false;
	}
}
