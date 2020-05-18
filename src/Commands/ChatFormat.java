package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;

public class ChatFormat implements CommandExecutor, TabCompleter {

	private void msg(String s, CommandSender a) {
		Loader.msg(s, a);
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s.hasPermission("ServerControl.ChatFormat.List"))
				msg("&6/ChatFormat List &7- &5List of groups", s);
			if (s.hasPermission("ServerControl.ChatFormat.Info"))
				msg("&6/ChatFormat Info <group> &7- &5Informations about group", s);
			if (s.hasPermission("ServerControl.ChatFormat.Create"))
				msg("&6/ChatFormat Create <group> &7- &5Create new group", s);
			if (s.hasPermission("ServerControl.ChatFormat.Delete"))
				msg("&6/ChatFormat Delete <group> &7- &5Delete group", s);
			if (s.hasPermission("ServerControl.ChatFormat.Set"))
				msg("&6/ChatFormat Set <group> <chat/name> <value> &7- &5Set name/chat format of group", s);
			return true;
		}
		if (args[0].equalsIgnoreCase("create")) {
			if (API.hasPerm(s, "ServerControl.ChatFormat.Create")) {
				if (args.length == 1) {
					msg("&6/ChatFormat Create <group> &7- &5Create new group", s);
					return true;
				}
				String g = args[1];
				if (Loader.config.getString("Chat-Groups." + g) != null) {

					msg("&6Group with name &c'" + g + "' &6already exist", s);
					return true;
				}
				Loader.config.set("Chat-Groups." + g, "");
				msg("&aGroup with name &2'" + g + "' &acreated", s);
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("delete")) {
			if (API.hasPerm(s, "ServerControl.ChatFormat.Delete")) {
				if (args.length == 1) {
					msg("&6/ChatFormat Delete <group> &7- &5Delete group", s);
					return true;
				}
				String g = args[1];
				if (Loader.config.getString("Chat-Groups." + g) == null) {

					msg("&cGroup with name &4'" + g + "' &cdoesn't exist", s);
					return true;
				}
				Loader.config.set("Chat-Groups." + g, null);
				msg("&6Group with name &c'" + g + "' &6deleted", s);
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("set")) {
			if (API.hasPerm(s, "ServerControl.ChatFormat.Set")) {
				if (args.length == 1 || args.length == 2 || args.length == 3) {
					msg("&6/ChatFormat Set <group> <chat/name> <value> &7- &5Set name/chat format of group", s);
					return true;
				}

				String g = args[1];
				String e = args[2];
				if (!e.equalsIgnoreCase("chat") && !e.equalsIgnoreCase("name")) {
					msg("&6/ChatFormat Set <group> <chat/name> <value> &7- &5Set name/chat format of group", s);
					return true;
				}
				if (Loader.config.getString("Chat-Groups." + g) == null) {
					msg("&cGroup with name &4'" + g + "' &cdoesn't exist", s);
					return true;
				}
				String msg = "";
				for (int i = 3; i < args.length; ++i) {
					msg = msg + " " + args[i];
				}

				msg = msg.substring(1, msg.length());
				String what = "chat";
				if (e.equalsIgnoreCase("name"))
					what = "name";
				String f = "Chat";
				if (what.equals("name"))
					f = "Name";

				Loader.config.set("Chat-Groups." + g + "." + f, msg);
				msg("&6Set " + what + " format to &c'" + msg + "&c' &6on group &c" + g, s);
				return true;

			}
			return true;
		}
		if (args[0].equalsIgnoreCase("list")) {
			if (API.hasPerm(s, "ServerControl.ChatFormat.List")) {
				msg("&9---< &bList of groups &9>----", s);
				for (String d : Loader.config.getConfigurationSection("Chat-Groups").getKeys(false))
					msg("&9- &b" + d, s);
				return true;
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("info")) {
			if (API.hasPerm(s, "ServerControl.ChatFormat.Info")) {
				if (args.length == 1) {
					msg("&6/ChatFormat Info <group> &7- &5Informations about group", s);
					return true;
				}
				String g = args[1];
				if (Loader.config.getString("Chat-Groups." + g) == null) {
					msg("&cGroup with name '" + g + "' doesn't exist", s);
					return true;
				}
				msg("&9---< &bInformation about group &c" + g + " &9>----", s);
				msg("&9Name: " + Loader.config.getString("Chat-Groups." + g + ".Name"), s);
				msg("&9Chat: " + Loader.config.getString("Chat-Groups." + g + ".Chat"), s);
				return true;
			}
			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {
			if (s.hasPermission("ServerControl.ChatFormat.List"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("List"), new ArrayList<>()));
			if (s.hasPermission("ServerControl.ChatFormat.Info"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Info"), new ArrayList<>()));
			if (s.hasPermission("ServerControl.ChatFormat.Create"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Create"), new ArrayList<>()));
			if (s.hasPermission("ServerControl.ChatFormat.Delete"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Delete"), new ArrayList<>()));
			if (s.hasPermission("ServerControl.ChatFormat.Set"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Set"), new ArrayList<>()));
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("info") && s.hasPermission("ServerControl.ChatFormat.Info")
					|| args[0].equalsIgnoreCase("delete") && s.hasPermission("ServerControl.ChatFormat.Delete")
					|| args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				c.addAll(StringUtil.copyPartialMatches(args[1],
						Loader.config.getConfigurationSection("Chat-Groups").getKeys(false), new ArrayList<>()));

			if (args[0].equalsIgnoreCase("create") && s.hasPermission("ServerControl.ChatFormat.Create")) {
				if (Loader.config.getString("Chat-Groups") != null)
					for (String a : Loader.config.getConfigurationSection("Chat-Groups").getKeys(false))
						if (args[1].equals(a))
							c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("AlreadyExists"),
									new ArrayList<>()));
						else {
							List<String> list = new ArrayList<String>();
							if (Loader.vault != null && API.existVaultPlugin()) {
								for (String d : Loader.vault.getGroups()) {
									list.add(d);
								}
							} else
								list = Arrays.asList("?");
							list.removeAll(Loader.config.getConfigurationSection("Chat-Groups").getKeys(false));
							c.addAll(StringUtil.copyPartialMatches(args[1], list, new ArrayList<>()));
						}
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
			}
		}
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				c.addAll(StringUtil.copyPartialMatches(args[2], Arrays.asList("Chat", "Name"), new ArrayList<>()));
		}
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set") && s.hasPermission("ServerControl.ChatFormat.Set"))
				if (args[2].equalsIgnoreCase("Chat") || args[2].equalsIgnoreCase("Name"))
					c.addAll(StringUtil.copyPartialMatches(args[3], Arrays.asList("?"), new ArrayList<>()));
		}
		return c;
	}

}
