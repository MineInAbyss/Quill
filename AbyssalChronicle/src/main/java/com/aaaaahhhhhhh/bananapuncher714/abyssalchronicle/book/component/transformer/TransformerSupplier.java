package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;

public interface TransformerSupplier< T extends ComponentTransformer > {
	T createTransformer( CommandSender sender, CatalogBuildable buildable, BookPart part );
}
