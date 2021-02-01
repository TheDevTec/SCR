package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.devtec.theapi.utils.StringUtils;

public class Tab implements CommandExecutor, TabCompleter {

	//tablist prefix <player/group> [name] <tablist/nametag> get
	//tablist prefix <player/group> [name] <tablist/nametag> set [value]

	//tablist suffix <player/group> [name] <tablist/nametag> get
	//tablist suffix <player/group> [name] <tablist/nametag> set [value]

	//tablist format <player/group> [name] get
	//tablist format <player/group> [name] set [value]

	//tablist prefixWorld <player/group> [name] [world] <tablist/nametag> get
	//tablist prefixWorld <player/group> [name] [world] <tablist/nametag> set [value]

	//tablist suffixWorld <player/group> [name] [world] <tablist/nametag> get
	//tablist suffixWorld <player/group> [name] [world] <tablist/nametag> set [value]

	//tablist formatWorld <player/group> [name] [world] get
	//tablist formatWorld <player/group> [name] [world] set [value]
	
	//tablist header <group/player/global> [name] add [value]
	//tablist header <group/player/global> [name] remove [line]
	//tablist header <group/player/global> [name] set [line] [value]
	//tablist header <group/player/global> [name] list
	
	//tablist footer <group/player/global> [name] add [value]
	//tablist footer <group/player/global> [name] remove [line]
	//tablist footer <group/player/global> [name] set [line] [value]
	//tablist footer <group/player/global> [name] list
	
	//tablist headerWorld [name] [world] add [value]
	//tablist headerWorld [name] [world] remove [line]
	//tablist headerWorld [name] [world] set [line] [value]
	//tablist headerWorld [name] [world] list
	
	//tablist footerWorld [name] [world] add [value]
	//tablist footerWorld [name] [world] remove [line]
	//tablist footerWorld [name] [world] set [line] [value]
	//tablist footerWorld [name] [world] list
	private void help(CommandSender s, int type) {
		switch(type) {
		case -1:
			for(int i = 0; i < 10; ++i)
				help(s, i);
			break;
		case 0:
			Loader.advancedHelp(s, "TabList", "Other", "Prefix");
			break;
		case 1:
			Loader.advancedHelp(s, "TabList", "Other", "Suffix");
			break;
		case 2:
			Loader.advancedHelp(s, "TabList", "Other", "Format");
			break;
		case 3:
			Loader.advancedHelp(s, "TabList", "Other", "PrefixWorld");
			break;
		case 4:
			Loader.advancedHelp(s, "TabList", "Other", "SuffixWorld");
			break;
		case 5:
			Loader.advancedHelp(s, "TabList", "Other", "FormatWorld");
			break;
		case 6:
			Loader.advancedHelp(s, "TabList", "Other", "Header");
			break;
		case 7:
			Loader.advancedHelp(s, "TabList", "Other", "Footer");
			break;
		case 8:
			Loader.advancedHelp(s, "TabList", "Other", "HeaderWorld");
			break;
		case 9:
			Loader.advancedHelp(s, "TabList", "Other", "FooterWorld");
			break;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String awd, String[] args) {
		if(args.length==0) {
			help(s, -1);
			return true;
		}
		if(args[0].equalsIgnoreCase("prefix")) {
			if(args.length<5) {
				help(s, 0);
				return true;
			}
			String type = args[1];
			if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
				help(s, 0);
				return true;
			}
			String name = args[2];
			String ttype = args[3];
			if(!(ttype.equalsIgnoreCase("nametag")||ttype.equalsIgnoreCase("tablist")||ttype.equalsIgnoreCase("tab"))) {
				help(s, 0);
				return true;
			}
			if(args[4].equalsIgnoreCase("set")) {
				if(args.length==5) {
					help(s, 0);
					return true;
				}
				if(type.equalsIgnoreCase("player")) {
					String val = StringUtils.buildString(5, args);
					if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
					TabList.setPrefix(name, ttype.equalsIgnoreCase("nametag"), 0, val);
					Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.Player", 
							Placeholder.c().add("%value%", val).replace("%name%", name));
					return true;
				}
				String val = StringUtils.buildString(5, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setPrefix(name, ttype.equalsIgnoreCase("nametag"), 2, val);
				Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.Group", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return true;
			}
			if(args[4].equalsIgnoreCase("get")) {
				if(type.equalsIgnoreCase("player")) {
					Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.Player", 
							Placeholder.c().add("%value%", TabList.getPrefix(name, ttype.equalsIgnoreCase("nametag"), 0)).replace("%name%", name));
					return true;
				}
				Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.Group", 
						Placeholder.c().add("%value%", TabList.getPrefix(name, ttype.equalsIgnoreCase("nametag"), 2)).replace("%name%", name));
				return true;
			}
			help(s, 0);
			return true;
		}
		if(args[0].equalsIgnoreCase("format")) {
			if(args.length<5) {
				help(s, 2);
				return true;
			}
			String type = args[1];
			if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
				help(s, 2);
				return true;
			}
			String name = args[2];
			if(args[3].equalsIgnoreCase("set")) {
				if(args.length==4) {
					help(s, 2);
					return true;
				}
				if(type.equalsIgnoreCase("player")) {
					String val = StringUtils.buildString(4, args);
					if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
					TabList.setNameFormat(name, 0, val);
					Loader.sendMessages(s, "TabList.Set.Format.Player", 
							Placeholder.c().add("%value%", val).replace("%name%", name));
					return true;
				}
				String val = StringUtils.buildString(4, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setNameFormat(name, 2, val);
				Loader.sendMessages(s, "TabList.Set.Format.Group", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return true;
			}
			if(args[3].equalsIgnoreCase("get")) {
				if(type.equalsIgnoreCase("player")) {
					Loader.sendMessages(s, "TabList.Get.Format.Player", 
							Placeholder.c().add("%value%", TabList.getNameFormat(name, 0)).replace("%name%", name));
					return true;
				}
				Loader.sendMessages(s, "TabList.Get.Format.Group", 
						Placeholder.c().add("%value%", TabList.getNameFormat(name, 2)).replace("%name%", name));
				return true;
			}
			help(s, 2);
			return true;
		}
		if(args[0].equalsIgnoreCase("suffix")) {
			if(args.length<5) {
				help(s, 1);
				return true;
			}
			String type = args[1];
			if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
				help(s, 1);
				return true;
			}
			String name = args[2];
			String ttype = args[3];
			if(!(ttype.equalsIgnoreCase("nametag")||ttype.equalsIgnoreCase("tablist")||ttype.equalsIgnoreCase("tab"))) {
				help(s, 1);
				return true;
			}
			if(args[4].equalsIgnoreCase("set")) {
				if(args.length==5) {
					help(s, 1);
					return true;
				}
				if(type.equalsIgnoreCase("player")) {
					String val = StringUtils.buildString(5, args);
					if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
					TabList.setSuffix(name, ttype.equalsIgnoreCase("nametag"), 0, val);
					Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Suffix.Player", 
							Placeholder.c().add("%value%", val).replace("%name%", name));
					return true;
				}
				String val = StringUtils.buildString(5, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setSuffix(name, ttype.equalsIgnoreCase("nametag"), 2, val);
				Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Suffix.Group", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return true;
			}
			if(args[4].equalsIgnoreCase("get")) {
				if(type.equalsIgnoreCase("player")) {
					Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Suffix.Player", 
							Placeholder.c().add("%value%", TabList.getSuffix(name, ttype.equalsIgnoreCase("nametag"), 0)).replace("%name%", name));
					return true;
				}
				Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Suffix.Group", 
						Placeholder.c().add("%value%", TabList.getSuffix(name, ttype.equalsIgnoreCase("nametag"), 2)).replace("%name%", name));
				return true;
			}
			help(s, 1);
		}
		if(args[0].equalsIgnoreCase("header")) {
			if(args.length<3) {
				help(s,6);
				return true;
			}
			if(args[3].equalsIgnoreCase("add")) {
				
			}
			if(args[3].equalsIgnoreCase("remove")) {
				
			}
			if(args[3].equalsIgnoreCase("set")) {
				
			}
			if(args[3].equalsIgnoreCase("list")) {
				//Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Suffix.Group", 
				//		Placeholder.c().add("%value%", TabList.getSuffix(name, ttype.equalsIgnoreCase("nametag"), 2)).replace("%name%", name));
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("footer")) {
			if(args.length<4) {
				help(s,7);
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		//must be rewrited
		return c;
	}
}
