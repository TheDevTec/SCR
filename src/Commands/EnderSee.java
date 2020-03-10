package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;
import me.Straiker123.PlayerAPI.InvseeType;

public class EnderSee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.EnderChest")) {
			if(s instanceof Player) {
				if(args.length==0) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningEnderChest"), s);
				TheAPI.getPlayerAPI((Player)s).invsee((Player)s, InvseeType.ENDERCHEST);
				return true;
				}
				if(args.length==1) {
					Player p = TheAPI.getPlayer(args[0]);
					if(p!=null) {
					if(p==s) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningEnderChest"), s);
						TheAPI.getPlayerAPI((Player)s).invsee((Player)s, InvseeType.ENDERCHEST);
						return true;
					}else {
						if(API.hasPerm(s, "ServerControl.EnderSee")) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.OpeningEnderChestOther")
						.replace("%player%",p.getName()).replace("%playername%",p.getDisplayName()), s);

						TheAPI.getPlayerAPI((Player)s).invsee(p, InvseeType.ENDERCHEST);
						return true;
						}
					return true;	
					}
				}
					Loader.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}
			}	
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		
		return true;
	}

}