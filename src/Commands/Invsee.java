package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.PlayerAPI.InvseeType;
import me.Straiker123.TheAPI;

public class Invsee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Invsee")) {
				if(args.length==0) {
				Loader.Help(s, "/Invsee <player> <target>", "Invsee");
				return true;
				}
				if(args.length==1) {
					if(s instanceof Player) {
						Player p = Bukkit.getPlayer(args[0]);
						if(p==null) {
							Loader.msg(Loader.PlayerNotOnline(args[0]), s);
							return true;
						} 
							Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningInvsee").replace("%player%",p.getName()).replace("%playername%",p.getDisplayName()), s);
							TheAPI.getPlayerAPI((Player)s).invsee(p, InvseeType.INVENTORY);
							return true;
					}
					Loader.Help(s, "/Invsee <player> <target>", "Invsee");
					return true;
				}
				if(args.length==2) {
					Player p = Bukkit.getPlayer(args[0]);
					Player t = Bukkit.getPlayer(args[1]);
					if(p==null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					} 
					if(t==null) {
						Loader.msg(Loader.PlayerNotOnline(args[1]), s);
						return true;
					} 
						Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningInvseeForTarget").replace("%player%",p.getName()).replace("%playername%",p.getDisplayName()).replace("%target%",t.getDisplayName()), s);
						TheAPI.getPlayerAPI((Player)t).invsee(p, InvseeType.INVENTORY);
						return true;
						
				}
			}
			return true;
	}

}
