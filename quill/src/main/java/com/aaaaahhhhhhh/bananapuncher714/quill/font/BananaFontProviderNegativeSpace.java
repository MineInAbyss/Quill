package com.aaaaahhhhhhh.bananapuncher714.quill.font;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Specifically for the negative space font for Minecraft resource packs
 * 
 * @author BananaPuncher714
 */
public class BananaFontProviderNegativeSpace implements BananaFontProvider {
	private static final BananaFontProviderNegativeSpace INSTANCE = new BananaFontProviderNegativeSpace();
	
	private Map< Character, Integer > widths = new HashMap< Character, Integer >();
	
	private BananaFontProviderNegativeSpace() {
		widths.put( '\uF800', Integer.MIN_VALUE );
		widths.put( '\uF801', -1 );
		widths.put( '\uF802', -2 );
		widths.put( '\uF803', -3 );
		widths.put( '\uF804', -4 );
		widths.put( '\uF805', -5 );
		widths.put( '\uF806', -6 );
		widths.put( '\uF807', -7 );
		widths.put( '\uF808', -8 );
		widths.put( '\uF809', -16 );
		widths.put( '\uF80A', -32 );
		widths.put( '\uF80B', -64 );
		widths.put( '\uF80C', -128 );
		widths.put( '\uF80D', -256 );
		widths.put( '\uF80E', -512 );
		widths.put( '\uF80F', -1024 );
		widths.put( '\uF820', Integer.MAX_VALUE );
		widths.put( '\uF821', 1 );
		widths.put( '\uF822', 2 );
		widths.put( '\uF823', 3 );
		widths.put( '\uF824', 4 );
		widths.put( '\uF825', 5 );
		widths.put( '\uF826', 6 );
		widths.put( '\uF827', 7 );
		widths.put( '\uF828', 8 );
		widths.put( '\uF829', 16 );
		widths.put( '\uF82A', 32 );
		widths.put( '\uF82B', 64 );
		widths.put( '\uF82C', 128 );
		widths.put( '\uF82D', 256 );
		widths.put( '\uF82E', 512 );
		widths.put( '\uF82F', 1024 );
	}
	
	@Override
	public int getWidthFor( char c ) {
		return widths.getOrDefault( c, 0 );
	}

	@Override
	public boolean contains( char c ) {
		return widths.containsKey( c );
	}

	@Override
	public Collection< Character > getCharacters() {
		return widths.keySet();
	}
	
	public static BananaFontProviderNegativeSpace getProvider() {
		return INSTANCE;
	}
}
