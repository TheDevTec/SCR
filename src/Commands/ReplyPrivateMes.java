package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class ReplyPrivateMes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.PrivateMessage")) {
	if(args.length==0) {
		Loader.Help(s,"/Reply <message>", "ReplyPrivateMessage");
	}
	if(args.length>=1) {
		String path = "";

		if(s instanceof Player ==false)path="Server.Reply";else {
			path="Players."+s.getName()+".Reply";
		}
		if(Loader.me.getString(path)!=null) {
		String msg = "";
		for (int i = 0; i < args.length; i++) {
		msg = msg + args[i] + " ";
		}
		msg = msg.substring(0, msg.length()-1);
		msg = Events.ChatFormat.r(msg,s,false);
		String from = "";
		String to = "";
		if(s instanceof Player ==false) {
			if(Loader.me.getString("Server.Reply").equalsIgnoreCase("CONSOLE")) {
				from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
				 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
				 to = to.replace("%message%", msg);
				 from = from.replace("%message%", msg);
				if(s instanceof Player ==false) {
					Loader.me.set("Server.Reply", "CONSOLE");
					s.sendMessage(to);
				Bukkit.getConsoleSender().sendMessage(from);
				return true;
			}}else {
			Player p = TheAPI.getPlayer(Loader.me.getString("Server.Reply"));
			if(p!=null) {
				from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", p.getName()));
				 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", p.getName()));
				 to = to.replace("%message%", msg);
				 from = from.replace("%message%", msg);
				Loader.me.set("Server.Reply", p.getName());
				Loader.me.set("Players."+p.getName()+".Reply", s.getName());

				Configs.chatme.save();s.sendMessage(to);
					p.sendMessage(from);
				return true;
			}
			Loader.msg(Loader.PlayerNotOnline(Loader.me.getString("Server.Reply")), s);
			return true;
		}}else {
		if(Loader.me.getString("Players."+s.getName()+".Reply").equalsIgnoreCase("CONSOLE")) {
			from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
			 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", "CONSOLE"));
			 to = to.replace("%message%", msg);
			 from = from.replace("%message%", msg);
			Loader.me.set("Server.Reply", s.getName());
			Loader.me.set("Players."+s.getName()+".Reply", "CONSOLE");
			Configs.chatme.save();
			s.sendMessage(to);
			Bukkit.getConsoleSender().sendMessage(from);
			return true;
		}else {
		Player p = TheAPI.getPlayer(Loader.me.getString("Players."+s.getName()+".Reply"));
		if(p!=null) {
			from = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageFrom").replace("%from%", s.getName()).replace("%to%", p.getName()));
			 to = TheAPI.colorize(Loader.config.getString("Format.PrivateMessageTo").replace("%from%", s.getName()).replace("%to%", p.getName()));
			 to = to.replace("%message%", msg);
			 from = from.replace("%message%", msg);
			Loader.me.set("Players."+s.getName()+".Reply", p.getName());
			Loader.me.set("Players."+p.getName()+".Reply", s.getName());

			Configs.chatme.save();s.sendMessage(to);
			p.sendMessage(from);
			return true;
		}
		Loader.msg(Loader.PlayerNotOnline(Loader.me.getString("Players."+s.getName()+".Reply")), s);
		return true;
	}}}
		Loader.msg(Loader.s("PrivateMessage.NoPlayerToReply"), s);
		return true;
	}}
		return true;
	}

}
