package Commands.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import Utils.Colors;
import me.DevTec.TheAPI.TheAPI;

public class ReplyPrivateMes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "PrivateMessage", "Message")) {
			if (args.length == 0) {
				Loader.Help(s, "Reply", "Message");
				return true;
			}
			if (args.length >= 1) {
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
						} else {
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
				}
				Loader.sendMessages(s, "NoReply");
				return true;
			}
			return true;
		}
		Loader.noPerms(s, "Reply", "Message");
		return true;
	}

}
