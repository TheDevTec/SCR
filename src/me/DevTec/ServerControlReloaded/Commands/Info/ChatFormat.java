package me.DevTec.ServerControlReloaded.Commands.Info;


import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.apis.PluginManagerAPI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFormat implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
	if (Loader.has(s, "ChatFormat", "Info")) {
		if (args.length == 0) {
			Loader.Help(s, "ChatFormat", "Info");
			/*if (Loader.has(s, "ChatFormat.List", "Info"))
				msg("&6/ChatFormat List &7- &5List of groups", s);
			if (Loader.has(s, "ChatFormat.Info", "Info"))
				msg("&6/ChatFormat Info <group> &7- &5Informations about group", s);
			if (Loader.has(s, "ChatFormat.Create", "Info"))
				msg("&6/ChatFormat Create <group> &7- &5Create new group", s);
			if (Loader.has(s, "ChatFormat.Delete", "Info"))
				msg("&6/ChatFormat Delete <group> &7- &5Delete group", s);
			if (Loader.has(s, "ChatFormat.Set", "Info"))
				msg("&6/ChatFormat Set <group> <chat/name> <value> &7- &5Set name/chat format of group", s);*/
			return true;
		}
		if (args[0].equalsIgnoreCase("create")) {
			if (Loader.has(s, "ChatFormat", "Info")) {
				if (args.length == 1) {
					Loader.advancedHelp(s, "ChatFormat", "Info", "Create");
					//msg("&6/ChatFormat Create <group> &7- &5Create new group", s);
					return true;
				}
				String g = args[1];
				if (Loader.config.exists("Chat-Groups." + g)) {
					Loader.sendMessages(s, "ChatFormat.Group.Exists", Placeholder.c().add("%group%", g));
					return true;
				}
				Loader.config.set("Chat-Groups." + g+ ".Name", "%player%");
				Loader.config.set("Chat-Groups." + g+ ".Chat", "<%playername%> %message%");
				Loader.config.save();
				Loader.sendMessages(s, "ChatFormat.Group.Create", Placeholder.c().add("%group%", g));
				return true;
			}
			Loader.noPerms(s, "ChatFormat.Create", "Info");
			return true;
		}

		if (args[0].equalsIgnoreCase("delete")) {
			if (Loader.has(s, "ChatFormat.Delete", "Info")) {
				if (args.length == 1) {
					Loader.advancedHelp(s, "ChatFormat", "Info", "Delete");
					return true;
				}
				String g = args[1];
				if (!Loader.config.exists("Chat-Groups." + g)) {
					Loader.sendMessages(s, "ChatFormat.Group.NotExist", Placeholder.c().add("%group%", g));
					return true;
				}
				Loader.config.remove("Chat-Groups." + g);
				Loader.config.save();
				Loader.sendMessages(s, "ChatFormat.Group.Delete", Placeholder.c().add("%group%", g));
				return true;
			}
			Loader.noPerms(s, "ChatFormat.Delete", "Info");
			return true;
		}

		if (args[0].equalsIgnoreCase("set")) {
			if (Loader.has(s, "ChatFormat.Set", "Info")) {
				if (args.length == 1 || args.length == 2 || args.length == 3) {
					Loader.advancedHelp(s, "ChatFormat", "Info", "Set");
					return true;
				}

				String g = args[1];
				String e = args[2];
				if (!e.equalsIgnoreCase("chat") && !e.equalsIgnoreCase("name")) {
					Loader.advancedHelp(s, "ChatFormat", "Info", "Set");
					return true;
				}
				if (!Loader.config.exists("Chat-Groups." + g)) {
					Loader.sendMessages(s, "ChatFormat.Group.NotExist", Placeholder.c().add("%group%", g));
					return true;
				}
				String msg = StringUtils.buildString(3, args);
				String f = "Chat";
				if (e.equalsIgnoreCase("name"))
					f = "Name";

				Loader.config.set("Chat-Groups." + g + "." + f, msg);
				Loader.config.save();
				Loader.sendMessages(s, "ChatFormat.Edit."+f, Placeholder.c().add("%group%", g).add("%value%", msg));
				return true;

			}
			Loader.noPerms(s, "ChatFormat.Set", "Info");
			return true;
		}
		if (args[0].equalsIgnoreCase("list")) {
			if (Loader.has(s, "ChatFormat.List", "Info")) {
				Loader.sendMessages(s, "ChatFormat.List", Placeholder.c().add("%groups%", StringUtils.join(Loader.config.getKeys("Chat-Groups"), ", ")));
				return true;
			}
			Loader.noPerms(s, "ChatFormat.List", "Info");
			return true;
		}
		if (args[0].equalsIgnoreCase("info")) {
			if (Loader.has(s, "ChatFormat.Info", "Info")) {
				if (args.length == 1) {
					Loader.advancedHelp(s, "ChatFormat", "Info", "Info");
					return true;
				}
				String g = args[1];
				if (Loader.config.getString("Chat-Groups." + g) == null) {
					Loader.sendMessages(s, "ChatFormat.Group.NotExist", Placeholder.c().add("%group%", g));
					return true;
				}
				Loader.sendMessages(s, "ChatFormat.Info", Placeholder.c()
						.add("%group%", g)
						.add("%name%", ""+Loader.config.getString("Chat-Groups." + g + ".Name"))
						.add("%chat%", ""+Loader.config.getString("Chat-Groups." + g + ".Chat")));
				return true;
			}
			Loader.noPerms(s, "ChatFormat.Info", "Info");
			return true;
		}

		return false;
	}
	return true;
}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 1) {
			List<String> c = new ArrayList<>();
			if (s.hasPermission("ServerControl.ChatFormat.List"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("List")));
			if (s.hasPermission("ServerControl.ChatFormat.Info"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Info")));
			if (s.hasPermission("ServerControl.ChatFormat.Create"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Create")));
			if (s.hasPermission("ServerControl.ChatFormat.Delete"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Delete")));
			if (s.hasPermission("ServerControl.ChatFormat.Set"))
				c.addAll(StringUtils.copyPartialMatches(args[0], Arrays.asList("Set")));
			return c;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("info") && s.hasPermission("ServerControl.ChatFormat.Info")
					|| args[0].equalsIgnoreCase("delete") && s.hasPermission("ServerControl.ChatFormat.Delete")
					|| args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				return StringUtils.copyPartialMatches(args[1],
						Loader.config.getKeys("Chat-Groups"));

			if (args[0].equalsIgnoreCase("create") && s.hasPermission("ServerControl.ChatFormat.Create")) {
				if (Loader.config.getString("Chat-Groups") != null)
					for (String a : Loader.config.getKeys("Chat-Groups"))
						if (args[1].equals(a))
							return StringUtils.copyPartialMatches(args[1], Arrays.asList("AlreadyExists"));
						else {
							List<String> list = new ArrayList<String>();
							if (Loader.vault != null && PluginManagerAPI.getPlugin("Vault") != null) {
								for (String d : Loader.vault.getGroups()) {
									list.add(d);
								}
							} else
								list = Arrays.asList("?");
							list.removeAll(Loader.config.getKeys("Chat-Groups"));
							return StringUtils.copyPartialMatches(args[1], list);
						}
				return StringUtils.copyPartialMatches(args[1], Arrays.asList("?"));
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				return StringUtils.copyPartialMatches(args[2], Arrays.asList("Chat", "Name"));
		}
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				if (args[2].equalsIgnoreCase("Chat") || args[2].equalsIgnoreCase("Name"))
					return StringUtils.copyPartialMatches(args[3], Arrays.asList("?"));
		}
		return Arrays.asList();
	}

}