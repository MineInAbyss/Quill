package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class StyleSupplierConsumingComponentTransformerSupplier< T extends ComponentTransformer & Consumer< Supplier< ? extends ComponentTransformerStyle > > > implements TransformerSupplier< ComponentTransformer >, Supplier< ComponentTransformer > {
	private TransformerSupplier< T > supplier;
	private Supplier< Supplier< ? extends ComponentTransformerStyle > > styleSupplier;
	private T consumingTransformer;
	
	public StyleSupplierConsumingComponentTransformerSupplier( TransformerSupplier< T > supplier, Supplier< Supplier< ? extends ComponentTransformerStyle > > styleSupplier ) {
		this.supplier = supplier;
		this.styleSupplier = styleSupplier;
	}
	
	@Override
	public T createTransformer( CommandSender sender, CatalogBuildable buildable, BookPart part ) {
		consumingTransformer = supplier.createTransformer( sender, buildable, part );

		consumingTransformer.accept( styleSupplier.get() );
		
		return consumingTransformer;
	}
	
	public T get() {
		return consumingTransformer;
	}
}
