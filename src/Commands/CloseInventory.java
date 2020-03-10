package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class CloseInventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.CloseInventory")) {
			if(args.length==0) {
				Loader.Help(s, "/closeinv <player> ", "Inventory.CloseInventory");
				return true;
			}
			if(args.length==1) {
				Player p = TheAPI.getPlayer(args[0]);
				if(p==null) {
					if(args[0].equals("*")) {
						Repeat.a(s,"closeinv *");
						return true;
					}
					Loader.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("Inventory.ClosePlayersInventory").replace("%player%",p.getDisplayName()).replace("%playername%",p.getDisplayName()), s);
				p.closeInventory();
				return true;
			}
			return true;
		}
		
		return true;
	}

}
