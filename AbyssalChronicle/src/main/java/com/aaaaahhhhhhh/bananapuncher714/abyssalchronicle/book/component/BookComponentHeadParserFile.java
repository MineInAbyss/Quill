package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component;

import java.nio.file.Path;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.FileParser;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.FileSelector;

public interface BookComponentHeadParserFile extends BookComponentHeadParser< Path >, FileParser< BookComponentHead >, FileSelector {
}
