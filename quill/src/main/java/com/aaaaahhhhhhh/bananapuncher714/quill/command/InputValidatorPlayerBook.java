package com.aaaaahhhhhhh.bananapuncher714.quill.command;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.quill.Library;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.InputValidator;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.InputValidatorPlayer;

public class InputValidatorPlayerBook implements InputValidator< PlayerBookPair > {
	private Supplier< Library > librarySupplier;
	private String defaultCatalogId;
	private InputValidator< ? extends Player > playerValidator;
	private InputValidator< ? extends NamespacedKey > namespaceValidator;
	
	public InputValidatorPlayerBook( String defaultCatalogId, Supplier< Library > supplier, boolean usePerms ) {
		this.defaultCatalogId = defaultCatalogId;
		librarySupplier = supplier;
		
		playerValidator = new InputValidatorPlayer();
		namespaceValidator = new InputValidatorBook( this.defaultCatalogId, librarySupplier, usePerms );
	}
	
	@Override
	public Collection< String > getTabCompletes( CommandSender sender, String[] input ) {
		if ( input.length == 1 ) {
			return playerValidator.getTabCompletes( sender, new String[] { input[ 0 ] } );
		} else if ( input.length == 2 ) {
			Player player = Bukkit.getPlayerExact( input[ 0 ] );
			return namespaceValidator.getTabCompletes( player, new String[] { input[ 1 ] } );
		} else {
			return new HashSet< String >();
		}
	}

	@Override
	public boolean isValid( CommandSender sender, String[] input, String[] args ) {
		if ( input.length == 2 ) {
			// TODO Maybe use the isValid instead of null check?
			Player player = playerValidator.get( sender, new String[] { input[ 0 ] } );
			if ( player != null ) {
				return namespaceValidator.isValid( player, new String[] { input[ 1 ] }, args );
			}
		}
		
		return false;
	}

	@Override
	public PlayerBookPair get( CommandSender sender, String input[] ) {
		if ( input.length == 2 ) {
			Player player = playerValidator.get( sender, new String[] { input[ 0 ] } );
			if ( player != null ) {
				NamespacedKey key = namespaceValidator.get( player, new String[] { input[ 1 ] } );
				if ( key != null ) {
					return new PlayerBookPair( player, key );
				}
			}
		}
		return null;
	}

	@Override
	public int getArgumentCount() {
		return 2;
	}
}
