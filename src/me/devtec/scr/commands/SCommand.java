package me.devtec.scr.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.scr.Loader;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.utils.Messages;
import me.devtec.scr.utils.Messages.Placeholder;
import me.devtec.shared.utility.StringUtils;

public abstract class SCommand implements CommandExecutor, TabCompleter {

	protected String name;
	public SCommand(String name) {
		this.name = name;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if(canUseCommand(API.getUser(sender)) || API.getUser(sender).isConsole())
			command(API.getUser(sender), cmd, args);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		User u = API.getUser(sender);
		if (u.isAutorized(getPerm())) {
		List<String> tab = tabCompleter(u, cmd, args);
		return tab==null?API.getPlayerNames(sender):tab; //TODO - get Players (vanished, spectator, ...)
	}
	return Collections.emptyList();
		return null;
	}

	public abstract List<String> tabCompleter(User user, Command cmd, String[] args);

	public abstract void command(User user, Command cmd, String[] args);
	
	public boolean canUseCommand(User user) {
		if(user.isAutorized(getPerm())) {
			if(!CommandsManager.cooldownExpired(user, name)) { //Cooldown check
				Messages.message(user.player, "Cooldowns.Commands",
						Placeholder.c().replace("%time%", StringUtils.timeToString( CommandsManager.expires(user, name) ) )
						.replace("%time_sec%", CommandsManager.expires(user, name) ));
				return false;
			}
			return true;
		}
		Messages.noPerm(user.player, getPerm());
		return false;
	}
	
	//Permissions
	public String getPerm() { //Default permission
		return Loader.commands.getString(name+".permission.default");
	}
	public String getPerm(String subcommand) { //SubPermissions
		return Loader.commands.getString(name+".permission."+subcommand);
	}
	
	//Help messages
	public void help(User user) { //Default help
		if(user.isConsole())
			Messages.msgConfig(Bukkit.getConsoleSender(), name+".help.default", Loader.commands, null);
		else
			Messages.msgConfig(user.player, name+".help.default", Loader.commands, null);
	}
	public void help(User user, String subcommand) { //SubCommand message
		if(user.isConsole())
			Messages.msgConfig(Bukkit.getConsoleSender(), name+".help."+subcommand, Loader.commands, null);
		else
			Messages.msgConfig(user.player, name+".help."+subcommand, Loader.commands, null);
	}
	
	//Messages
	public void msg(User user, String path, Placeholder placeholders) {
		Messages.message(user.player, path, placeholders);
	}
}
