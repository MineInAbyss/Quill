package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

/*
 * Nothing but a block of plaintext
 */
public class BookComponentText extends BookComponent {
	protected String text;

	public BookComponentText( String text ) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}
}
