package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.Deque;
import java.util.List;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;

public interface ComponentTransformer {
	boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components );
}
