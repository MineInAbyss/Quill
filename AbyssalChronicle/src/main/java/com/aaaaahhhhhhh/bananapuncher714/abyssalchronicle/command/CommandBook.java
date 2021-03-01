package com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.AbyssalChronicle;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.Library;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.Updateable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.CommandContext;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.executor.CommandExecutable;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.executor.CommandExecutableMessage;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.validator.InputValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.validator.sender.SenderValidatorNotPlayer;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.validator.sender.SenderValidatorPermission;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.api.command.validator.sender.SenderValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.Catalog;
import com.aaaaahhhhhhh.bananapuncher714.abyssalchronicle.catalog.CatalogBuildable;

import net.md_5.bungee.api.chat.BaseComponent;

public class CommandBook {
	private static final CommandExecutable INVALID_PLAYER = new CommandExecutableMessage( ChatColor.RED + "Invalid player!" );
	
	private PluginCommand pluginCommand;
	private SubCommand command;
	private AbyssalChronicle plugin;
	
	public CommandBook( AbyssalChronicle plugin, PluginCommand command ) {
		this.plugin = plugin;
		this.pluginCommand = command;
		
		applyToCommand();
	}
	
	private void applyToCommand() {
		this.command = new SubCommand( "book" )
				.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command" ) )
				.add( new SubCommand( "update" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.update" ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book update" ) )
						.defaultTo( this::update ) )
				.add( new SubCommand( "list" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.list" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorPlayer() )
								.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.list.other" ) )
								.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book list [player]" ) )
								.defaultTo( this::listOther ) )
						.whenUnknown( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.list.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Invalid player!" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book list" );
							}
						} )
						.defaultTo( this::list ) )
				.add( new SubCommand( "list" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.list" ) )
						.addSenderValidator( new SenderValidatorNotPlayer() )
						.add( new SubCommand( new InputValidatorPlayer() )
								.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.list.other" ) )
								.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book list <player>" ) )
								.defaultTo( this::listOther ) )
						.whenUnknown( INVALID_PLAYER )
						.defaultTo( new CommandExecutableMessage( ChatColor.RED + "Usage: /book list <player>" ) ) )
				.add( new SubCommand( "read" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.read" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorBook( AbyssalChronicle.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.read.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book read <book> [player]" ) )
										.defaultTo( this::readForOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.read.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book read <book>" );
									}
								} )
								.defaultTo( this::readSelf ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.read.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book read <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book read <book>" );
							}
						} ) )
				.add( new SubCommand( "readother" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.readother" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorPlayerBook( AbyssalChronicle.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.readother.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book readother <player> <book> [player]" ) )
										.defaultTo( this::readOtherOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.readother.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book readother <player> <book>" );
									}
								} )
								.defaultTo( this::readSelfOther ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid player or book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.readother.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book readother <player> <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book readother <player> <book>" );
							}
						} ) )
				.add( new SubCommand( "openfor" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.openfor" ) )
						.addSenderValidator( new SenderValidatorNotPlayer() )
						.add( new SubCommand( new InputValidatorPlayerBook( AbyssalChronicle.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.openfor.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book openfor <player> <book> [player]" ) )
										.defaultTo( this::readOtherOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.openfor.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book openfor <player> <book>" );
									}
								} )
								.defaultTo( this::readFromConsole ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid player or book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.openfor.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book openfor <player> <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book openfor <player> <book>" );
							}
						} ) )
				.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid argument!" ) )
				.defaultTo( new CommandExecutableMessage( ChatColor.RED + "You must provide an argument!" ) )
				.applyTo( pluginCommand );
	}
	
	private void update( CommandSender sender, String[] args, CommandContext parameters ) {
		Library library = plugin.getLibrary();
		if ( library != null ) {
			sender.sendMessage( ChatColor.DARK_AQUA + "Updating catalogs..." );
			boolean updated = false;
			boolean hadUpdateable = false;
			for ( Catalog catalog : library.getCatalogs() ) {
				if ( catalog instanceof Updateable ) {
					hadUpdateable = true;
					boolean catalogUpdated = ( ( Updateable ) catalog).update();
					updated = updated || catalogUpdated;
					if ( updated ) {
						sender.sendMessage( ChatColor.AQUA + "Updated catalog '" + ChatColor.YELLOW + catalog.getId() + ChatColor.AQUA + "'!" );
					}
				}
			}
			
			if ( !updated ) {
				if ( hadUpdateable ) {
					sender.sendMessage( ChatColor.AQUA + "Everything was up to date!" );
				} else {
					sender.sendMessage( ChatColor.AQUA + "No updateable catalogs found!" );
				}
			}
		} else {
			sender.sendMessage( ChatColor.RED + "The library does not exist!" );
		}
	}
	
	private void list( CommandSender sender, String[] args, CommandContext parameters ) {
		listFor( sender, sender );
	}
	
	private void listOther( CommandSender sender, String[] args, CommandContext parameters ) {
		listFor( sender, parameters.getLast( Player.class ) );
	}
	
	private void listFor( CommandSender sender, CommandSender other ) {
		Library library = plugin.getLibrary();
		if ( library != null ) {
			StringBuilder builder = new StringBuilder( ChatColor.AQUA + "Available books:" + ChatColor.RESET + "\n" );
			boolean hasBook = false;
			Collection< Catalog > catalogs = library.getCatalogs();
			for ( Iterator< Catalog > catalogIt = catalogs.iterator(); catalogIt.hasNext(); ) {
				Catalog catalog = catalogIt.next();
				Set< String > books = catalog.getAvailableBooks( other );
				if ( !books.isEmpty() ) {
					hasBook = true;
					if ( catalog.getId().equals( AbyssalChronicle.DEFAULT_CATALOG_NAMESPACE ) ) {
						builder.append( ChatColor.LIGHT_PURPLE + "Default catalog '" + catalog.getId() + "':" + ChatColor.RESET + "\n" );
					} else {
						builder.append( ChatColor.DARK_PURPLE + "Catalog '" + catalog.getId() + "':" + ChatColor.RESET + "\n" );
					}
					
					for ( Iterator< String > iterator = books.iterator(); iterator.hasNext(); ) {
						builder.append( ChatColor.WHITE + "- " + ChatColor.YELLOW + iterator.next() );
						
						if ( iterator.hasNext() ) {
							builder.append( ChatColor.RESET + "\n" );
						}
					}
					if ( catalogIt.hasNext() ) {
						builder.append( ChatColor.RESET + "\n" );
					}
				}
			}
			
			if ( hasBook ) {
				sender.sendMessage( builder.toString() );
			} else {
				sender.sendMessage( ChatColor.RED + "There are no books available!" );
			}
		} else {
			sender.sendMessage( ChatColor.RED + "The library does not exist!" );
		}
	}
	
	private void readSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		readFor( sender, sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void readForOther( CommandSender sender, String[] args, CommandContext parameters ) {
		readFor( parameters.getLast( Player.class ), sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void readFromConsole( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( pair.getPlayer(), pair.getPlayer(), pair.getBook() );
	}
	
	private void readSelfOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( sender, pair.getPlayer(), pair.getBook() );
	}
	
	private void readOtherOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( parameters.getLast( Player.class ), pair.getPlayer(), pair.getBook() );
	}
	
	private void readFor( CommandSender sender, CommandSender reader, NamespacedKey key ) {
		Library library = plugin.getLibrary();
		if ( library != null ) {
			Optional< Book > optionalBook = library.getBook( sender, key );
			if ( optionalBook.isPresent() ) {
				Book book = optionalBook.get();
				if ( reader instanceof Player ) {
					Player player = ( Player ) reader;
					
					ItemStack bookItem = new ItemStack( Material.WRITTEN_BOOK );
					BookMeta meta = ( BookMeta ) bookItem.getItemMeta();
					meta.setAuthor( book.getAuthor() );
					meta.setTitle( book.getTitle() );
					for ( BaseComponent page : book.getPages() ) {
						meta.spigot().addPage( new BaseComponent[] { page } );
					}
					bookItem.setItemMeta( meta );

					player.openBook( bookItem );
				} else {
					reader.sendMessage( ChatColor.RED + "Can only display books to players!" );
				}
			} else {
				reader.sendMessage( ChatColor.RED + "Unknown book!" );
			}
		} else {
			reader.sendMessage( ChatColor.RED + "The library does not exist!" );
		}
	}
}