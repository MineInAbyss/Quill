package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.chat.BaseComponent;

/*
 * So, the end result that gets shown to the player should be a customized book based off an existing template.
 * It may either have no customization, or show completely different things depending on the circumstances.
 * 
 * To accomplish that, I need to find a format for storing the book data before the customization.
 * Actually, is it even necessary to store the book, or can this step be completely skipped?
 * 
 * I originally intended for the library to contain books, upon which other plugins could add their own books and not get deleted
 * This means I'd need to have a format suitable for other plugins which may not want any customization.
 * So should the book contain exactly what gets sent to the player?
 * Ideally, I would have a bunch of catalogs which provide a way to get the books given a certain amount of parameters
 * I shouldn't have a cache, and I guess the catalog should be part of the library by default.
 * 
 * The book should contain the raw data for the book, as in, the chat components.
 * Essentially, it's the same as BookMeta, but only the book parts.
 */
public class Book {
	protected final String id;
	protected String author;
	protected String title;
	protected List< BaseComponent > pages = new ArrayList< BaseComponent >();
	
	public Book( String id ) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public Book setAuthor( String author ) {
		this.author = author;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Book setTitle( String title ) {
		this.title = title;
		return this;
	}

	public String getId() {
		return id;
	}

	public List< BaseComponent > getPages() {
		return pages;
	}
}
