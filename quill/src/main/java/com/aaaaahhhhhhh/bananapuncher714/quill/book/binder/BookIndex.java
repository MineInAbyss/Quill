package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;

public class BookIndex {
	protected String id;
	protected String title;
	protected String author;
	
	protected List< BookPage > headers = new ArrayList< BookPage >();
	protected List< BookPage > footers = new ArrayList< BookPage >();
	protected Map< String, List< BookPage > > components = new HashMap< String, List< BookPage > >();
	protected List< BookPage > pages = new ArrayList< BookPage >();
	
	protected String header;
	protected String footer;
	
	public BookIndex( String id, String title, String author ) {
		this.id = id;
		this.title = title;
		this.author = author;
	}
	
	public List< BookPage > getHeaders() {
		return headers;
	}
	
	public List< BookPage > getFooters() {
		return footers;
	}
	
	public void setHeader( String header ) {
		this.header = header;
	}

	public void setFooter( String footer ) {
		this.footer = footer;
	}

	public String getHeader() {
		return header;
	}
	
	public String getFooter() {
		return footer;
	}

	public Map< String, List< BookPage > > getComponents() {
		return components;
	}
	
	public List< BookPage > getPages() {
		return pages;
	}
}
