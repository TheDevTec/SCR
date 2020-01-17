package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Helpop implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s,"ServerControl.Helpop")) {
		if(args.length==0) {
			Loader.Help(s, "/Helpop <message>", "Helpop");
			return true;
		}
		if(args.length>=1) {
			String msg = "";
			for (int i = 0; i < args.length; i++) {
			msg = msg + args[i] + " ";
			}
			msg = TheAPI.colorize(msg);
			msg = msg.substring(0, msg.length()-1);
			msg = Loader.config.getString("Format.Helpop").replace("%sender%", s.getName()).replace("%playername%", displayname(s)).replace("%message%", msg).replace("%player%", s.getName());
			TheAPI.broadcast(msg, "ServerControl.Helpop.Receive");
			if(!s.hasPermission("ServerControl.Helpop.Receive")) {
				Loader.msg(msg, s);
				return true;
			}
			return true;
		}}
		return true;
	}
	public String displayname(CommandSender s) {
		if(Bukkit.getPlayer(s.getName())!=null)return Bukkit.getPlayer(s.getName()).getDisplayName();
		return s.getName();
	}

}
