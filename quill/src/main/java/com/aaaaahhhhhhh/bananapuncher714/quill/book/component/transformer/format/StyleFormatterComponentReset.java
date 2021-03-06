package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class StyleFormatterComponentReset implements StyleFormatter< BookComponentObject > {

	@Override
	public boolean applyStyle( Style style, BookComponentObject component ) {
		if ( component.getTagName().equalsIgnoreCase( "reset" ) || component.getTagName().equalsIgnoreCase( "r" ) ) {
			style.getValues().remove( "font" );
			style.getValues().remove( "bold" );
			style.getValues().remove( "italic" );
			style.getValues().remove( "magic" );
			style.getValues().remove( "strikethrough" );
			style.getValues().remove( "underline" );
			style.getValues().remove( "color" );
			
			return true;
		}
		return false;
	}
}
