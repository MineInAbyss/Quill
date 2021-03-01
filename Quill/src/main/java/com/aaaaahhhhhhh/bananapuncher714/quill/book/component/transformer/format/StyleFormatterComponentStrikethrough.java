package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentStrikethrough implements StyleFormatter< BookComponentObject > {
	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "strikethrough" ) || component.getTagName().equalsIgnoreCase( "s" ) ) {
			style.getValues().put( "strikethrough", "true" );
		} else if ( component.getTagName().equalsIgnoreCase( "unstrikethrough" ) || component.getTagName().equalsIgnoreCase( "us" ) ) {
			style.getValues().remove( "strikethrough" );
		} else {
			return false;
		}
		return true;
	}
}
