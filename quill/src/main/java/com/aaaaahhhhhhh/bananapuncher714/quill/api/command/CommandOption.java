package com.aaaaahhhhhhh.bananapuncher714.quill.api.command;

import org.bukkit.command.CommandSender;

import com.aaaaahhhhhhh.bananapuncher714.quill.api.command.executor.CommandExecutable;

public class CommandOption {
	protected CommandExecutable exe;
	protected String[] args;
	protected CommandContext parameter;
	
	public CommandOption( CommandExecutable exe, String[] args, CommandContext parameter ) {
		this.exe = exe;
		this.args = args;
		this.parameter = parameter;
	}
	
	public int getArgumentSize() {
		return args.length;
	}
	
	public void execute( CommandSender sender ) {
		exe.execute( sender, args, parameter );
	}
}
