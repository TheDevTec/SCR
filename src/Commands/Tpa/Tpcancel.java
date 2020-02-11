package Commands.Tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Commands.BanSystem.BanSystem;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Tpcancel implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(s.hasPermission("ServerControl.Tpa")||s.hasPermission("ServerControl.Tpahere")) {
			if(s instanceof Player) {
					String pd = RequestMap.getRequest(s.getName());
			        if(pd==null || !RequestMap.containsRequest(s.getName(),pd)) {
			        	Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.NoRequest"),s);
			            return true;
			        }
					Player d = Bukkit.getPlayer(pd);
					Player p = (Player)s;
			            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Cancelled")
			            .replace("%player%",p.getName())
			            .replace("%playername%", BanSystem.getName(pd)), d);
			            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaCancel")
			            .replace("%player%",p.getName())
			            .replace("%playername%", p.getDisplayName()),p);
			            RequestMap.removeRequest(p.getName(), pd);
						return true;
				}
				Loader.msg(Loader.s("ConsoleErrorMessage"), s);
				return true;
		}
		s.sendMessage(TheAPI.colorize(Loader.s("NotPermissionsMessage")
				.replace("%player%", s.getName())
				.replace("%playername%", s.getName())
				.replace("%permission%", "ServerControl.Tpa or ServerControl.Tpahere")));
		return true;
	}

}
