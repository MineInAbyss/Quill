package com.aaaaahhhhhhh.bananapuncher714.quill;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.configuration.YamlFileConfiguration;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPartParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPartParserXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.IncludeSourceSupplierBookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.binder.BookBinderIndexMinecraft;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHeadParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentHeadParserXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentParserXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.StyleTransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerBreak;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerGoto;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerClick;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerDirective;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerExpand;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerFormat;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerHover;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerInsert;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerMarker;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.TextTransformerMineDown;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerNegativeSpaces;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerRedirect;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformerText;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.StyleSupplierConsumingComponentTransformerSupplier;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentBold;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentColor;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentItalic;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentMagic;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentStrikethrough;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.format.StyleFormatterComponentUnderline;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogCallbackPlugin;
import com.aaaaahhhhhhh.bananapuncher714.quill.command.CommandBook;
import com.aaaaahhhhhhh.bananapuncher714.quill.configuration.YamlMerger;
import com.aaaaahhhhhhh.bananapuncher714.quill.dependencies.TextTransformerEmotes;
import com.aaaaahhhhhhh.bananapuncher714.quill.dependencies.TextTransformerPlaceholderAPI;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFont;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFontFactory;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFontProvider;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFontProviderNegativeSpace;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.BananaFontProviderTTF;
import com.aaaaahhhhhhh.bananapuncher714.quill.font.MinecraftFontContainer;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.ResourcePackZip;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontIndex;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontProvider;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontProviderLegacy;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontProviderTTF;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.IncludeSourceSupplierStyleSheet;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleParserXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleSheetParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.style.StyleSheetParserXML;
import com.aaaaahhhhhhh.bananapuncher714.quill.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Quill extends JavaPlugin {
	public static final String DEFAULT_CATALOG_NAMESPACE = "quill";
	
	private Gson GSON = new Gson();
	
	private Library library;
	private CatalogBuildable catalog;
	private boolean catalogAutoUpdate;
	private long catalogUpdateInterval;
	
	private String negativeSpacesFont;
	
	private Path resourcePackPath;
	private Map< String, BananaFont > fonts = new ConcurrentHashMap< String, BananaFont >();
	
	private CommandBook command;
	
	private Map< String, String > messages = new HashMap< String, String >();
	
	@Override
	public void onEnable() {
		if ( !new File( getDataFolder(), "README.md" ).exists() ) {
			initialize();
		}
		
		saveResource( "config.yml", false );
		saveResource( "README.md", false );
		
		loadConfig();
		
		loadAssets();
		
		loadLibrary();
		
		command = new CommandBook( this, getCommand( "book" ) );
		
		Bukkit.getPluginManager().registerEvents( new BookListener( this ), this );
	}
	
	@Override
	public void onDisable() {
		if ( catalog.isRunning() ) {
			catalog.stop();
		}
	}
	
	private void initialize() {
		FileUtil.saveToFile( getResource( "data/guidebook/quill_guidebook_stylesheet.xml" ), new File( getDataFolder(), "catalogs/" + DEFAULT_CATALOG_NAMESPACE + "/styles/quill_guidebook_stylesheet.xml" ), false );
		FileUtil.saveToFile( getResource( "data/guidebook/quill_guidebook_content.xml" ), new File( getDataFolder(), "catalogs/" + DEFAULT_CATALOG_NAMESPACE + "/components/quill_guidebook_content.xml" ), false );
		FileUtil.saveToFile( getResource( "data/guidebook/quill_guidebook.xml" ), new File( getDataFolder(), "catalogs/" + DEFAULT_CATALOG_NAMESPACE + "/books/quill_guidebook.xml" ), false );
	}
	
	private void loadConfig() {
		YamlFileConfiguration configuration = new YamlFileConfiguration( new File( getDataFolder() + "/" + "config.yml" ).toPath() );
		try {
			configuration.load();
			YamlMerger merger = new YamlMerger( configuration, getResource( "config.yml" ) );
			merger.updateHeader( false );
			merger.updateKeys();
			merger.trimKeys();
			merger.updateComments( false );
		} catch ( IOException | InvalidConfigurationException e ) {
			e.printStackTrace();
		}
		
		FileConfiguration config = configuration.getConfiguration();
		resourcePackPath = getDataFolder().toPath().resolve( config.getString( "resource-pack", "resourcepack.zip" ) );
		negativeSpacesFont = config.getString( "negative-spaces-font", "default" );
		catalogAutoUpdate = config.getBoolean( "cache.auto-update", true );
		catalogUpdateInterval = config.getLong( "cache.update-interval", 10_000 );
		
		ConfigurationSection messages = config.getConfigurationSection( "messages" );
		this.messages.clear();
		for ( String key : messages.getKeys( true ) ) {
			this.messages.put( key, messages.getString( key ) );
		}
		
		try {
			configuration.save();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig() {
		loadConfig();
	}
	
	public void reloadAssets() {
		loadConfig();
		loadAssets();
	}
	
	private void loadAssets() {
		if ( !this.fonts.containsKey( "default" ) ) {
			BananaFont font = new BananaFont();
			enhanceFont( font );
			this.fonts.put( "default", font );
		}
		
		Bukkit.getScheduler().runTaskAsynchronously( this , () -> {
			if ( Files.exists( resourcePackPath ) ) {
				getLogger().info( "Loading the resource pack (This may take a while)..." );
				Map< String, BananaFont > fonts = new HashMap< String, BananaFont >();
				try  {
					ResourcePackZip resourcePack = new ResourcePackZip( resourcePackPath );
					
					for ( Entry< String, FontIndex > entry : resourcePack.getFonts().entrySet() ) {
						BananaFont font = BananaFontFactory.constructFrom( resourcePack, entry.getValue() );
						
						enhanceFont( font );
						
						fonts.put( entry.getKey(), font );
					}
					
					resourcePack.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				
				Bukkit.getScheduler().runTask( this, () -> {
					this.fonts.clear();
					this.fonts.putAll( fonts );
					if ( !this.fonts.containsKey( "default" ) ) {
						BananaFont font = new BananaFont();
						enhanceFont( font );
						this.fonts.put( "default", font );
					}
					getLogger().info( "Done loading the resource pack!" );
				} );
			} else {
				getLogger().warning( "Resource pack not found!" );
			}
		} );
	}
	
	private void loadLibrary() {
		library = new Library();
		Path libraryPath = getDataFolder().toPath().resolve( "catalogs/" + DEFAULT_CATALOG_NAMESPACE + "/" );
		catalog = new CatalogBuildable( DEFAULT_CATALOG_NAMESPACE, libraryPath, catalogUpdateInterval );
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
		
		catalog.getTransformers().add( ComponentTransformerDirective::new );
		catalog.getTransformers().add( ComponentTransformerMarker::new );
		catalog.getTransformers().add( ComponentTransformerBreak::new );
		catalog.getTransformers().add( ComponentTransformerGoto::new );
		catalog.getTransformers().add( ComponentTransformerRedirect::new );
		catalog.getTransformers().add( ComponentTransformerExpand::new );
		catalog.getTransformers().add( hoverSupplier );
		catalog.getTransformers().add( ComponentTransformerClick::new );
		catalog.getTransformers().add( ComponentTransformerInsert::new );
		catalog.getTransformers().add( formatSupplier );
		catalog.getTransformers().add( styleSupplier );
		catalog.getTransformers().add( ( sender, catalog, part ) ->  {
			ComponentTransformerText textTransformer = textSupplier.createTransformer( sender, catalog, part );
			
			if ( Bukkit.getPluginManager().isPluginEnabled( "PlaceholderAPI" ) ) {
				// Add the placeholder api text transformer
				textTransformer.addTextTransformer( new TextTransformerPlaceholderAPI() );
			}
			
			textTransformer.addComponentTransformer( new TextTransformerMineDown() );
			if ( Bukkit.getPluginManager().isPluginEnabled( "BondrewdLikesHisEmotes" ) ) {
				textTransformer.addComponentTransformer( new TextTransformerEmotes() );
			}
			
			return textTransformer;
		} );
		catalog.getTransformers().add( ( sender, cache, part ) -> new ComponentTransformerNegativeSpaces( () -> negativeSpacesFont ) );
//		catalog.getTransformers().add( ComponentTransformerTail::new );
		
		// Add the include source suppliers
		catalog.getStyleIncludeSuppliers().add( IncludeSourceSupplierStyleSheet::new );
		catalog.getBookPartIncludeSuppliers().add( IncludeSourceSupplierBookPart::new );
		
		// Set the callback
		catalog.setCallback( new CatalogCallbackPlugin( this ) );
		
		// Set the bookbinder
		catalog.setBookbinder( new BookBinderIndexMinecraft( str -> getFonts().getOrDefault( str, getFonts().get( "default" ) ) ) );
		
		// Start the monitoring
		if ( catalogAutoUpdate ) {
			catalog.start();
		}
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
	
	public Library getLibrary() {
		return library;
	}
	
	public CatalogBuildable getDefaultCatalog() {
		return catalog;
	}
	
	public Map< String, BananaFont > getFonts() {
		return fonts;
	}
	
	public NamespacedKey getBookId() {
		return new NamespacedKey( this, "book-id" );
	}
	
	public String getMessage( String key ) {
		return messages.get( key );
	}
	
	public String getNegativeSpacesFont() {
		return negativeSpacesFont;
	}
}
