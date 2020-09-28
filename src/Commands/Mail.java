package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;

public class Mail implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String label, String[] args) {

		if (args.length == 0) {
			if (s.hasPermission("ServerControl.Mail.Send"))
				TheAPI.msg("/Mail Send <player> <text>", s);
			if (s.hasPermission("ServerControl.Mail.Read")) {
				TheAPI.msg("/Mail Clear", s);
				TheAPI.msg("/Mail Read", s);
			}
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("Send") && API.hasPerm(s, "ServerControl.Mail.Send")) {
				TheAPI.msg("/Mail Send <player> <text>", s);
				return true;
			}
			if (args[0].equalsIgnoreCase("Read") && API.hasPerm(s, "ServerControl.Mail.Read")) {
				if (getMails(s.getName()).isEmpty()) {
					TheAPI.sendActionBar((Player) s, Loader.s("Mail.Empty"));
					return true;
				}
				for (String mail : getMails(s.getName())) {
					TheAPI.msg(mail, s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("Clear") && API.hasPerm(s, "ServerControl.Mail.Read")) {
				if (getMails(s.getName()).isEmpty()) {
					TheAPI.sendActionBar((Player) s,Loader.s("Mail.Empty"));
					return true;
				}
				removeALL(s);
				TheAPI.sendActionBar((Player) s, Loader.s("Mail.Clear"));
				return true;
			}

		}
		if (args.length > 2 && args[0].equalsIgnoreCase("Send") && API.hasPerm(s, "ServerControl.Mail.Send")) {
			if (!TheAPI.existsUser(args[1])) {
				TheAPI.msg(Loader.PlayerNotEx(args[1]), s);
				return true;
			}
			String msg = TheAPI.buildString(args).replaceFirst(args[0]+" "+args[1]+" ", "");
			add(s, "&8" + s.getName() + ": &8" + msg, args[1]);
			TheAPI.sendActionBar((Player) s, Loader.s("Mails.Sent").replace("%player%", args[1]));
			Player p = TheAPI.getPlayerOrNull(args[1]);
			if (p != null) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Mail.Notification").replace("%number%", "" +  getMails(p.getName()).size()), p);
			}
			return true;
		}
		return true;
	}

	public static void add(CommandSender s, String message, String p) {
		List<String> a = getMails(p);
		a.add(message);
		TheAPI.getUser(s.getName()).setAndSave("Mails", a);
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
		if (s.hasPermission("ServerControl.Mail.Read")) { // pokud m» permise ...
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
