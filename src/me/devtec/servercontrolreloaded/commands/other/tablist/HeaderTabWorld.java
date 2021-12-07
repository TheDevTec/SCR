package me.devtec.servercontrolreloaded.commands.other.tablist;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.TabList.FormatType;
import me.devtec.theapi.utils.StringUtils;

public class HeaderTabWorld {

	public HeaderTabWorld(CommandSender s, String[] args) {
		if(args[2].equalsIgnoreCase("add")) {
			List<String> lines = TabList.getHeader(args[1], FormatType.PER_PLAYER);
			String val = StringUtils.buildString(3, args);
			lines.add(val);
			TabList.setHeader(args[1], FormatType.PER_PLAYER, lines);
			Loader.sendMessages(s, "TabList.Add.Header.World.Info", Placeholder.c().add("%value%", val));
			return;
		}
		if(args[2].equalsIgnoreCase("remove")) {
			List<String> lines = TabList.getHeader(args[1], FormatType.PER_PLAYER);
			int set = StringUtils.getInt(args[3]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = lines.remove(set);
			TabList.setHeader(null, FormatType.PER_PLAYER, lines);
			Loader.sendMessages(s, "TabList.Remove.Header.World.Info", Placeholder.c().add("%position%", set+"").add("%value%", val+""));
			return;
		}
		if(args[2].equalsIgnoreCase("set")) {
			List<String> lines = TabList.getHeader(args[1], FormatType.PER_PLAYER);
			int set = StringUtils.getInt(args[3]);
			if(lines.size() < set || set<0) {
				Loader.sendMessages(s, "TabList.Error", Placeholder.c().add("%size%", lines.size()+"").add("%value%", set+""));
				return;
			}
			String val = StringUtils.buildString(4, args);
			lines.set(set, val);
			TabList.setHeader(args[1], FormatType.PER_PLAYER, lines);
			Loader.sendMessages(s, "TabList.Set.Header.World.Info");
			return;
		}
		if(args[2].equalsIgnoreCase("list")) {
			Loader.sendMessages(s, "TabList.Get.Header.World.Info");
			int id = 0;
			for(String ssd : Loader.tab.getStringList("PerWorld."+args[1]+".Header"))
				Loader.sendMessages(s, "TabList.Get.Header.World.List", Placeholder.c().replace("%line%", (id++)+"").replace("%value%",ssd));
			return;
		}
		Loader.advancedHelp(s, "Tablist", "Other", "HeaderWorld");
	}
}
