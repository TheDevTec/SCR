package me.devtec.servercontrolreloaded.commands.other.tablist;

import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.TabList.FormatType;
import me.devtec.theapi.utils.StringUtils;

public class FormatTabWorld {

	public FormatTabWorld(CommandSender s, String[] args) {
		String name = args[1];
		String ttype = args[2];
		if(!(ttype.equalsIgnoreCase("nametag")||ttype.equalsIgnoreCase("tablist")||ttype.equalsIgnoreCase("tab"))) {
			Loader.advancedHelp(s, "Tablist", "Other", "SuffixWorld");
			return;
		}
		if(args[3].equalsIgnoreCase("set")) {
			if(args.length==4) {
				Loader.advancedHelp(s, "Tablist", "Other", "SuffixWorld");
				return;
			}
			String val = StringUtils.buildString(4, args);
			if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
			TabList.setPrefix(name, false, FormatType.PER_WORLD, val);
			Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.World", 
					Placeholder.c().add("%value%", val).replace("%name%", name));
			return;
		}
		if(args[3].equalsIgnoreCase("get")) {
			Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"Tablist")+".Suffix.World", 
					Placeholder.c().add("%value%", TabList.getPrefix(name, ttype.equalsIgnoreCase("nametag"), FormatType.PER_WORLD)).replace("%name%", name));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "SuffixWorld");
	}
}
