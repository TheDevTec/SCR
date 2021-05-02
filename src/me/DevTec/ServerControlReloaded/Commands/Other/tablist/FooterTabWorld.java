package me.DevTec.ServerControlReloaded.Commands.Other.tablist;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FooterTabWorld {

	public FooterTabWorld(CommandSender s, String[] args) {
		if(args[2].equalsIgnoreCase("add")) {
			List<String> lines = TabList.getHeader(args[1], 1);
			String val = StringUtils.buildString(3, args);
			lines.add(val);
			TabList.setHeader(args[1], 1, lines);
			Loader.sendMessages(s, "TabList.Add.Footer.World.Info", Placeholder.c().add("%value%", val));
			return;
		}
		if(args[2].equalsIgnoreCase("remove")) {
			List<String> lines = TabList.getHeader(args[1], 1);
			int set = StringUtils.getInt(args[3]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = lines.remove(set);
			TabList.setHeader(null, 1, lines);
			Loader.sendMessages(s, "TabList.Remove.Footer.World.Info", Placeholder.c().add("%position%", set+"").add("%value%", val+""));
			return;
		}
		if(args[2].equalsIgnoreCase("set")) {
			List<String> lines = TabList.getHeader(args[1], 1);
			int set = StringUtils.getInt(args[3]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = StringUtils.buildString(4, args);
			lines.set(set, val);
			TabList.setHeader(args[1], 1, lines);
			Loader.sendMessages(s, "TabList.Set.Footer.World.Info");
			return;
		}
		if(args[2].equalsIgnoreCase("list")) {
			Loader.sendMessages(s, "TabList.Get.Footer.World.Info");
			int id = 0;
			for(String ssd : Loader.tab.getStringList("PerWorld."+args[1]+".Footer"))
				Loader.sendMessages(s, "TabList.Get.Footer.World.List", Placeholder.c().replace("%line%", (id++)+"").replace("%value%",ssd));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "FooterWorld");
	}
}
