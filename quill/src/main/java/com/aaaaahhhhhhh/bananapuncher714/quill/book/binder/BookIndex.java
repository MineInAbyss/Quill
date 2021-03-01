package com.aaaaahhhhhhh.bananapuncher714.quill.book.binder;

import java.util.ArrayList;
import java.util.List;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;

public class BookIndex {
	protected String id;
	protected String title;
	protected String author;
	
	protected List< BookPage > headers = new ArrayList< BookPage >();
	protected List< BookPage > footers = new ArrayList< BookPage >();
	protected List< BookPage > pages = new ArrayList< BookPage >();
	
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

	public List< BookPage > getPages() {
		return pages;
	}
}
