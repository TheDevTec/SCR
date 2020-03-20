package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Colors;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class PrivateMessage implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.PrivateMessage")) {
			if(args.length==0||args.length==1) {
				Loader.Help(s, "/Msg <player> <message>", "PrivateMessage");
			}
			if(args.length>=2) {
				
				String msg=Colors.colorize(TheAPI.buildString(args).replaceFirst(args[0]+" ", ""),false,s);
				String from = "";
				String to = "";
				if(args[0].equalsIgnoreCase("CONSOLE")) {
					if(s instanceof Player ==false) {
					Loader.me.set("Server.Reply", "CONSOLE");
					}else {
						Loader.me.set("Server.Reply", s.getName());
						Loader.me.set("Players."+s.getName()+".Reply", "CONSOLE");
					}
					Configs.chatme.save();
					 from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
					 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
					 to = to.replace("%message%", msg);
					 from = from.replace("%message%", msg);
					s.sendMessage(to);
					Bukkit.getConsoleSender().sendMessage(from);
					return true;
				}else {
				Player p = TheAPI.getPlayer(args[0]);
				if(p!=null) {
					from =TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", p.getName()));
					 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", p.getName()));
					 to = to.replace("%message%", msg);
					 from = from.replace("%message%", msg);if(s instanceof Player ==false) {
						Loader.me.set("Server.Reply", p.getName());
						Loader.me.set("Players."+p.getName()+".Reply", "CONSOLE");
					}else {
						Loader.me.set("Players."+s.getName()+".Reply", p.getName());
						Loader.me.set("Players."+p.getName()+".Reply", s.getName());
					}
						Configs.chatme.save();

					s.sendMessage(to);
					p.sendMessage(from);
					return true;
				}
				Loader.msg(Loader.PlayerNotOnline(args[0]), s);
				return true;
			}}}
		return true;
	}

}
