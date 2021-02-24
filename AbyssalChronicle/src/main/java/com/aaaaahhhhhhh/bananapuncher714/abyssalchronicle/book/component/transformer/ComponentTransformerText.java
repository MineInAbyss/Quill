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
				textComponent.setBold( Boolean.valueOf( currentStyle.getValues().getOrDefault( "bold", "false" ).toLowerCase() ) );
				textComponent.setItalic( Boolean.valueOf( currentStyle.getValues().getOrDefault( "italic", "false" ).toLowerCase() ) );
				textComponent.setUnderlined( Boolean.valueOf( currentStyle.getValues().getOrDefault( "underline", "false" ).toLowerCase() ) );
				textComponent.setObfuscated( Boolean.valueOf( currentStyle.getValues().getOrDefault( "magic", "false" ).toLowerCase() ) );
				textComponent.setStrikethrough( Boolean.valueOf( currentStyle.getValues().getOrDefault( "strikethrough", "false" ).toLowerCase() ) );
				textComponent.setFont( currentStyle.getValues().get( "font" ) );
				
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
