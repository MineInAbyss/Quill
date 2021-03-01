package com.aaaaahhhhhhh.bananapuncher714.quill.font;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JLabel;

public class BananaFontProviderTTF implements BananaFontProvider {
	protected Font font;
	protected FontMetrics metrics;
	
	public BananaFontProviderTTF( Font font ) {
		this.font = font;
		metrics = new JLabel().getFontMetrics( font );
	}

	@Override
	public int getWidthFor( char c ) {
		return metrics.stringWidth( c + "" );
	}

	@Override
	public boolean contains( char c ) {
		return font.canDisplay( c );
	}

	@Override
	public Collection< Character > getCharacters() {
		return new HashSet< Character >();
	}
}
