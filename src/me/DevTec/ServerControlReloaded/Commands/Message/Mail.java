package me.DevTec.ServerControlReloaded.Commands.Message;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class Mail implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {
		if(!Loader.has(s, "Mail", "Message")) {
			Loader.noPerms(s, "Mail", "Message");
			return true;
		}
		if (args.length == 0) {
			Loader.Help(s, "Mail", "Message");
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("Send") && Loader.has(s, "Mail", "Message", "Send")) {
				Loader.Help(s, "Mail", "Message");
				return true;
			}
			if (args[0].equalsIgnoreCase("Read") && Loader.has(s, "Mail", "Message", "Read")) {
				if (getMails(s.getName()).isEmpty()) {
					TheAPI.sendActionBar((Player) s, Loader.getTranslation("Mail.Empty").toString()
							.replace("%prefix%", Loader.getTranslation("prefix")+""));
					return true;
				}
				for (String mail : getMails(s.getName()))
					TheAPI.msg(mail, s);
				return true;
			}
			if (args[0].equalsIgnoreCase("Clear") && Loader.has(s, "Mail", "Message", "Read")) {
				if (getMails(s.getName()).isEmpty()) {
					TheAPI.sendActionBar((Player) s, Loader.getTranslation("Mail.Empty").toString()
							.replace("%prefix%", Loader.getTranslation("prefix")+""));
					return true;
				}
				removeALL(s);
				TheAPI.sendActionBar((Player) s, Loader.getTranslation("Mail.Clear").toString()
						.replace("%prefix%", Loader.getTranslation("prefix")+""));
				return true;
			}

		}
		if (args.length > 2 && args[0].equalsIgnoreCase("Send") && Loader.has(s, "Mail", "Message", "Send")) {
			if (!TheAPI.existsUser(args[1])) {
				Loader.notExist(s, args[1]);
				return true;
			}
			String msg = StringUtils.buildString(2, args);
			add(Loader.config.getString("Format.Mail").replace("%player%", s.getName())
					.replace("%playername%", s instanceof Player ? ((Player)s).getDisplayName() : s.getName())
					.replace("%customname%", s instanceof Player ? ((Player)s).getCustomName() : s.getName())
					.replace("%message%", msg), args[1]);
			TheAPI.sendActionBar((Player) s, Loader.getTranslation("Mail.Sent").toString()
					.replace("%player%", args[1])
					.replace("%prefix%", Loader.getTranslation("prefix")+""));
			Player p = TheAPI.getPlayerOrNull(args[1]);
			if (p != null) {
				Loader.sendMessages(p, "Mail.Received", Placeholder.c().add("%amount%", "" + getMails(p.getName()).size()));
			}
			return true;
		}
		return true;
	}

	public static void add(String message, String p) {
		List<String> a = getMails(p);
		a.add(message);
		TheAPI.getUser(p).setAndSave("Mails", a);
	}

	public static List<String> getMails(String p) {
		return TheAPI.getUser(p).getStringList("Mails");
	}

	public static void removeALL(CommandSender p) {
		TheAPI.getUser(p.getName()).setAndSave("Mails", null);
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (s.hasPermission("ServerControl.Mail.Read")) {
			if (args.length == 1) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Read", "Clear"), new ArrayList<>()));
			}
		}
		if (s.hasPermission("ServerControl.Mail.Send")) {
			if (args.length == 1) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Send"), new ArrayList<>()));
			}
			if (args[0].equalsIgnoreCase("Send") && args.length == 2)
				return null;
			if (args.length >= 3)
				return Arrays.asList("?");
		}
		return c;
	}
}
