package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPartParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPartParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.IncludeSourceSupplierBookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.binder.BookBinderIndexMinecraft;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHeadParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentHeadParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.BookComponentParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.StyleTransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerBreak;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerClick;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerExpand;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerFormat;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerHover;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerInsert;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerTail;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.ComponentTransformerText;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.StyleSupplierConsumingComponentTransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentBold;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentColor;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentItalic;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentMagic;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentStrikethrough;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.component.transformer.format.StyleFormatterComponentUnderline;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogCallbackPlugin;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFont;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFontFactory;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFontProvider;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFontProviderNegativeSpace;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.BananaFontProviderTTF;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font.MinecraftFontContainer;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.ResourcePackZip;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontIndex;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProvider;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderLegacy;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderTTF;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.IncludeSourceSupplierStyleSheet;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheetParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheetParserXML;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class AbyssalChronicle extends JavaPlugin {
	public static final String DEFAULT_CATALOG_NAMESPACE = "abyssal";
	
	private Gson GSON = new Gson();
	
	private Library library;
	private CatalogBuildable catalog;
	
	private Path resourcePackPath;
	private ResourcePackZip resourcePack;
	private Map< String, BananaFont > fonts = new HashMap< String, BananaFont >();
	
	@Override
	public void onEnable() {
		saveResource( "config.yml", false );
		
		loadConfig();
		
		loadAssets();
		
		loadLibrary();
	}
	
	@Override
	public void onDisable() {
		catalog.stop();
	}
	
	private int getLength( BaseComponent component ) {
		int length = 0;
		if ( component instanceof TextComponent ) {
			TextComponent text = ( TextComponent ) component;
			String fontId = text.getFont();
			BananaFont font = fonts.getOrDefault( fontId, fonts.get( "default" ) );
			
			if ( font != null ) {
				length += font.getStringWidth( text.getText(), text.isBold() );
			}
		}
		List< BaseComponent > extra = component.getExtra();
		if ( extra != null ) {
			for ( BaseComponent subComponent : extra ) {
				length += getLength( subComponent );
			}
		}
		return length;
	}
	
	private void loadConfig() {
		FileConfiguration config = YamlConfiguration.loadConfiguration( new File( getDataFolder(), "config.yml" ) );
		resourcePackPath = getDataFolder().toPath().resolve( config.getString( "resource-pack", "resourcepack.zip" ) );
	}
	
	private void loadAssets() {
		Path resourcePackPath = getDataFolder().toPath().resolve( "resource-pack.zip" );
		try {
			resourcePack = new ResourcePackZip( resourcePackPath );
			
			for ( Entry< String, FontIndex > entry : resourcePack.getFonts().entrySet() ) {
				BananaFont font = BananaFontFactory.constructFrom( resourcePack, entry.getValue() );
				
				enhanceFont( font );
				
				fonts.put( entry.getKey(), font );
			}
			
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	private void loadLibrary() {
		library = new Library();
		Path libraryPath = getDataFolder().toPath().resolve( "catalogs/" + DEFAULT_CATALOG_NAMESPACE + "/" );
		catalog = new CatalogBuildable( DEFAULT_CATALOG_NAMESPACE, libraryPath, 10_000 );
		library.addCatalog( catalog );
		
		// Construct the component, style and book parsers
		StyleSheetParserXML stylesheetParser = new StyleSheetParserXML( new StyleParserXML() );
		BookComponentHeadParserXML componentParser = new BookComponentHeadParserXML( new BookComponentParserXML() );
				
		try {
			catalog.getStylesheetParsers().add( new StyleSheetParserFileXML( stylesheetParser ) );
			catalog.getComponentParsers().add( new BookComponentHeadParserFileXML( path -> catalog.getComponentBaseDirectory().relativize( path ).toString(), componentParser ) );
			catalog.getBookParsers().add( new BookPartParserFileXML( new BookPartParserXML( componentParser, stylesheetParser ) ) );
		} catch ( ParserConfigurationException e ) {
			e.printStackTrace();
		}
		
		// Construct the transformers
		StyleTransformerSupplier styleSupplier = new StyleTransformerSupplier();
		StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerText > textSupplier = new StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerText >( ComponentTransformerText::new, styleSupplier );
		StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerHover > hoverSupplier = new StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerHover >( ComponentTransformerHover::new, styleSupplier );
		StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerFormat > formatSupplier = new StyleSupplierConsumingComponentTransformerSupplier< ComponentTransformerFormat >( ( pages, component, components ) -> {
			ComponentTransformerFormat formatter = new ComponentTransformerFormat( pages, component, components );
			formatter.getFormatters().add( new StyleFormatterComponentBold() );
			formatter.getFormatters().add( new StyleFormatterComponentColor() );
			formatter.getFormatters().add( new StyleFormatterComponentItalic() );
			formatter.getFormatters().add( new StyleFormatterComponentMagic() );
			formatter.getFormatters().add( new StyleFormatterComponentStrikethrough() );
			formatter.getFormatters().add( new StyleFormatterComponentUnderline() );
			return formatter;
		}, styleSupplier );
		
		catalog.getTransformers().add( ComponentTransformerBreak::new );
		catalog.getTransformers().add( ComponentTransformerExpand::new );
		catalog.getTransformers().add( hoverSupplier );
		catalog.getTransformers().add( ComponentTransformerClick::new );
		catalog.getTransformers().add( ComponentTransformerInsert::new );
		catalog.getTransformers().add( formatSupplier );
		catalog.getTransformers().add( styleSupplier );
		catalog.getTransformers().add( textSupplier );
//		catalog.getTransformers().add( ComponentTransformerTail::new );
		
		// Add the include source suppliers
		catalog.getStyleIncludeSuppliers().add( IncludeSourceSupplierStyleSheet::new );
		catalog.getBookPartIncludeSuppliers().add( IncludeSourceSupplierBookPart::new );
		
		// Set the callback
		catalog.setCallback( new CatalogCallbackPlugin( this ) );
		
		// Set the bookbinder
		catalog.setBookbinder( new BookBinderIndexMinecraft( str -> fonts.getOrDefault( str, fonts.get( "default" ) ) ) );
		
		// Start the monitoring
		catalog.start();
	}
	
	private void enhanceFont( BananaFont font ) {
		JsonObject obj = GSON.fromJson( new InputStreamReader( getResource( "data/font/default.json" ) ), JsonObject.class );
		FontIndex index = new FontIndex( obj );
		for ( FontProvider provider : index.getProviders() ) {
			if ( provider instanceof FontProviderBitmap ) {
				FontProviderBitmap bitmap = ( FontProviderBitmap ) provider;
				
				InputStream resource = getResource( bitmap.getFile().key );
				try {
					BufferedImage image = ImageIO.read( resource );
					BananaFontProvider bitmapProvider = new BananaFontProviderBitmap( image, bitmap.getChars(), bitmap.getHeight() );
					font.addProvider( bitmapProvider );
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			} else if ( provider instanceof FontProviderTTF ) {
				FontProviderTTF ttf = ( FontProviderTTF ) provider;
				
				if ( ttf.getFile().key.contains( "negative_spaces" ) ) {
					font.addProvider( BananaFontProviderNegativeSpace.getProvider() );
				} else {
					InputStream resource = getResource( ttf.getFile().key );
					try {
						Font jFont = Font.createFont( Font.TRUETYPE_FONT, resource );
						BananaFontProvider bananaProvider = new BananaFontProviderTTF( jFont );
						font.addProvider( bananaProvider );
					} catch ( FontFormatException | IOException e ) {
						e.printStackTrace();
					}
				}
			} else if ( provider instanceof FontProviderLegacy ) {
				FontProviderLegacy legacy = ( FontProviderLegacy ) provider;
				InputStream resource = getResource( legacy.getSizes().key );
				BananaFontProvider container = new MinecraftFontContainer( resource );
				font.addProvider( container );
			}
		}
	}
	
	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( args.length == 1 ) {
			Optional< Book > optionalBook = library.getBook( sender, NamespacedKey.of( DEFAULT_CATALOG_NAMESPACE, args[ 0 ] ) );
			if ( optionalBook.isPresent() ) {
				Book book = optionalBook.get();
				
				if ( sender instanceof Player ) {
					Player player = ( Player ) sender;

					ItemStack bookItem = new ItemStack( Material.WRITTEN_BOOK );
					BookMeta meta = ( BookMeta ) bookItem.getItemMeta();
					meta.setAuthor( book.getAuthor() );
					meta.setTitle( book.getTitle() );
					for ( BaseComponent page : book.getPages() ) {
						meta.spigot().addPage( new BaseComponent[] { page } );
					}
					bookItem.setItemMeta( meta );
					
					player.getInventory().addItem( bookItem );
				} else {
					for ( int i = 0; i < book.getPages().size(); i++ ) {
						sender.sendMessage( "Page " + i );
						sender.sendMessage( book.getPages().get( i ).toPlainText() );
					}
				}
			} else {
				sender.sendMessage( "Unknown book!" );
			}
		} else if ( args.length > 1 ) {
			if ( args[ 0 ].equalsIgnoreCase( "doom" ) && sender instanceof Player ) {
				Player player = ( Player ) sender;
				ItemStack bookItem = new ItemStack( Material.WRITTEN_BOOK );
				BookMeta meta = ( BookMeta ) bookItem.getItemMeta();
				meta.setAuthor( "Bug Dev" );
				meta.setTitle( "Buggy underline" );
				TextComponent comp = new TextComponent( "Some text" );
				comp.setUnderlined( true );
				meta.spigot().addPage( new BaseComponent[] { comp } );

				bookItem.setItemMeta( meta );

				player.getInventory().addItem( bookItem );
			} else {
				StringBuilder builder = new StringBuilder();
				for ( String s : args ) {
					builder.append( s );
					builder.append( " " );
				}
				String finStr = builder.toString().trim();
				int length = getLength( new TextComponent( finStr ) );
				sender.sendMessage( "Length is " + length + " for '" + finStr + "'" );
			}
		}
		
		return false;
	}
}
