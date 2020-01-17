package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import Commands.BanSystem.BanSystem;
import ServerControl.API;
import ServerControl.Loader;

public class Tpaccept implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender p, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(p, "ServerControl.Tpa")||API.hasPerm(p, "ServerControl.Tpahere")) {
			if(p instanceof Player) {
			if(args.length==0) {
				String pd = RequestMap.getRequest(p.getName());
		        if(pd==null || Bukkit.getPlayer(pd) == null || pd != null && !RequestMap.containsRequest(p.getName(),pd)) {
		        	Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.NoRequest"),p);
		            return true;
		        }
				Player d = Bukkit.getPlayer(pd);
		        switch(RequestMap.getTeleportType(p.getName(),pd)) {
		        case TPA:
		            API.setBack(d);
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Tpaccept")
		            .replace("%player%",pd)
		            .replace("%playername%", BanSystem.getName(pd)), p);
		            d.teleport(((Player)p).getLocation());
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaAccepted")
		            .replace("%player%",p.getName())
		            .replace("%playername%", BanSystem.getName(p.getName())),d);
		            RequestMap.removeRequest(p.getName(),pd);
		            break;
		        case TPAHERE:
		            API.setBack((Player)p);
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Tpahereccept")
		            .replace("%player%",pd)
		            .replace("%playername%", BanSystem.getName(pd)), p);
		            ((Player)p).teleport(d);
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpahereAccepted")
		            .replace("%player%",p.getName())
		            .replace("%playername%", BanSystem.getName(p.getName())),d);
		            RequestMap.removeRequest(p.getName(),pd);
		            break;
		        }
				return true;
			}
			return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), p);
			return true;
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		return c;
	}	
}