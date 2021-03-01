package com.aaaaahhhhhhh.bananapuncher714.quill.book;

import java.nio.file.Path;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.FileParser;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.FileSelector;

public interface BookPartParserFile extends BookPartParser< Path >, FileParser< BookPart >, FileSelector{
}
