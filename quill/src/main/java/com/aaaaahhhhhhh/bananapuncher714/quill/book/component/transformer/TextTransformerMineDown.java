package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.TextTransformer;
import com.aaaaahhhhhhh.bananapuncher714.quill.util.Util;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextTransformerMineDown implements TextTransformer< TextComponent, TextComponent[] > {
	@Override
	public TextComponent[] transform( CommandSender sender, TextComponent text ) {
		List< TextComponent > components = new ArrayList< TextComponent >();
		BaseComponent[] subcomponents = MineDown.parse( text.getText() );
		for ( BaseComponent component : subcomponents ) {
			if ( component instanceof TextComponent ) {
				components.add( ( TextComponent ) component );
				// Copy over the original text formatting
				Util.merge( component, text );
				// Copy over the formatting for any extra as well
				if ( component.getExtra() != null ) {
					Deque< BaseComponent > copy = new ArrayDeque< BaseComponent >();
					copy.addAll( component.getExtra() );
					while ( !copy.isEmpty() ) {
						BaseComponent first = copy.poll();
						Util.merge( first, text );
						if ( first.getExtra() != null ) {
							copy.addAll( first.getExtra() );
						}
					}
				}
			}
		}
		return components.toArray( new TextComponent[ components.size() ] );
	}

}
