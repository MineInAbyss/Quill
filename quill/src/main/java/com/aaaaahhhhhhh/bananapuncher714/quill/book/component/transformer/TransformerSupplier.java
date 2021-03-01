package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public interface TransformerSupplier< T extends ComponentTransformer > {
	T createTransformer( CommandSender sender, CatalogBuildable buildable, BookPart part );
}
