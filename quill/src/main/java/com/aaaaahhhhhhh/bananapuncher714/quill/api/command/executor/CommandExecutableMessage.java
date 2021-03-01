package com.aaaaahhhhhhh.bananapuncher714.quill.api.command.executor;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.CommandContext;

public class CommandExecutableMessage implements CommandExecutable {
	protected String message;
	
	public CommandExecutableMessage( String message ) {
		this.message = message;
	}
	
	@Override
	public void execute( CommandSender sender, String[] args, CommandContext params ) {
		sender.sendMessage( message );
	}
}
