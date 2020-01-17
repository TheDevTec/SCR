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

public class Tpadeny implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Tpa")||API.hasPerm(s, "ServerControl.Tpahere")) {
			if(s instanceof Player) {
			if(args.length==0) {
				String pd = RequestMap.getRequest(s.getName());
		        if(pd==null || Bukkit.getPlayer(pd) == null || pd != null && !RequestMap.containsRequest(s.getName(),pd)) {
		        	Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.NoRequest"),s);
		            return true;
		        }
				Player d = Bukkit.getPlayer(pd);
				Player p = (Player)s;
		        switch(RequestMap.getTeleportType(p.getName(),pd)) {
		        case TPA:
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Tpadeny")
		            .replace("%player%",pd)
		            .replace("%playername%", BanSystem.getName(pd)), p);
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpaDenied")
		            .replace("%player%",p.getName())
		            .replace("%playername%", p.getDisplayName()),d);
		            RequestMap.removeRequest(p.getName(),pd);
		            break;
		        case TPAHERE:
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Tpaheredeny")
		            .replace("%player%",pd)
		            .replace("%playername%", BanSystem.getName(pd)), p);
		            Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpahereDenied")
		            .replace("%player%",p.getName())
		            .replace("%playername%", p.getDisplayName()),d);
		            RequestMap.removeRequest(p.getName(),pd);
		            break;
		        }
				return true;
			}
			return true;
			}
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		return c;
	}}