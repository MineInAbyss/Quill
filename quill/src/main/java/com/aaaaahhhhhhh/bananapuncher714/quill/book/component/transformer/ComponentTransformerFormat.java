package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleFormatter;

public class ComponentTransformerFormat implements ComponentTransformer, Consumer< Supplier< ? extends ComponentTransformerStyle > > {
	private List< StyleFormatter< BookComponentObject > > formatters = new ArrayList< StyleFormatter< BookComponentObject > >();
	private Supplier< ? extends ComponentTransformerStyle > supplier;
	
	public ComponentTransformerFormat( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.asObjectComponent();
			Style style = supplier.get().getCurrentStyle();
			if ( style != null ) {
				if ( object.getSubElements().isEmpty() ) {
					// Apply the format to the current style
					boolean formatted = false;
					for ( int i = 0; i < formatters.size() && !( formatted = formatters.get( i ).applyStyle( style, object ) ); i++ );
					return formatted;
				} else {
					// Apply the format to all elements within
					Style copy = style.copyOf();
					boolean formatted = false;
					for ( int i = 0; i < formatters.size() && !( formatted = formatters.get( i ).applyStyle( copy, object ) ); i++ );
					if ( formatted ) {
						supplier.get().push( object, copy );
					}
				}
			}
		}
		
		return false;
	}
	
	public List< StyleFormatter< BookComponentObject > > getFormatters() {
		return formatters;
	}
	
	@Override
	public void accept( Supplier< ? extends ComponentTransformerStyle > supplier ) {
		this.supplier= supplier;
	}
}
