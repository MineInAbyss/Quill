package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book;

import java.nio.file.Path;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.FileParser;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.FileSelector;

public interface BookPartParserFile extends BookPartParser< Path >, FileParser< BookPart >, FileSelector{
}
