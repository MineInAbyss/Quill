package com.aaaaahhhhhhh.bananapuncher714.quill.dependencies;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPage;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.BookPart;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponent;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentObject;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.BookComponentText;
import com.aaaaahhhhhhh.bananapuncher714.quill.book.component.transformer.ComponentTransformer;
import com.aaaaahhhhhhh.bananapuncher714.quill.catalog.CatalogBuildable;

public class ComponentTransformerEmotes implements ComponentTransformer {
	private static Map< String, Character > EMOTES;
	
	static {
		Plugin plugin = Bukkit.getPluginManager().getPlugin( "BondrewdLikesHisEmotes" );
		if ( plugin != null ) {
			Field emoteField;
			try {
				emoteField = plugin.getClass().getDeclaredField( "emotes" );
				emoteField.setAccessible( true );
				
				EMOTES = ( Map< String, Character > ) emoteField.get( plugin );
			} catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
				EMOTES = new HashMap< String, Character >();
				e.printStackTrace();
			}
		} else {
			EMOTES = new HashMap< String, Character >();
		}
	}
	
	private Set< BookComponentText > processed = Collections.newSetFromMap( new WeakHashMap< BookComponentText, Boolean >() ); 
	
	public ComponentTransformerEmotes( CommandSender sender, CatalogBuildable cache, BookPart part ) {
	}
	
	@Override
	public boolean transform( List< BookPage > pages, BookComponent component, Deque< BookComponent > components ) {
		if ( !EMOTES.isEmpty() ) {
			if ( component.isTextComponent() ) {
				BookComponentText text = component.getAsTextComponent();
				if ( !processed.contains( text ) ) {
					Deque< BookComponent > oldQueue = new ArrayDeque< BookComponent >();
					oldQueue.add( text );
					for ( Entry< String, Character > entry : EMOTES.entrySet() ) {
						String key = String.format( Pattern.quote( ":%s:" ), entry.getKey() );
						
						Deque< BookComponent > newQueue = new ArrayDeque< BookComponent >();
						// Scan through each book component
						for ( BookComponent comp : oldQueue ) {
							if ( comp.isTextComponent() ) {
								BookComponentText textComp = comp.getAsTextComponent();
								String content = textComp.getText();
								String[] notEmotes = content.split( "(?<!\\\\)" + key, -1 );
								if ( notEmotes.length > 1 ) {
									for ( int i = 0; i < notEmotes.length; i++ ) {
										String plain = notEmotes[ i ];
										if ( !plain.isEmpty() ) {
											BookComponentText plainComp = new BookComponentText( plain.replace( "\\\\" + key, key ) );
											newQueue.add( plainComp );
										}
										if ( i < notEmotes.length - 1 ) {
											// Add the emote and set the color to 0xFFFFFF so that it actually shows up
											BookComponentText emoteText = new BookComponentText( String.valueOf( entry.getValue() ) );
											BookComponentObject colorWrap = new BookComponentObject( "color" );
											colorWrap.getAttributes().put( "value", "0xFFFFFF" );
											colorWrap.getSubElements().add( emoteText );
											
											BookComponentObject fontWrap = new BookComponentObject( "font" );
											fontWrap.getSubElements().add( colorWrap );
											
											BookComponentObject boldWrap = new BookComponentObject( "unbold" );
											boldWrap.getSubElements().add( fontWrap );
											
											BookComponentObject underlineWrap = new BookComponentObject( "derline" );
											underlineWrap.getSubElements().add( boldWrap );
											
											newQueue.add( underlineWrap );
										}
									}
								} else {
									newQueue.add( comp );
								}
							} else {
								newQueue.add( comp );
							}
						}
						
						oldQueue = newQueue;
					}
					
					// Keep track of what components we're adding to the queue so we don't re-process them 
					Deque< BookComponent > allComponents = new ArrayDeque< BookComponent >( oldQueue );
					while ( !allComponents.isEmpty() ) {
						BookComponent base = allComponents.poll();
						
						if ( base.isObjectComponent() ) {
							BookComponentObject object = base.getAsObjectComponent();
							for ( int i = object.getSubElements().size() - 1; i >= 0; i-- ) {
								allComponents.addFirst( object.getSubElements().get( i ) );
							}
						} else if ( base.isTextComponent() ) {
							processed.add( base.getAsTextComponent() );
						}
					}
					
					// oldQueue should contain all the new elements
					// Push all the elements back onto the stack
					while ( !oldQueue.isEmpty() ) {
						components.addFirst( oldQueue.pollLast() );
					}
					
					// Stop any more transformers from getting access to this component
					return true;
				}
			}
		}
		
		return false;
	}

}
