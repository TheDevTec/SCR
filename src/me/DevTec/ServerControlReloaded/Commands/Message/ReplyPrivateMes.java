package me.DevTec.ServerControlReloaded.Commands.Message;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.TheAPI.TheAPI;

public class ReplyPrivateMes implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Reply", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "Reply", "Message");
				return true;
			}
				String name = "";

				if (s instanceof Player == false)
					name = "CONSOLE";
				else
					name = s.getName();
				if (TheAPI.existsUser(name)) {
					String msg = Colors.colorize(TheAPI.buildString(args), false, s);
					String from = "";
					String to = "";
					if (s instanceof Player == false) {
						if (TheAPI.getUser("CONSOLE").getString("Reply").equalsIgnoreCase("CONSOLE")) {
							from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
									.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
							to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
									.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
							to = to.replace("%message%", msg);
							from = from.replace("%message%", msg);
							s.sendMessage(to);
							Bukkit.getConsoleSender().sendMessage(from);
							return true;
						} else {
							Player p = TheAPI.getPlayer(TheAPI.getUser("CONSOLE").getString("Reply"));
							if (p != null) {
								from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
										.replace("%from%", s.getName()).replace("%to%", p.getName()));
								to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
										.replace("%from%", s.getName()).replace("%to%", p.getName()));
								to = to.replace("%message%", msg);
								from = from.replace("%message%", msg);
								TheAPI.getUser(p).setAndSave("Reply", "CONSOLE");
								s.sendMessage(to);
								p.sendMessage(from);
								return true;
							}
							Loader.notOnline(s, TheAPI.getUser(s.getName()).getString("Reply"));
							return true;
						}
					} else {
						if (TheAPI.getUser(s.getName()).getString("Reply").equalsIgnoreCase("CONSOLE")) {
							from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
									.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
							to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
									.replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
							to = to.replace("%message%", msg);
							from = from.replace("%message%", msg);
							TheAPI.getUser("CONSOLE").setAndSave("Reply", s.getName());
							s.sendMessage(to);
							Bukkit.getConsoleSender().sendMessage(from);
							return true;
						}
						Player p = TheAPI.getPlayer(TheAPI.getUser(s.getName()).getString("Reply"));
						if (p != null) {
							from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom")
									.replace("%from%", s.getName()).replace("%to%", p.getName()));
							to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo")
									.replace("%from%", s.getName()).replace("%to%", p.getName()));
							to = to.replace("%message%", msg);
							from = from.replace("%message%", msg);
							TheAPI.getUser(p).setAndSave("Reply", s.getName());
							s.sendMessage(to);
							p.sendMessage(from);
							return true;
						}
						Loader.notOnline(s, TheAPI.getUser(s.getName()).getString("Reply"));
						return true;
					}
			}
			Loader.sendMessages(s, "NoReply");
			return true;
		}
		Loader.noPerms(s, "Reply", "Message");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
