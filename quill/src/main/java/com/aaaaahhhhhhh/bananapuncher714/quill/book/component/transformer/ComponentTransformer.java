package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.Deque;
import java.util.List;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;

public interface ComponentTransformer {
	boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components );
}
