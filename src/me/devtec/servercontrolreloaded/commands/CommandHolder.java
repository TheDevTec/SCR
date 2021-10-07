package me.devtec.servercontrolreloaded.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

public abstract class CommandHolder implements CommandExecutor, TabCompleter {
	protected String section, name;
	public CommandHolder(String section, String name) {
		this.section=section;
		this.name=name;
	}
	
	public boolean check(CommandSender s) {
		if (Loader.has(s, name, section)) {
			if(!CommandsManager.canUse(section+"."+name, s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire(section+"."+name, s))));
				return false;
			}
			return true;
		}
		Loader.noPerms(s, name, section);
		return false;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args) {
		if(check(s))
			command(s, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		if (Loader.has(s, name, section))
			return tabCompleter(s, args);
		return Collections.emptyList();
	}
	
	public abstract List<String> tabCompleter(CommandSender s, String[] args);

	protected abstract void command(CommandSender s, String[] args);
}