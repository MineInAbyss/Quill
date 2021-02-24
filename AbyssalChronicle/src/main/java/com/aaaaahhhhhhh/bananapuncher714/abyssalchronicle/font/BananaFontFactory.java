package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.ResourcePackZip;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontIndex;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProvider;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderBitmap;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderLegacy;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.resourcepack.font.FontProviderTTF;

public class BananaFontFactory {
	public static BananaFont constructFrom( ResourcePackZip resourcePack, FontIndex index ) throws IOException {
		BananaFont font = new BananaFont();
		for ( FontProvider provider : index.getProviders() ) {
			if ( provider instanceof FontProviderBitmap ) {
				FontProviderBitmap bitmap = ( FontProviderBitmap ) provider;
				
				InputStream resource = resourcePack.getTexture( bitmap.getFile() );
				try {
					BufferedImage image = ImageIO.read( resource );
					BananaFontProvider bitmapProvider = new BananaFontProviderBitmap( image, bitmap.getChars(), bitmap.getHeight() );

					font.addProvider( bitmapProvider );
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			} else if ( provider instanceof FontProviderTTF ) {
				FontProviderTTF ttf = ( FontProviderTTF ) provider;
				
				InputStream resource = resourcePack.getResource( ttf.getFile() );
				try {
					Font jFont = Font.createFont( Font.TRUETYPE_FONT, resource );
					BananaFontProvider bananaProvider = new BananaFontProviderTTF( jFont );
					font.addProvider( bananaProvider );
				} catch ( FontFormatException | IOException e ) {
					e.printStackTrace();
				}
			} else if ( provider instanceof FontProviderLegacy ) {
				FontProviderLegacy legacy = ( FontProviderLegacy ) provider;
				InputStream resource = resourcePack.getResource( legacy.getSizes() );
				BananaFontProvider container = new MinecraftFontContainer( resource );
				font.addProvider( container );
			}
		}
		
		return font;
	}
}
