package com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.resourcepack.font.FontIndex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ResourcePackZip extends ResourcePack {
	protected static Gson GSON = new GsonBuilder().create();
	protected ZipWrapper zip;
	
	public ResourcePackZip( Path file ) throws IOException {
		zip = new ZipWrapper( file );
		
		if ( zip.contains( "pack.png" ) ) {
			packImage = ImageIO.read( zip.readElement( "pack.png" ) );
		}
		
		if ( zip.contains( "pack.mcmeta" ) ) {
			JsonObject obj = GSON.fromJson( new InputStreamReader( zip.readElement( "pack.mcmeta" ), "UTF-8" ), JsonObject.class );
			packMcmeta = new PackDescription( obj );
		} else {
			packMcmeta = new PackDescription();
			JsonObject object = packMcmeta.toJsonObject();
			zip.addElement( "pack.mcmeta", GSON.toJson( object ).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
		}
		
		if ( zip.contains( "assets/minecraft/font" ) ) {
			for ( String path : zip.walkElements( "assets/minecraft/font" ) ) {
				if ( path.endsWith( ".json" ) ) {
					JsonObject obj = GSON.fromJson( new InputStreamReader( zip.readElement( "assets/minecraft/font/" + path ), "UTF-8" ), JsonObject.class );
					FontIndex font = new FontIndex( obj );
					String fontName = path.substring( 0, path.length() - 5 );
					
					fonts.put( fontName, font );
				}
			}
		}
	}
	
	public ZipWrapper getWrapper() {
		return zip;
	}

	public Map< String, FontIndex > getFonts() {
		return fonts;
	}
	
	public void setPackImage( BufferedImage image ) {
		super.setPackImage( image );
		
		try {
			zip.addImage( "pack.png", packImage, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public void addResource( NamespacedKey key, File file ) throws IOException {
		zip.addElement( "assets/" + key.namespace + "/" + key.key, file, StandardCopyOption.REPLACE_EXISTING );
	}
	
	public InputStream getResource( NamespacedKey key ) throws IOException {
		return zip.readElement( "assets/" + key.namespace + "/" + key.key );
	}
	
	public void addTexture( NamespacedKey path, BufferedImage image ) throws IOException {
		zip.addImage( "assets/" + path.namespace + "/textures/" + path.key, image, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
	}
	
	public InputStream getTexture( NamespacedKey path ) throws IOException {
		return zip.readElement( "assets/" + path.namespace + "/textures/" + path.key );
	}
	
	public void save() throws IOException {
		JsonObject packObj = packMcmeta.toJsonObject();
		zip.addElement( "pack.mcmeta", GSON.toJson( packObj ).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
		
		for ( Entry< String, FontIndex > entry : fonts.entrySet() ) {
			JsonObject fontObj = entry.getValue().toJsonObject();
			zip.addElement( "assets/minecraft/font/" + entry.getKey() +  ".json", GSON.toJson( fontObj ).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE );
		}
	}
	
	public void close() throws IOException {
		zip.close();
	}
}
