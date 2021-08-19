package me.devtec.servercontrolreloaded.commands.other.tablist;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Tab implements CommandExecutor, TabCompleter {
	//tablist prefix <player/group> [name] <tablist/nametag> get
	//tablist prefix <player/group> [name] <tablist/nametag> set [value]

	//tablist suffix <player/group> [name] <tablist/nametag> get
	//tablist suffix <player/group> [name] <tablist/nametag> set [value]

	//tablist format <player/group> [name] get
	//tablist format <player/group> [name] set [value]
	
	//tablist header <group/player/global> [name] add [value]
	//tablist header <group/player/global> [name] remove [line]
	//tablist header <group/player/global> [name] set [line] [value]
	//tablist header <group/player/global> [name] list
	
	//tablist footer <group/player/global> [name] add [value]
	//tablist footer <group/player/global> [name] remove [line]
	//tablist footer <group/player/global> [name] set [line] [value]
	//tablist footer <group/player/global> [name] list
	
	//tablist prefixWorld [world] <tablist/nametag> get
	//tablist prefixWorld [world] <tablist/nametag> set [value]

	//tablist suffixWorld [world] <tablist/nametag> get
	//tablist suffixWorld [world] <tablist/nametag> set [value]

	//tablist formatWorld [world] get
	//tablist formatWorld [world] set [value]
	
	//tablist headerWorld [world] add [value]
	//tablist headerWorld [world] remove [line]
	//tablist headerWorld [world] set [line] [value]
	//tablist headerWorld [world] list
	
	//tablist footerWorld [world] add [value]
	//tablist footerWorld [world] remove [line]
	//tablist footerWorld [world] set [line] [value]
	//tablist footerWorld [world] list
	private void help(CommandSender s, int type) {
		switch(type) {
		case -1:
			for(int i = 0; i < 10; ++i)
				help(s, i);
			break;
		case 0:
			Loader.advancedHelp(s, "Tablist", "Other", "Prefix");
			break;
		case 1:
			Loader.advancedHelp(s, "Tablist", "Other", "Suffix");
			break;
		case 2:
			Loader.advancedHelp(s, "Tablist", "Other", "Format");
			break;
		case 3:
			Loader.advancedHelp(s, "Tablist", "Other", "PrefixWorld");
			break;
		case 4:
			Loader.advancedHelp(s, "Tablist", "Other", "SuffixWorld");
			break;
		case 5:
			Loader.advancedHelp(s, "Tablist", "Other", "FormatWorld");
			break;
		case 6:
			Loader.advancedHelp(s, "Tablist", "Other", "Header");
			break;
		case 7:
			Loader.advancedHelp(s, "Tablist", "Other", "Footer");
			break;
		case 8:
			Loader.advancedHelp(s, "Tablist", "Other", "HeaderWorld");
			break;
		case 9:
			Loader.advancedHelp(s, "Tablist", "Other", "FooterWorld");
			break;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String awd, String[] args) {
		if (Loader.has(s, "Tablist", "Other")) {
		if(!CommandsManager.canUse("Other.Tablist", s)) {
			Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Tablist", s))));
			return true;
		}
		if(args.length==0) {
			help(s, -1);
			return true;
		}
		if(args[0].equalsIgnoreCase("prefix")) {
			if(args.length<5) {
				help(s, 0);
				return true;
			}
			new PrefixTab(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("suffix")) {
			if(args.length<5) {
				help(s, 1);
				return true;
			}
			new SuffixTab(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("format")) {
			if(args.length<5) {
				help(s, 2);
				return true;
			}
			new FormatTab(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("header")) {
			if(args.length<3) {
				help(s,6);
				return true;
			}
			new HeaderTab(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("footer")) {
			if(args.length<3) {
				help(s,7);
				return true;
			}
			new FooterTab(s, args);
			return true;
		}
		
		//PerWorld
		if(args[0].equalsIgnoreCase("prefixWorld")) {
			if(args.length<4) {
				help(s, 3);
				return true;
			}
			new PrefixTabWorld(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("suffixWorld")) {
			if(args.length<4) {
				help(s, 5);
				return true;
			}
			new SuffixTabWorld(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("formatWorld")) {
			if(args.length<5) {
				help(s, 4);
				return true;
			}
			new FormatTabWorld(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("headerWorld")) {
			if(args.length<3) {
				help(s,8);
				return true;
			}
			new HeaderTabWorld(s, args);
			return true;
		}
		if(args[0].equalsIgnoreCase("footerWorld")) {
			if(args.length<3) {
				help(s,9);
				return true;
			}
			new FooterTabWorld(s, args);
			return true;
		}
		help(s, -1);
		return true;
		}
		Loader.noPerms(s, "Tablist", "Other");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		//TODO must be rewrited
		return c;
	}
}
