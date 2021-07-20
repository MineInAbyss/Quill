package com.aaaaahhhhhhh.bananapuncher714.quill.api;

import org.bukkit.command.CommandSender;

public interface TextTransformer< T, R > {
	R transform( CommandSender sender, T text );
}
