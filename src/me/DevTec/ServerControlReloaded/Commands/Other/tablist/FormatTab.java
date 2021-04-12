package me.DevTec.ServerControlReloaded.Commands.Other.tablist;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.CommandSender;

public class FormatTab {

	public FormatTab(CommandSender s, String[] args) {
		String type = args[1];
		if(!(type.equalsIgnoreCase("player")||type.equalsIgnoreCase("group"))) {
			Loader.advancedHelp(s, "TabList", "Other", "Format");
			return;
		}
		String name = args[2];
		if(args[3].equalsIgnoreCase("set")) {
			if(args.length==4) {
				Loader.advancedHelp(s, "TabList", "Other", "Format");
				return;
			}
			if(type.equalsIgnoreCase("player")) {
				String val = StringUtils.buildString(4, args);
				if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
				TabList.setNameFormat(name, 0, val);
				TabList.update();
				Loader.sendMessages(s, "TabList.Set.Format.Player", 
						Placeholder.c().add("%value%", val).replace("%name%", name));
				return;
			}
			String val = StringUtils.buildString(4, args);
			if(val.startsWith("\"") && val.endsWith("\""))val=val.substring(1, val.length()-1);
			TabList.setNameFormat(name, 2, val);
			TabList.update();
			Loader.sendMessages(s, "TabList.Set.Format.Group", 
					Placeholder.c().add("%value%", val).replace("%name%", name));
			return;
		}
		if(args[3].equalsIgnoreCase("get")) {
			if(type.equalsIgnoreCase("player")) {
				Loader.sendMessages(s, "TabList.Get.Format.Player", 
						Placeholder.c().add("%value%", TabList.getNameFormat(name, 0)).replace("%name%", name));
				return;
			}
			Loader.sendMessages(s, "TabList.Get.Format.Group", 
					Placeholder.c().add("%value%", TabList.getNameFormat(name, 2)).replace("%name%", name));
			return;
		}
		Loader.advancedHelp(s, "TabList", "Other", "Format");
	}

}
