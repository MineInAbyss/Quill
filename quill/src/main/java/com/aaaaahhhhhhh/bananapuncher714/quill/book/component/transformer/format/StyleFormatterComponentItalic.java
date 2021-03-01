package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentItalic implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "italic" ) || component.getTagName().equalsIgnoreCase( "i" ) ) {
			style.getValues().put( "italic", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unitalic" ) || component.getTagName().equalsIgnoreCase( "ui" ) ) {
			style.getValues().remove( "italic" );
		} else {
			return false;
		}
		return true;
	}
}
