package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.command;

import org.bukkit.entity.Player;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;

public class PlayerBookPair {
	private final Player player;
	private final NamespacedKey book;
	
	public PlayerBookPair( Player player, NamespacedKey book ) {
		this.player = player;
		this.book = book;
	}

	public Player getPlayer() {
		return player;
	}

	public NamespacedKey getBook() {
		return book;
	}
}
