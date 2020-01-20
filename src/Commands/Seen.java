package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.API.SeenType;

public class Seen implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Seen")) {
		if(args.length==0) {
			Loader.Help(s,"/Seen <player>","Seen");
			return true;
		}
			String a = Loader.me.getString("Players."+args[0]);
			if(a!=null) {
				Player p = Bukkit.getPlayer(args[0]);
				if(p!=null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Seen.Online")
					.replace("%playername%", p.getDisplayName())
					.replace("%online%", API.getSeen(p.getName(), SeenType.Online))
					.replace("%player%", p.getName()), s);
					return true;
				}else {
					Loader.msg(Loader.s("Prefix")+Loader.s("Seen.Offline")
					.replace("%playername%", args[0])
					.replace("%offline%", API.getSeen(args[0], SeenType.Offline))
					.replace("%player%", args[0]), s);
					return true;
				}
				
			}
			Loader.msg(Loader.PlayerNotEx(args[0]),s);
			return true;
		
		}
		return true;
	}

}
