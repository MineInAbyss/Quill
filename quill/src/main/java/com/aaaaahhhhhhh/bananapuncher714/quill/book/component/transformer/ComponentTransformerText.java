package com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.ColorType;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.TextTransformer;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentText;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.Style;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentTransformerText implements ComponentTransformer, Consumer< Supplier< ? extends ComponentTransformerStyle > > {
	private Supplier< ? extends ComponentTransformerStyle > styleSupplier;
	private List< TextTransformer< String, String > > transformers = new ArrayList< TextTransformer< String, String > >();
	private List< TextTransformer< TextComponent, TextComponent[] > > componentTransformers = new ArrayList< TextTransformer< TextComponent, TextComponent[] > >();
	private CommandSender sender;
	
	public ComponentTransformerText( CommandSender sender, CatalogBuildable cache, BookPart part ) {
		this.sender = sender;
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( component instanceof BookComponentText ) {
			BookComponentText text = component.getAsTextComponent();
			String content = text.getText();
			for ( TextTransformer< String, String > transformer : transformers ) {
				content = transformer.transform( sender, content );
			}
			TextComponent textComponent = new TextComponent( content );
			Style currentStyle = styleSupplier.get().getCurrentStyle();
			applyStyle( currentStyle, textComponent );
			
			List< TextComponent > textComponents = new ArrayList< TextComponent >();
			textComponents.add( textComponent );
			for ( TextTransformer< TextComponent, TextComponent[] > componentTransformer : componentTransformers ) {
				List< TextComponent > additional = new ArrayList< TextComponent >();
				for ( TextComponent sub : textComponents ) {
					for ( TextComponent res : componentTransformer.transform( sender, sub ) ) {
						additional.add( res );
					}
				}
				textComponents = additional;
			}
			
			for ( TextComponent parsed : textComponents ) {
				applyStyle( currentStyle, parsed );
				pages.get( pages.size() - 1 ).getComponents().add( parsed );
			}
		}
		
		return false;
	}

	public void addTextTransformer( TextTransformer< String, String > transformer ) {
		transformers.add( transformer );
	}
	
	public void addComponentTransformer( TextTransformer< TextComponent, TextComponent[] > transformer ) {
		componentTransformers.add( transformer );
	}
	
	@Override
	public void accept( Supplier< ? extends ComponentTransformerStyle > supplier ) {
		styleSupplier = supplier;
	}
	
	private void applyStyle( Style style, BaseComponent component ) {
		if ( style != null ) {
			String boldStr = style.getValues().get( "bold" );
			if ( boldStr != null && component.isBoldRaw() == null ) {
				component.setBold( Boolean.valueOf( boldStr ) );
			}
			String italicStr = style.getValues().get( "italic" );
			if ( italicStr != null && component.isItalicRaw() == null ) {
				component.setItalic( Boolean.valueOf( italicStr ) );
			}
			String underStr = style.getValues().get( "underline" );
			if ( underStr != null && component.isUnderlinedRaw() == null ) {
				component.setUnderlined( Boolean.valueOf( underStr ) );
			}
			String magicStr = style.getValues().get( "magic" );
			if ( magicStr != null && component.isObfuscatedRaw() == null ) {
				component.setObfuscated( Boolean.valueOf( magicStr ) );
			}
			String strikeStr = style.getValues().get( "strikethrough" );
			if ( strikeStr != null && component.isStrikethroughRaw() == null ) {
				component.setStrikethrough( Boolean.valueOf( strikeStr ) );
			}
			String fontStr = style.getValues().get( "font" );
			if ( fontStr != null && component.getFontRaw() == null ) {
				component.setFont( fontStr );
			}

			String colorStr = style.getValues().get( "color" );
			if ( colorStr != null && component.getColorRaw() == null ) {
				Optional< Color > optionalColor = ColorType.fromString( colorStr );
				if ( !optionalColor.isPresent() ) {
					throw new IllegalArgumentException( String.format( "Cannot convert '%s' to a color!", colorStr ) );
				}
				component.setColor( ChatColor.of( optionalColor.get() ) );
			}
			
			if ( component.getExtra() != null ) {
				for ( BaseComponent extra : component.getExtra() ) {
					applyStyle( style, extra );
				}
			}
		}
	}
}
