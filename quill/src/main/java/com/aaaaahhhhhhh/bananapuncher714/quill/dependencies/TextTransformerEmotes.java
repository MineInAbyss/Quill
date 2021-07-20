package com.aaaaahhhhhhh.bananapuncher714.quill.dependencies;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.TextTransformer;

import io.github.bananapuncher714.bondrewd.likes.his.emotes.BondrewdLikesHisEmotes;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextTransformerEmotes implements TextTransformer< TextComponent, TextComponent[] > {
	private BondrewdLikesHisEmotes plugin;
	
	public TextTransformerEmotes() {
		plugin = ( BondrewdLikesHisEmotes ) Bukkit.getPluginManager().getPlugin( "BondrewdLikesHisEmotes" );
	}
	
	@Override
	public TextComponent[] transform( CommandSender sender, TextComponent text ) {
		BaseComponent res = plugin.transformComponent( text );
		if ( res instanceof TextComponent ) {
			return new TextComponent[] { ( TextComponent ) res };
		}
		return new TextComponent[] { text };
	}
}
