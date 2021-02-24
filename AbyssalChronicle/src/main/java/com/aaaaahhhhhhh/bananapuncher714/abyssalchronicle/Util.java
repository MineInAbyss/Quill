package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle;

import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
}
