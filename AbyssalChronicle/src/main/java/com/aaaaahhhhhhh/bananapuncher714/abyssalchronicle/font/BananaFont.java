package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class BananaFont {
	private List< BananaFontProvider > providers = new ArrayList< BananaFontProvider >();
	
	public void addProvider( BananaFontProvider provider ) {
		providers.add( provider );
	}
	
	public int getCharWidth( char c, boolean bold ) {
		for ( BananaFontProvider provider : providers ) {
			if ( provider.contains( c ) ) {
				return provider.getWidthFor( c ) + ( bold ? 1 : 0 );
			}
		}
		return 0;
	}
	
	public int getStringWidth( String string, boolean bold ) {
		char[] arr = ChatColor.stripColor( string ).replace( "\n", "" ).toCharArray();
		int length = 0;
		for ( char c : arr ) {
			for ( BananaFontProvider provider : providers ) {
				if ( provider.contains( c ) ) {
					length += provider.getWidthFor( c );
					break;
				}
			}
		}
		return length + ( bold ? arr.length : 0 );
	}
}
