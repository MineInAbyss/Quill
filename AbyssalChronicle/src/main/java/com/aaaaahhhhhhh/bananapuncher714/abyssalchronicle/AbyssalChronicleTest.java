package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.DirectoryCache;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.DirectoryCache.DirectoryDifference;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.IncludeSource;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPartParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.BookPartParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.IncludeSourceSupplierBookPart;
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
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.Style;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleParserXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheet;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheetParserFileXML;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.style.StyleSheetParserXML;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class AbyssalChronicleTest {
	private static File base = new File( System.getProperty( "user.dir" ) );

	public static void main( String[] args ) throws ParserConfigurationException, SAXException, IOException, InterruptedException {
		testImageParse();
	}

	private static void testDocumentParse() throws ParserConfigurationException, SAXException, IOException {
		File testFile = new File( base, "test/test.xml" );
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse( testFile );

		Element root = doc.getDocumentElement();
		NamedNodeMap map = root.getAttributes();
		for ( int i = 0; i < map.getLength(); i++ ) {
			Node node = map.item( i );
			System.out.println( node.getNodeName() + ": " + node.getNodeValue() );
		}

		Node content = root.getElementsByTagName( "content" ).item( 0 );

		System.out.println( content.getTextContent() );
		NodeList list = content.getChildNodes();
		for ( int i = 0; i < list.getLength(); i++ ) {
			Node element = list.item( i );
			System.out.print( "<" + element.getNodeName() + ">" );
			System.out.print( element.getTextContent() );
		}
		
		System.out.println( "\nParsing styles" );
		StyleParserXML styleParser = new StyleParserXML();
		Node styles = root.getElementsByTagName( "styles" ).item( 0 );
		Node styleNode = ( ( Element ) styles ).getElementsByTagName( "style" ).item( 0 );
		Style style = styleParser.parse( ( Element ) styleNode );
		System.out.println( style.getProperties().getProperties().get( "A key" ).getAsString() );
		
		NamedNodeMap node = root.getAttributes();
		for ( int i = 0; i < node.getLength(); i++ ) {
			Node attr = node.item( i );
			System.out.println( attr.getNodeName() + ":" + attr.getNodeValue() );
		}
	}
	
	private static void createBook() throws ParserConfigurationException {
		File testFile = new File( base, "test/test.xml" );
		
		StyleParserXML styleParser = new StyleParserXML();
		StyleSheetParserXML sheetParser = new StyleSheetParserXML( styleParser );
		BookComponentParserXML bookComponentParser = new BookComponentParserXML();
		BookComponentHeadParserXML componentParser = new BookComponentHeadParserXML( bookComponentParser );
		BookPartParserXML bookPartParserXml = new BookPartParserXML( componentParser, sheetParser );
		BookPartParserFileXML bookPartParser = new BookPartParserFileXML( bookPartParserXml );
		
		BookPart book = bookPartParser.parse( testFile.toPath() );
		StyleSheet sheet = book.getStyleSheet();
		
		System.out.println( "Created book part '" + book.getId() + "' with title " + book.getTitle() + " by " + book.getAuthor() );
		System.out.println( "Got sheet " + sheet.getId() );
		for ( IncludeSource source : sheet.getIncludes() ) {
			System.out.println( "Include: " + source.type + "." + source.id );
		}
		for ( Style style : sheet.getStyles().values() ) {
			System.out.println( "Got style " + style.getId() );
		}
	}
	
	private static void createStylesheet() throws ParserConfigurationException, SAXException, IOException {
		File testFile = new File( base, "test/stylesheet.xml" );
		
		StyleParserXML styleParser = new StyleParserXML();
		StyleSheetParserXML sheetParser = new StyleSheetParserXML( styleParser );
		StyleSheetParserFileXML parser = new StyleSheetParserFileXML( sheetParser );
		
		StyleSheet sheet = parser.parse( testFile.toPath() );
		
		System.out.println( "ID: " + sheet.getId() );
		for ( IncludeSource source : sheet.getIncludes() ) {
			System.out.println( "Include: " + source.type + "." + source.id );
		}
		for ( Style style : sheet.getStyles().values() ) {
			System.out.println( "Got style " + style.getId() );
		}
	}

	private static void testWatcher() throws IOException {
		File watchDirectory = new File( base + "/watch/" );
		watchDirectory.mkdirs();

		Path watchPath = watchDirectory.toPath();
		DirectoryCache cache = new DirectoryCache( watchPath );

		while ( true ) {
			DirectoryDifference difference = cache.getUpdates();
			if ( !difference.isUnchanged() ) {
				for ( Path path : difference.getAdded() ) {
					System.out.println( "Added " + path );
				}
				for ( Path path : difference.getUpdated() ) {
					System.out.println( "Updated " + path );
				}
				for ( Path path : difference.getRemoved() ) {
					System.out.println( "Removed " + path );
				}
			}
			
			try {
				Thread.sleep( 5000 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}
	
	private static void testImageParse() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		File testImage = new File( base, "test/nonlatin_european.png" );
		File testFontFile = new File( base, "test/default.json" );
		
		BananaFont font = new BananaFont();
		
		JsonObject obj = new Gson().fromJson( new InputStreamReader( new FileInputStream( testFontFile ) ), JsonObject.class );
		FontIndex index = new FontIndex( obj );
		
		for ( FontProvider provider : index.getProviders() ) {
			if ( provider instanceof FontProviderBitmap ) {
				FontProviderBitmap bitmap = ( FontProviderBitmap ) provider;
				
				InputStream resource = new FileInputStream( testImage );
				try {
					BufferedImage image = ImageIO.read( resource );
					BananaFontProvider bitmapProvider = new BananaFontProviderBitmap( image, bitmap.getChars(), bitmap.getHeight() );
					font.addProvider( bitmapProvider );
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println( font.getStringWidth( "⭐", false ) );
	}
	
	private static void testResourcePack() throws IOException {
		File testFile = new File( base, "test/resource-pack.zip" );
		ResourcePackZip resourcePack = new ResourcePackZip( testFile.toPath() );
		
		BananaFont bFont = BananaFontFactory.constructFrom( resourcePack, resourcePack.getFonts().get( "default" ) );
		
		File glyphFile = new File( base, "test/glyph_sizes.bin" );
		bFont.addProvider( new MinecraftFontContainer( new FileInputStream( glyphFile ) ) );
		
		TextComponent component = new TextComponent( "||| おはよう" );
		System.out.println( "Length is " + getLength( bFont, component ) );
		
		System.out.println( getLength( bFont, new TextComponent( "|| ||" ) ) );
	}
	
	private static int getLength( BananaFont font, BaseComponent component ) {
		int length = 0;
		if ( component instanceof TextComponent ) {
			TextComponent text = ( TextComponent ) component;
			
			if ( font != null ) {
				length += font.getStringWidth( text.getText(), text.isBold() );
			}
		}
		List< BaseComponent > extra = component.getExtra();
		if ( extra != null ) {
			for ( BaseComponent subComponent : extra ) {
				length += getLength( font, subComponent );
			}
		}
		return length << 1;
	}
	
	private static void testCatalog() throws ParserConfigurationException, InterruptedException {
		File testFile = new File( base, "test" );
		CatalogBuildable catalog = new CatalogBuildable( "DOOM", testFile.toPath(), 5000L );
		
		StyleSheetParserXML stylesheetParser = new StyleSheetParserXML( new StyleParserXML() );
		BookComponentHeadParserXML componentParser = new BookComponentHeadParserXML( new BookComponentParserXML() );
				
		catalog.getStylesheetParsers().add( new StyleSheetParserFileXML( stylesheetParser ) );
		catalog.getComponentParsers().add( new BookComponentHeadParserFileXML( path -> testFile.toPath().relativize( path ).toString(), componentParser ) );
		catalog.getBookParsers().add( new BookPartParserFileXML( new BookPartParserXML( componentParser, stylesheetParser ) ) );
		
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
		catalog.getTransformers().add( ComponentTransformerTail::new );
		
		catalog.getStyleIncludeSuppliers().add( IncludeSourceSupplierStyleSheet::new );
		catalog.getBookPartIncludeSuppliers().add( IncludeSourceSupplierBookPart::new );
		
		catalog.start();
		Thread.sleep( 1000 );
		
		for ( String id : catalog.getAvailableBooks( null ) ) {
			System.out.println( "Found book " + id );
		}
		
		Optional< Book > optionalBook = catalog.getBookFor( null, "beast_bestiary" );
		System.out.println();
		if ( optionalBook.isPresent() ) {
			Book book = optionalBook.get();
			System.out.println( "Printing out book '" + book.getTitle() + "'" );
			for ( int i = 0; i < book.getPages().size(); i++ ) {
				System.out.print( "=START PAGE " + i + "=" );
				System.out.print( book.getPages().get( i ).toPlainText() );
				System.out.println( "=END PAGE " + i + "=" );
			}
		}
		System.out.println();
		
		catalog.stop();
	}
}
