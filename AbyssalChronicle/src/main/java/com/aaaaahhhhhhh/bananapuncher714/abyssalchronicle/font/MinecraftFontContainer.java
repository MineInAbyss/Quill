package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;

public class MinecraftFontContainer implements BananaFontProvider {
	/** Array of the start/end column (in upper/lower nibble) for every glyph in the /font directory. */
	private final byte[] glyphWidth = new byte[65536];
	
	public MinecraftFontContainer( InputStream glyph_sizes ) {
		try {
			glyph_sizes.read( glyphWidth );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	private int getCharWidth( char character )  {
		if (character == 160) {
			return 4; // forge: display nbsp as space. MC-2595
		}
		if (character == 167) {
			return -1;
		} else if (character == ' ') {
			return 4;
		} else if ( character == '\n' ) {
			return 0;
		} else {
            if (this.glyphWidth[character] != 0) {
                int j = this.glyphWidth[character] & 255;
                int k = j >>> 4;
                int l = j & 15;
                ++l;
                return (l - k) / 2 + 1;
            } else {
                return 4;
            }
		}
	}
	
	/**
	 * @deprecated
     * Returns the width of this string. Equivalent of FontMetrics.stringWidth(String s).
     */
    private int getStringWidth( String text ) {
        if ( text == null ) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                int k = getCharWidth(c0);
                
                if ( k < 0 && j < text.length() - 1 ) {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }
                    k = 0;
                }
                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }
            return i;
        }
    }
    
    /**
     * @deprecated
     * Returns the width of this message. Does not check if message is bold, and strips all color codes
     */
    private int getStringWidth( String message, boolean isBold ) {
    	int len = 0;
    	for ( char ch : ChatColor.stripColor( message ).toCharArray() ) {
    		if ( isBold ) {
    			len++;
    		}
    		len = len + getCharWidth( ch );
    	}
    	return len;
    }

	@Override
	public int getWidthFor( char c ) {
		return getCharWidth( c );
	}

	@Override
	public boolean contains( char c ) {
		return c < '\uE000' || c > '\uF8FF';
	}

	@Override
	public Collection< Character > getCharacters() {
		Set< Character > characters = new HashSet< Character >();
		for ( char i = 0; i < '\uE000'; i++ ) {
			characters.add( i );
		}
		for ( char i = '\uFFFF' ; i > '\uF8FF'; i-- ) {
			characters.add( i );
		}
		return characters;
	}
}
