package com.aaaaahhhhhhh.bananapuncher714.quill.book.component;

import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerStyle;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.TransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class StyleTransformerSupplier implements TransformerSupplier< ComponentTransformerStyle >, Supplier< Supplier< ? extends ComponentTransformerStyle > > {
	protected ComponentTransformerStyle transformer;
	
	@Override
	public ComponentTransformerStyle createTransformer( CommandSender sender, CatalogBuildable buildable, BookPart part ) {
		return transformer = new ComponentTransformerStyle( sender, buildable, part );
	}

	@Override
	public Supplier< ? extends ComponentTransformerStyle > get() {
		return () -> transformer;
	}
}
