package com.aaaaahhhhhhh.bananapuncher714.quill.command;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.aaaaahhhhhhh.bananapuncher714.quill.Quill;
import com.aaaaahhhhhhh.bananapuncher714.quill.Library;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.NamespacedKey;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.Updateable;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.CommandContext;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.SubCommand;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.executor.CommandExecutable;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.executor.CommandExecutableMessage;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.InputValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.sender.SenderValidatorNotPlayer;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.sender.SenderValidatorPermission;
import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.validator.sender.SenderValidatorPlayer;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.Book;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.Catalog;

import net.md_5.bungee.api.chat.BaseComponent;

public class CommandBook {
	private static final CommandExecutable INVALID_PLAYER = new CommandExecutableMessage( ChatColor.RED + "Invalid player!" );
	
	private PluginCommand pluginCommand;
	private SubCommand command;
	private Quill plugin;
	
	public CommandBook( Quill plugin, PluginCommand command ) {
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
				.add( new SubCommand( "updateassets" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.updateassets" ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book updateassets" ) )
						.defaultTo( this::updateAssets ) )
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
						.add( new SubCommand( new InputValidatorBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.read.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book read <book> [player]" ) )
										.defaultTo( this::readForOtherWithSelf ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.read.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book read <book>" );
									}
								} )
								.defaultTo( this::readForSelf ) )
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
						.add( new SubCommand( new InputValidatorPlayerBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.readother.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book readother <player> <book> [player]" ) )
										.defaultTo( this::readForOtherWithOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.readother.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book readother <player> <book>" );
									}
								} )
								.defaultTo( this::readForSelfWithOther ) )
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
						.add( new SubCommand( new InputValidatorPlayerBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.openfor.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book openfor <player> <book> [player]" ) )
										.defaultTo( this::readForOtherWithOther ) )
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
				.add( new SubCommand( "give" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.give" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.give.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book give <book> [player]" ) )
										.defaultTo( this::giveToOtherFromSelf ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.give.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book give <book>" );
									}
								} )
								.defaultTo( this::giveSelf ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.give.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book give <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book give <book>" );
							}
						} ) )
				.add( new SubCommand( "giveother" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.giveother" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorPlayerBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.giveother.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book giveother <player> <book> [player]" ) )
										.defaultTo( this::giveToOtherFromOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.giveother.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book giveother <player> <book>" );
									}
								} )
								.defaultTo( this::giveToSelfFromOther ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid player or book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.giveother.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book giveother <player> <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book giveother <player> <book>" );
							}
						} ) )
				.add( new SubCommand( "give" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.give" ) )
						.addSenderValidator( new SenderValidatorNotPlayer() )
						.add( new SubCommand( new InputValidatorPlayerBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.add( new SubCommand( new InputValidatorPlayer() )
										.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.give.other" ) )
										.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book give <player> <book> [player]" ) )
										.defaultTo( this::giveToOtherFromOther ) )
								.whenUnknown( ( sender, args, parameters ) -> {
									if ( sender.hasPermission( "abyssalchronicle.book.command.give.other" ) ) {
										sender.sendMessage( ChatColor.RED + "Invalid player!" );
									} else {
										sender.sendMessage( ChatColor.RED + "Usage: /book give <player> <book>" );
									}
								} )
								.defaultTo( this::giveFromConsole ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid player or book!" ) )
						.defaultTo( ( sender, args, parameters ) -> {
							if ( sender.hasPermission( "abyssalchronicle.book.command.give.other" ) ) {
								sender.sendMessage( ChatColor.RED + "Usage: /book give <player> <book> [player]" );
							} else {
								sender.sendMessage( ChatColor.RED + "Usage: /book give <player> <book>" );
							}
						} ) )
				.add( new SubCommand( "set" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.set" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book set <book>" ) )
								.defaultTo( this::setSelf ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid book!" ) )
						.defaultTo( new CommandExecutableMessage( ChatColor.RED + "Usage: /book set <book>" ) ) )
				.add( new SubCommand( "setother" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.setother" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.add( new SubCommand( new InputValidatorPlayerBook( Quill.DEFAULT_CATALOG_NAMESPACE, plugin::getLibrary ) )
								.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book setother <player> <book>" ) )
								.defaultTo( this::setOther ) )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Invalid player or book!" ) )
						.defaultTo( new CommandExecutableMessage( ChatColor.RED + "Usage: /book setother <player> <book>" ) ) )
				.add( new SubCommand( "unset" )
						.addSenderValidator( new SenderValidatorPermission( "abyssalchronicle.book.command.unset" ) )
						.addSenderValidator( new SenderValidatorPlayer() )
						.whenUnknown( new CommandExecutableMessage( ChatColor.RED + "Usage: /book unset" ) )
						.defaultTo( this::unset ) )
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
	
	private void updateAssets( CommandSender sender, String[] args, CommandContext parameters ) {
		sender.sendMessage( ChatColor.DARK_AQUA + "Updating assets..." );
		plugin.reloadAssets();
		sender.sendMessage( ChatColor.AQUA + "Updated assets successfully!" );
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
					if ( catalog.getId().equals( Quill.DEFAULT_CATALOG_NAMESPACE ) ) {
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
	
	private void readForSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		readFor( sender, sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void readForOtherWithSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		readFor( parameters.getLast( Player.class ), sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void readFromConsole( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( pair.getPlayer(), pair.getPlayer(), pair.getBook() );
	}
	
	private void readForSelfWithOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( pair.getPlayer(), sender, pair.getBook() );
	}
	
	private void readForOtherWithOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		readFor( parameters.getLast( Player.class ), pair.getPlayer(), pair.getBook() );
	}
	
	private void readFor( CommandSender lender, CommandSender reader, NamespacedKey key ) {
		Library library = plugin.getLibrary();
		if ( library != null ) {
			Optional< Book > optionalBook = library.getBook( lender, key );
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
	
	private void giveSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		giveFor( sender, sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void giveToOtherFromSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		giveFor( sender, sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void giveFromConsole( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		giveFor( pair.getPlayer(), pair.getPlayer(), pair.getBook() );
	}
	
	private void giveToSelfFromOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		giveFor( pair.getPlayer(), sender, pair.getBook() );
	}
	
	private void giveToOtherFromOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		giveFor( parameters.getLast( Player.class ), pair.getPlayer(), pair.getBook() );
	}
	
	private void giveFor( CommandSender lender, CommandSender reader, NamespacedKey key ) {
		Library library = plugin.getLibrary();
		if ( library != null ) {
			Optional< Book > optionalBook = library.getBook( lender, key );
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

					player.getInventory().addItem( bookItem );
				} else {
					reader.sendMessage( ChatColor.RED + "Can only give books to players!" );
				}
			} else {
				reader.sendMessage( ChatColor.RED + "Unknown book!" );
			}
		} else {
			reader.sendMessage( ChatColor.RED + "The library does not exist!" );
		}
	}
	
	private void setSelf( CommandSender sender, String[] args, CommandContext parameters ) {
		set( sender, parameters.getLast( NamespacedKey.class ) );
	}
	
	private void setOther( CommandSender sender, String[] args, CommandContext parameters ) {
		PlayerBookPair pair = parameters.getLast( PlayerBookPair.class );
		set( sender, pair.getBook() );
	}
	
	private void set( CommandSender sender, NamespacedKey key ) {
		if ( sender instanceof Player ) {
			Player player = ( Player ) sender;
			
			ItemStack item = player.getEquipment().getItemInMainHand();
			if ( item != null && item.getType() != Material.AIR ) {
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().set( plugin.getBookId(), PersistentDataType.STRING, key.toString() );
				item.setItemMeta( meta );
				player.sendMessage( ChatColor.AQUA + "Item set to book '" + key + "'!" );
			} else {
				player.sendMessage( ChatColor.RED + "You must be holding an item!" );
			}
		} else {
			sender.sendMessage( ChatColor.RED + "You must be a player to run this command!" );
		}
	}
	
	private void unset( CommandSender sender, String[] args, CommandContext parameters ) {
		if ( sender instanceof Player ) {
			Player player = ( Player ) sender;
			
			ItemStack item = player.getEquipment().getItemInMainHand();
			if ( item != null && item.getType() != Material.AIR ) {
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().remove( plugin.getBookId() );
				item.setItemMeta( meta );
				player.sendMessage( ChatColor.AQUA + "Item unset!" );
			} else {
				player.sendMessage( ChatColor.RED + "You must be holding a book item!" );
			}
		} else {
			sender.sendMessage( ChatColor.RED + "You must be a player to run this command!" );
		}
	}
}
