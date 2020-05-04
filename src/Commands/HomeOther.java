package Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.User;

public class HomeOther implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(s instanceof Player) {
			Player p = (Player)s;
			if(API.hasPerm(s, "ServerControl.HomeOther")) {
				if(args.length==0) {
					Loader.Help(s, "/homeother <player> <home> <target>", "Homes.HomeOther"); 
					return true;
				}
				if(args.length==1) {
					Loader.Help(s, "/homeother <player> <home> <target>", "Homes.HomeOther"); 
					return true;
				}
				if(args.length==2) {
					User d = TheAPI.getUser(args[0]);
					if(d.exist("Homes."+args[1])) {
						Location loc = TheAPI.getStringUtils().getLocationFromString(d.getString("Homes."+args[1]));
						API.setBack(p);
						if(loc!=null){
							if(setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
						 else
							TheAPI.getPlayerAPI(p).teleport(loc);
						Loader.msg(Loader.s("Prefix")+Loader.s("Homes.TeleportingToOther")
			            .replace("%player%", p.getName())
			            .replace("%playername%", p.getDisplayName())
			            .replace("%target%", args[0])
						.replace("%home%", args[1]), s);
						return true;
						}}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.NotExistsOther")
			        .replace("%player%", p.getName())
			        .replace("%playername%", p.getDisplayName())
			        .replace("%target%", args[0])
					.replace("%home%", args[1]), s);
					return true;
				}
				if(args.length==3) {
					Player pl = TheAPI.getPlayer(args[2]);
					if(pl==null) {
						Loader.msg(Loader.PlayerNotOnline(args[2]), s);
						return true;
					}
					User d = TheAPI.getUser(args[0]);
					if(d.exist("Homes."+args[1])) {
						Location loc = TheAPI.getStringUtils().getLocationFromString(d.getString("Homes."+args[1]));
						API.setBack(pl);
						if(loc!=null){
							if(setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
						 else
							TheAPI.getPlayerAPI(p).teleport(loc);
						Loader.msg(Loader.s("Prefix")+Loader.s("Homes.TeleportingOtherToOther")
			            .replace("%player%", pl.getName())
			            .replace("%playername%", pl.getDisplayName())
			            .replace("%target%", args[0])
						.replace("%home%", args[1]), s);
						return true;
						}}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.NotExistsOther")
			        .replace("%player%", p.getName())
			        .replace("%playername%", p.getDisplayName())
			        .replace("%target%",args[0])
					.replace("%home%", args[1]), s);
					return true;
				}
				return true;
			}
			return true;
		}
		Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"), s);
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
    	if(s instanceof Player) {
    		if(args.length==1) {
    			return null;
    		}
    		if(args.length==2) {
            		c.addAll(StringUtil.copyPartialMatches(args[1], TheAPI.getUser(args[0]).getKeys("Homes"), new ArrayList<>()));
    			return c;
    		}
    		if(args.length==3) {
    			return null;
    		}
    	}
		return c;	
	}

}
