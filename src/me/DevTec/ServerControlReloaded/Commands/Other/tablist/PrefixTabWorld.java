package me.DevTec.ServerControlReloaded.Commands.Other.tablist;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class PrefixTabWorld {
	
	public PrefixTabWorld(CommandSender s, String[] args) {
		String name = args[1];
		String ttype = args[2];
		if(!(ttype.equalsIgnoreCase("nametag")||ttype.equalsIgnoreCase("tablist")||ttype.equalsIgnoreCase("tab"))) {
			Loader.advancedHelp(s, "TabList", "Other", "PrefixWorld");
			return;
		}
		if(args[3].equalsIgnoreCase("set")) {
			if(args.length==4) {
				Loader.advancedHelp(s, "TabList", "Other", "PrefixWorld");
				return;
			}
			String val = StringUtils.buildString(4, args);
			if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
			TabList.setPrefix(name, false, 1, val);
			Loader.sendMessages(s, "TabList.Set."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.World", 
					Placeholder.c().add("%value%", val).replace("%name%", name));
			return;
		}
		if(args[3].equalsIgnoreCase("get")) {
			Loader.sendMessages(s, "TabList.Get."+(ttype.equalsIgnoreCase("nametag")?"NameTag":"TabList")+".Prefix.World", 
					Placeholder.c().add("%value%", TabList.getPrefix(name, ttype.equalsIgnoreCase("nametag"), 2)).replace("%name%", name));
			return;
		}
		Loader.advancedHelp(s, "TabList", "Other", "PrefixWorld");
	}
}
