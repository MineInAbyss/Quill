package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api;

import java.awt.Color;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ColorType {
	/**
	 * Hex form, accepts 6 digits. Ex: '#FF00FF'
	 */
	HEX( "^(?:#|0x)?([A-Fa-f0-9]{1,6})$" ),
	/**
	 * RGB form, accepts 3 bytes separated by non-digit characters. Ex: '( 255, 0, 128 )'
	 */
	RGB( "^\\D*?(\\d{1,3})\\D+(\\d{1,3})\\D+(\\d{1,3})\\D*?$" ),
	/**
	 * RGBA form, accepts 4 bytes with the 4th being the alpha of the color. Ex: '( 255, 128, 0, 170 )'
	 */
	RGBA( "^\\D*?(\\d{1,3})\\D+(\\d{1,3})\\D+(\\d{1,3})\\D+(\\d{1,3})\\D*?$" ),
	/**
	 * Integer form, accepts an 8 digit number only. Ex: '219203'
	 */
	INT( "^([0-9]?){8}$" );
	
	private Pattern pattern;
	
	ColorType( String pattern ) {
		this.pattern = Pattern.compile( pattern );
	}
	
	/**
	 * Check if the input matches this pattern.
	 * 
	 * @param string
	 * Cannot be null.
	 * @return
	 * Whether the input matches the pattern of this ColorType.
	 */
	public boolean matches( String string ) {
		return pattern.matcher( string ).matches();
	}
	
	/**
	 * Get the pattern for this color type.
	 * 
	 * @return
	 * The pattern.
	 */
	public Pattern getPattern() {
		return pattern;
	}
	
	/**
	 * Convert an integer color to a string.
	 * 
	 * @param color
	 * The integer color.
	 * @param type
	 * The format to use. Cannot be null.
	 * @return
	 * A string that follows the {@link ColorType} pattern.
	 */
	public static String toString( int color, ColorType type ) {
		if ( type == ColorType.HEX ) {
			return "#" + Integer.toHexString( color );
		} else if ( type == ColorType.INT ) {
			return "" + ( color & 0xFFFFFF );
		} else if ( type == ColorType.RGB ) {
			return "( " + ( color >> 16 & 0xFF ) + ", " + ( color >> 8 & 0xFF ) + ", " + ( color & 0xFF ) + " )";
		} else {
			throw new IllegalArgumentException( "Unknown ColorType provided! " + type.name() );
		}
	}
	
	/**
	 * Convert a color to a string.
	 * 
	 * @param color
	 * Cannot be null.
	 * @param type
	 * The format to use. Cannot be null.
	 * @return
	 * A string that follows the {@link ColorType} pattern.
	 */
	public static String toString( Color color, ColorType type ) {
		return toString( color.getRGB(), type );
	}
	
	/**
	 * Attempt to translate a color from the given string.
	 * 
	 * @param data
	 * Accepts HEX, INT, or RGB.
	 * @return
	 * An optional containing a Color if found, or none.
	 */
	public static Optional< Color > fromString( String data ) {
		if ( ColorType.HEX.matches( data ) ) {
			Matcher matcher = ColorType.HEX.getPattern().matcher( data );
			matcher.find();
			String hexString = matcher.group( 1 );
			int val = Integer.parseInt( hexString, 16 );
			return Optional.of( new Color( val ) );
		} else if ( ColorType.INT.matches( data ) ) {
			Matcher matcher = ColorType.INT.getPattern().matcher( data );
			matcher.find();
			String intString = matcher.group( 1 );
			return Optional.of( new Color( Integer.parseInt( intString ) ) );
		} else if ( ColorType.RGB.matches( data ) ) {
			Matcher matcher = ColorType.RGB.getPattern().matcher( data );
			matcher.find();
			String r = matcher.group( 1 );
			String g = matcher.group( 2 );
			String b = matcher.group( 3 );
			return Optional.of( new Color( Integer.parseInt( r ), Integer.parseInt( g ), Integer.parseInt( b ) ) );
		}
		return Optional.empty();
	}
}