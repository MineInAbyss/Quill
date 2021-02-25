package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.util.Util;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentTransformerMineDown implements ComponentTransformer {
	private Set< TextComponent > parsed = new HashSet< TextComponent >();
	
	public ComponentTransformerMineDown( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		for ( BookPage page : pages ) {
			List< TextComponent > replacement = new ArrayList< TextComponent >();
			boolean converted = false;
			for ( TextComponent text : page.getComponents() ) {
				if ( !parsed.contains( text ) ) {
					converted = true;
					BaseComponent[] subcomponents = MineDown.parse( text.getText() );
					Deque< BaseComponent > queue = new ArrayDeque< BaseComponent >();
					for ( BaseComponent comp : subcomponents ) {
						queue.add( comp );
					}
					while ( !queue.isEmpty() ) {
						BaseComponent parsing = queue.poll();
						
						List< BaseComponent > extra = parsing.getExtra();
						if ( extra != null ) {
							for ( int i = extra.size() - 1; i >= 0; i-- ) {
								queue.addFirst( extra.get( i ) );
							}
							parsing.setExtra( new ArrayList< BaseComponent >() );
						}
						
						if ( parsing instanceof TextComponent ) {
							// Now get the current thing or whatever it is
							TextComponent subtext = ( TextComponent ) parsing;
							Util.merge( subtext, text );
							parsed.add( subtext );
							replacement.add( subtext );
						}
					}
				} else {
					replacement.add( text );
				}
			}
			if ( converted ) {
				page.getComponents().clear();
				page.getComponents().addAll( replacement );
			}
		}
		
		return false;
	}

}
