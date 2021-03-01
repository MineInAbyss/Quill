package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentBold implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "bold" ) || component.getTagName().equalsIgnoreCase( "b" ) ) {
			style.getValues().put( "bold", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unbold" ) || component.getTagName().equalsIgnoreCase( "ub" ) ) {
			style.getValues().remove( "bold" );
		} else {
			return false;
		}
		return true;
	}
}
