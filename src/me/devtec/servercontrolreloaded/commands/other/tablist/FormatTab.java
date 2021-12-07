package me.devtec.servercontrolreloaded.commands.other.tablist;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.TabList.FormatType;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class FormatTab {

	public FormatTab(CommandSender s, String[] args) {
		String type = args[1];
		if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
			Loader.advancedHelp(s, "Tablist", "Other", "Format");
			return;
		}
		String name = args[2];
		if(args[3].equalsIgnoreCase("set")) {
			if(args.length==4) {
				Loader.advancedHelp(s, "Tablist", "Other", "Format");
				return;
			}
			if(type.equalsIgnoreCase("player")) {
				String val = StringUtils.buildString(4, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setNameFormat(name, FormatType.GROUP, val);
				TabList.update();
				Loader.sendMessages(s, "TabList.Set.Format.Player", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return;
			}
			String val = StringUtils.buildString(4, args);
			if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
			TabList.setNameFormat(name, FormatType.PER_PLAYER, val);
			TabList.update();
			Loader.sendMessages(s, "TabList.Set.Format.Group", 
					Placeholder.c().add("%value%", val).replace("%name%", name));
			return;
		}
		if(args[3].equalsIgnoreCase("get")) {
			if(type.equalsIgnoreCase("player")) {
				Loader.sendMessages(s, "TabList.Get.Format.Player", 
						Placeholder.c().add("%value%", TabList.getNameFormat(name, FormatType.GROUP)).replace("%name%", name));
				return;
			}
			Loader.sendMessages(s, "TabList.Get.Format.Group", 
					Placeholder.c().add("%value%", TabList.getNameFormat(name, FormatType.PER_PLAYER)).replace("%name%", name));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "Format");
	}

}
