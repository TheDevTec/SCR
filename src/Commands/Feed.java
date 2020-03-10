package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class Feed implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
			if(API.hasPerm(s, "ServerControl.Feed")){
				if(args.length==0) {
					if(s instanceof Player) {
						Player p = (Player)s;
						p.setFoodLevel(20);
					Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Feed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
					}
					Loader.Help(s, "/Feed <player>", "Feed");
					return true;
			}
				if(args.length==1) {
					Player p = TheAPI.getPlayer(args[0]);
					if(p==null) {
						if(args[0].equals("*")) {
							Repeat.a(s,"feed *");
							return true;
						}
						Loader.msg(Loader.PlayerNotOnline(args[0]),s);
						return true;
					}
					if(p==s) {
						p.setFoodLevel(20);
					Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Feed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;	
					
					}
					if(API.hasPerm(s, "ServerControl.Feed.Other")) {
					p.setFoodLevel(20);
					Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Feed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), p);
					Loader.msg(Loader.s("Prefix")+Loader.s("Heal.PlayerFeed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;	
					}return true;	
				}
				return true;
		}
		Loader.msg(Loader.s("ConsoleErrorMessage"), s);
		return true;
	}

}