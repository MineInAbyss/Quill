package com.aaaaahhhhhhh.bananapuncher714.quill.util;

import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

/*
 * Utility functions that I don't feel like organizing or don't know where to put.
 * 
 * Don't use this class if you aren't BananaPuncher714. If you do, don't complain.
 */
public class Util {
	private static final Gson GSON = new Gson();
	private static final Pattern XML_TAG = Pattern.compile( "^<.+?>(.*)</.+?>$", Pattern.DOTALL | Pattern.MULTILINE );
	
	public static final PathMatcher FILE_MATCHER_XML = FileSystems.getDefault().getPathMatcher( "regex:.+\\.xml" );
	public static final PathMatcher FILE_MATCHER_YAML = FileSystems.getDefault().getPathMatcher( "regex:.+\\.ya?ml" );
	public static final PathMatcher FILE_MATCHER_JSON = FileSystems.getDefault().getPathMatcher( "regex:.+\\.json" );
	
	/**
	 * Get a valid enum value no matter what.
	 * 
	 * @param clazz
	 * The enum class.
	 * @param values
	 * The name of the enums.
	 * @return
	 * The enum with the name provided, or the first enum available.
	 */
	@SuppressWarnings("unchecked")
	public static < T extends Enum<?> > T getEnum( Class< T > clazz, String... values ) {
		if ( !clazz.isEnum() ) return null;
		T[] constants = clazz.getEnumConstants();
		if ( values == null || values.length == 0 ) return constants[ 0 ];
		for ( Object object : constants ) {
			if ( object.toString().equals( values[ 0 ] ) ) {
				return ( T ) object;
			}
		}
		return getEnum( clazz, pop( values ) );
	}
	
	public static String nodeToString( Node node ) {
		// TODO Don't create new objects each time
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
			t.transform( new DOMSource( node ), new StreamResult( sw ) );
		} catch ( TransformerException te ) {
			te.printStackTrace();
		}
		return sw.toString();
	}
	
	public static String stripOuterTag( String xml ) {
		Matcher matcher = XML_TAG.matcher( xml );
		if ( matcher.find() ) {
			return matcher.group( 1 );
		}
		
		return null;
	}
	
	/*
	 * Copy over any values and overwrite them, if they exist. Combine lists.
	 */
	public static void mergeJson( JsonObject base, JsonObject merge ) {
		for ( Entry< String, JsonElement > entry : merge.entrySet() ) {
			String key = entry.getKey();
			JsonElement element = entry.getValue();
			JsonElement originalElement = base.get( key );
			
			if ( originalElement != null ) {
				if ( element.isJsonObject() && originalElement.isJsonObject() ) {
					mergeJson( originalElement.getAsJsonObject(), element.getAsJsonObject() );
				} else if ( element.isJsonArray() && originalElement.isJsonArray() ) {
					originalElement.getAsJsonArray().addAll( element.getAsJsonArray() );
				} else {
					base.add( key, element );
				}
			} else {
				base.add( key, element );
			}
		}
	}

	public static void mergeJsonNoOverwrite( JsonObject base, JsonObject merge ) {
		for ( Entry< String, JsonElement > entry : merge.entrySet() ) {
			String key = entry.getKey();
			JsonElement element = entry.getValue();
			JsonElement originalElement = base.get( key );
			
			if ( originalElement != null ) {
				if ( element.isJsonObject() && originalElement.isJsonObject() ) {
					mergeJsonNoOverwrite( originalElement.getAsJsonObject(), element.getAsJsonObject() );
				} else if ( element.isJsonArray() && originalElement.isJsonArray() ) {
					originalElement.getAsJsonArray().addAll( element.getAsJsonArray() );
				}
			} else {
				base.add( key, element );
			}
		}
	}
	
	public static boolean matchesFile( Path path, PathMatcher matcher ) {
		return Files.isRegularFile( path ) && matcher.matches( path );
	}
	
	public static < T > T deepCopy( T object, Class< T > type ) {
	    try {
	        return GSON.fromJson( GSON.toJson( object, type ), type );
	    } catch ( Exception e ) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static String[] pop( String[] array ) {
		String[] array2 = new String[ Math.max( 0, array.length - 1 ) ];
		for ( int i = 1; i < array.length; i++ ) {
			array2[ i - 1 ] = array[ i ];
		}
		return array2;
	}
	
	public static < T extends JsonElement > T getOrCreate( JsonObject object, String name, T element ) {
		JsonElement jsonElement = object.get( name );
		if ( jsonElement != null && jsonElement.getClass() == element.getClass() ) {
			element = ( T ) object.get( name );
		} else {
			object.remove( name );
			object.add( name, element );
		}
		return element;
	}
	
	public static List< TextComponent > simplify( List< ? extends BaseComponent > components ) {
		List< TextComponent > simplified = new ArrayList< TextComponent >();
		TextComponent last = null;
		
		Deque< BaseComponent > queue = new ArrayDeque< BaseComponent >( components );
		while ( !queue.isEmpty() ) {
			BaseComponent base = queue.poll().duplicate();
			
			List< BaseComponent > extra = base.getExtra();
			if ( extra != null ) {
				for ( int i = extra.size() - 1; i >= 0; i-- ) {
					queue.addFirst( extra.get( i ) );
				}
				base.setExtra( new ArrayList< BaseComponent >() );
			}
			
			if ( base instanceof TextComponent ) {
				TextComponent text = ( TextComponent ) base;
				if ( last == null ) {
					last = text;
				} else {
					if ( isSimilar( last, text ) ) {
						last.setText( last.getText() + text.getText() );
					} else {
						simplified.add( last );
						last = text;
					}
				}
			}
		}
		
		if ( last != null ) {
			simplified.add( last );
		}
		
		return simplified;
	}
	
	public static boolean isSimilar( BaseComponent c1, BaseComponent c2 ) {
		if ( c1.isBoldRaw() == c2.isBoldRaw() &&
				c1.isItalicRaw() == c2.isItalicRaw() &&
				c1.isObfuscatedRaw() == c2.isObfuscatedRaw() &&
				c1.isStrikethroughRaw() == c2.isStrikethroughRaw() &&
				c1.isUnderlinedRaw() == c2.isUnderlinedRaw() ) {
			String f1 = c1.getFontRaw();
			String f2 = c2.getFontRaw();

			if ( !( ( f1 == null || f1.equals( "default" ) ) &&
					( f2 == null || f2.equals( "default" ) ) ) &&
					!Objects.equal( f1, f2 ) ) {
				return false;
			}
					
			ChatColor cc1 = c1.getColorRaw();
			ChatColor cc2 = c2.getColorRaw();
			if ( cc1 == null ^ cc2 == null ) {
				return false;
			}
			
			if ( cc1 == cc2 || cc1.equals( cc2 ) ) {
				ClickEvent ce1 = c1.getClickEvent();
				ClickEvent ce2 = c2.getClickEvent();
				if ( ce1 == null ^ ce2 == null ) {
					return false;
				}
				
				if ( ce1 == ce2 || ( ce1.getAction() == ce2.getAction() && ce1.getValue() == ce2.getValue() ) ) {
					HoverEvent he1 = c1.getHoverEvent();
					HoverEvent he2 = c2.getHoverEvent();
					if ( he1 == null ^ he2 == null ) {
						return false;
					}
					
					boolean match = he1 == he2  || he1.getAction() == he2.getAction();
					if ( match && he1 != null ) {
						List< Content > content1 = he1.getContents();
						List< Content > content2 = he2.getContents();
						
						if ( content1.size() != content2.size() ) {
							return false;
						}
						
						for ( int i = 0; i < content1.size(); i++ ) {
							Content con1 = content1.get( i );
							Content con2 = content2.get( i );

							if ( !con1.equals( con2 ) ) {
								return false;
							}
						}
					}
					
					List< BaseComponent > extra1 = c1.getExtra();
					List< BaseComponent > extra2 = c2.getExtra();
					
					if ( extra1 == null ^ extra2 == null ) {
						return false;
					}
					
					if ( extra1 == extra2 ) {
						return true;
					}
					
					if ( extra1.size() == extra2.size() ) {
						for ( int i = 0; i < extra1.size(); i++ ) {
							if ( !isSimilar( extra1.get( i ), extra2.get( i ) ) ) {
								return false;
							}
						}
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isSame( TextComponent c1, TextComponent c2 ) {
		return isSimilar( c1, c2 ) && c1.getText().equals( c2.getText() );
	}
	
	public static void merge( BaseComponent original, BaseComponent other ) {
		original.setBold( original.isBoldRaw() == null ? other.isBoldRaw() : original.isBoldRaw() );
		original.setItalic( original.isItalicRaw() == null ? other.isItalicRaw() : original.isItalicRaw() );
		original.setUnderlined( original.isUnderlinedRaw() == null ? other.isUnderlinedRaw() : original.isUnderlinedRaw() );
		original.setStrikethrough( original.isStrikethroughRaw() == null ? other.isStrikethroughRaw() : original.isStrikethroughRaw() );
		original.setObfuscated( original.isObfuscatedRaw() == null ? other.isObfuscatedRaw() : original.isObfuscatedRaw() );
		original.setColor( original.getColorRaw() == null ? other.getColorRaw() : original.getColorRaw() );
		original.setFont( original.getFontRaw() == null ? other.getFontRaw() : original.getFontRaw() );
		original.setClickEvent( original.getClickEvent() == null ? other.getClickEvent() : original.getClickEvent() );
		original.setHoverEvent( original.getHoverEvent() == null ? other.getHoverEvent() : original.getHoverEvent() );
	}
}
