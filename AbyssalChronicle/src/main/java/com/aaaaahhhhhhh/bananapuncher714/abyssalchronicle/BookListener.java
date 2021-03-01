package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class BookListener implements Listener {
	private AbyssalChronicle plugin;
	
	protected BookListener( AbyssalChronicle plugin ) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onEvent( PlayerInteractEvent event ) {
		if ( event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR ) {
			ItemStack item = event.getItem();
			if ( item != null && item.getType() != Material.AIR ) {
				ItemMeta meta = item.getItemMeta();
				org.bukkit.NamespacedKey key = plugin.getBookId();
				if ( meta.getPersistentDataContainer().has( key, PersistentDataType.STRING ) ) {
					event.setCancelled( true );
					String id = meta.getPersistentDataContainer().get( key, PersistentDataType.STRING );
				
					NamespacedKey bookId = NamespacedKey.fromString( id );
					
					Player player = event.getPlayer();
					Library library = plugin.getLibrary();
					if ( library != null ) {
						Optional< Book > optionalBook = plugin.getLibrary().getBook( player, bookId );
						if ( optionalBook.isPresent() ) {
							Book book = optionalBook.get();
							
							ItemStack bookItem = new ItemStack( Material.WRITTEN_BOOK );
							BookMeta bookMeta = ( BookMeta ) bookItem.getItemMeta();
							bookMeta.setAuthor( book.getAuthor() );
							bookMeta.setTitle( book.getTitle() );
							for ( BaseComponent page : book.getPages() ) {
								bookMeta.spigot().addPage( new BaseComponent[] { page } );
							}
							bookItem.setItemMeta( bookMeta );
							
							player.openBook( bookItem );
						} else {
							player.spigot().sendMessage( ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText( plugin.getMessage("cannot-read"  ) ) );
						}
					} else {
						player.sendMessage( ChatColor.RED + "The library does not exist!" );
					}
				}
			}
		}
	}
}
