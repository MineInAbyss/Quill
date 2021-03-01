package com.aaaaahhhhhhh.bananapuncher714.quill.api.command.executor;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.CommandContext;

public interface CommandExecutable {
	void execute( CommandSender sender, String[] args, CommandContext params );
}
