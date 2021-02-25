package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer;

import java.awt.Color;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.ColorType;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentText;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentTransformerText implements ComponentTransformer, Consumer< Supplier< ? extends ComponentTransformerStyle > > {
	private Supplier< ? extends ComponentTransformerStyle > styleSupplier;
	
	public ComponentTransformerText( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component instanceof BookComponentText ) {
			BookComponentText text = component.getAsTextComponent();
			TextComponent textComponent = new TextComponent( text.getText() );
			pages.get( pages.size() - 1 ).getComponents().add( textComponent );
			
			Style currentStyle = styleSupplier.get().getCurrentStyle();
			if ( currentStyle != null ) {
				String boldStr = currentStyle.getValues().get( "bold" );
				if ( boldStr != null ) {
					textComponent.setBold( Boolean.valueOf( boldStr ) );
				}
				String italicStr = currentStyle.getValues().get( "italic" );
				if ( italicStr != null ) {
					textComponent.setItalic( Boolean.valueOf( italicStr ) );
				}
				String underStr = currentStyle.getValues().get( "underline" );
				if ( underStr != null ) {
					textComponent.setUnderlined( Boolean.valueOf( underStr ) );
				}
				String magicStr = currentStyle.getValues().get( "magic" );
				if ( magicStr != null ) {
					textComponent.setObfuscated( Boolean.valueOf( magicStr ) );
				}
				String strikeStr = currentStyle.getValues().get( "strikethrough" );
				if ( strikeStr != null ) {
					textComponent.setStrikethrough( Boolean.valueOf( strikeStr ) );
				}
				String fontStr = currentStyle.getValues().get( "font" );
				if ( fontStr != null ) {
					textComponent.setFont( fontStr );
				}
				String colorStr = currentStyle.getValues().get( "color" );
				if ( colorStr != null ) {
					Optional< Color > optionalColor = ColorType.fromString( colorStr );
					if ( !optionalColor.isPresent() ) {
						throw new IllegalArgumentException( String.format( "Cannot convert '%s' to a color!", colorStr ) );
					}
					textComponent.setColor( ChatColor.of( optionalColor.get() ) );
				}
			}
		}
		
		return false;
	}

	@Override
	public void accept( Supplier< ? extends ComponentTransformerStyle > supplier ) {
		styleSupplier = supplier;
	}
}
