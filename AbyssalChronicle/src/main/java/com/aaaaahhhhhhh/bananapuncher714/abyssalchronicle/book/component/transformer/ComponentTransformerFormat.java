package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleFormatter;

public class ComponentTransformerFormat implements ComponentTransformer, Consumer< Supplier< ? extends ComponentTransformerStyle > > {
	private List< StyleFormatter< BookComponentObject > > formatters = new ArrayList< StyleFormatter< BookComponentObject > >();
	private Supplier< ? extends ComponentTransformerStyle > supplier;
	
	public ComponentTransformerFormat( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component.isObjectComponent() ) {
			BookComponentObject object = component.getAsObjectComponent();
			Style style = supplier.get().getCurrentStyle();
			boolean formatted = true;
			for ( int i = 0; i < formatters.size() && ( formatted = !formatters.get( i ).applyStyle( style, object ) ); i++ );
			return !formatted;
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
