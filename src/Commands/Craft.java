package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Craft implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Workbench")) {
			if(s instanceof Player) {
				if(args.length==0) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningCraftTable"), s);
					((Player)s).openWorkbench(((Player) s).getLocation(), true);
					return true;
				}
				if(args.length==1) {
					Player t = TheAPI.getPlayer(args[0]);
					if(t==null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningCraftTableForTarget").replace("%target%",t.getDisplayName()), s);
					((Player)t).openWorkbench(((Player) t).getLocation(), true);
					return true;
				}
			}
			Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}

}
