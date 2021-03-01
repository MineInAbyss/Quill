package com.aaaaahhhhhhh.bananapuncher714.quill.dependencies;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.TextTransformer;

import me.clip.placeholderapi.PlaceholderAPI;

public class TextTransformerPlaceholderAPI implements TextTransformer {
	@Override
	public String transform( CommandSender sender, String text ) {
		if ( sender instanceof OfflinePlayer ) {
			return PlaceholderAPI.setPlaceholders( ( OfflinePlayer ) sender, text );
		}
		return PlaceholderAPI.setPlaceholders( ( OfflinePlayer ) null, text );
	}
}
