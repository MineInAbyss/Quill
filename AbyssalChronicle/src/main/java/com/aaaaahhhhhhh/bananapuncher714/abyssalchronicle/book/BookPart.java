package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHead;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheet;

/*
 * Contains parts for binding the book
 * 
 */
public class BookPart {
	public final String id;
	protected String author;
	protected String title;
	protected StyleSheet stylesheet;
	protected BookComponentHead content;
	protected Map< String, BookComponentHead > components = new HashMap< String, BookComponentHead >();
	protected List< IncludeSource > includes = new ArrayList< IncludeSource >();
	
	protected String headerId = "header";
	protected String footerId = "footer";
	
	public BookPart( String id ) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAuthor() {
		return author;
	}

	public BookPart setAuthor( String author ) {
		this.author = author;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public BookPart setTitle( String title ) {
		this.title = title;
		return this;
	}

	public String getHeaderId() {
		return headerId;
	}

	public BookPart setHeaderId( String headerId ) {
		this.headerId = headerId;
		return this;
	}

	public String getFooterId() {
		return footerId;
	}

	public BookPart setFooterId( String footerId ) {
		this.footerId = footerId;
		return this;
	}

	public BookComponentHead getContent() {
		return content;
	}

	public BookPart setContent( BookComponentHead content ) {
		this.content = content;
		return this;
	}

	public StyleSheet getStyleSheet() {
		return stylesheet;
	}

	public BookPart setStylesheet( StyleSheet stylesheet ) {
		this.stylesheet = stylesheet;
		return this;
	}

	public Map< String, BookComponentHead > getComponents() {
		return components;
	}
	
	public List< IncludeSource > getIncludes() {
		return includes;
	}
}
