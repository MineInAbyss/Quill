package com.aaaaahhhhhhh.bananapuncher714.quill.api.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import com.aaaaahhhhhhh.bananapuncher714.quill.util.BukkitUtil;

public class CommandBase {
	protected PluginCommand command;
	
	public CommandBase( Plugin plugin, String command ) {
		this.command = BukkitUtil.constructCommand( plugin, command );
	}
	
	public CommandBase setPermission( String permission ) {
		command.setPermission( permission );
		return this;
	}
	
	public CommandBase setDescription( String description ) {
		command.setDescription( description );
		return this;
	}
	
	public CommandBase addAliases( String... aliases ) {
		Set< String > aliasSet = new HashSet< String >( command.getAliases() );
		for ( String alias : aliases ) {
			aliasSet.add( alias );
		}
		command.setAliases( new ArrayList< String >( aliasSet ) );
		return this;
	}
	
	public CommandBase addAliases( Collection< String > aliases ) {
		Set< String > aliasSet = new HashSet< String >( command.getAliases() );
		aliasSet.addAll( aliases );
		command.setAliases( new ArrayList< String >( aliasSet ) );
		return this;
	}
	
	public CommandBase setPermission( Permission permission ) {
		return setPermission( permission.toString() );
	}
	
	public CommandBase setSubCommand( SubCommand subCommand ) {
		subCommand.applyTo( command );
		return this;
	}
	
	public PluginCommand build() {
		return command;
	}
}
