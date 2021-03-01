package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

import java.nio.file.Path;
import java.util.function.Function;

public class BookComponentIdGeneratorPath implements Function< Path, String > {
	protected Path baseDir;
	
	public BookComponentIdGeneratorPath( Path baseDir ) {
		this.baseDir = baseDir;
	}
	
	@Override
	public String apply( Path path ) {
		return baseDir.relativize( path ).normalize().toString();
	}
}
