package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Thor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Thor")) {
			if(args.length==0) {
				Loader.Help(s, "/Thor <player>", "Thor");
				return true;
			}
				Player p = Bukkit.getPlayer(args[0]);
				if(p!=null) {
					p.getWorld().strikeLightning(p.getLocation());
					Loader.msg(Loader.s("Prefix")+Loader.s("Thor").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
				}
				Loader.msg(Loader.PlayerNotOnline(args[0]),s);
				return true;
		}
		return true;
	}

}
