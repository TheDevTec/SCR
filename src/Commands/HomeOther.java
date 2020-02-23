package Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
					Player t = Bukkit.getPlayer(args[0]);
					if(t==null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					}
					if(Loader.me.getString("Players."+t.getName()+".Homes."+args[1])!=null) {
						World w = Bukkit.getWorld(Loader.me.getString("Players."+t.getName()+".Homes."+args[1]+".World"));
						double x = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".X");
						double y = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".Y");
						double z = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".Z");
						float pitch = Loader.me.getInt("Players."+t.getName()+".Homes."+args[1]+".Pitch");
						float yaw = Loader.me.getInt("Players."+t.getName()+".Homes."+args[1]+".Yaw");
						API.setBack(p);
						Location loc = new Location(w,x,y,z,yaw,pitch);
						if(w!=null){
							if(setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
						 else
							TheAPI.getPlayerAPI(p).teleport(loc);
						Loader.msg(Loader.s("Prefix")+Loader.s("Homes.TeleportingToOther")
			            .replace("%player%", p.getName())
			            .replace("%playername%", p.getDisplayName())
			            .replace("%target%", t.getDisplayName())
						.replace("%home%", args[1]), s);
						return true;
						}}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.NotExistsOther")
			        .replace("%player%", p.getName())
			        .replace("%playername%", p.getDisplayName())
			        .replace("%target%", t.getDisplayName())
					.replace("%home%", args[1]), s);
					return true;
				}
				if(args.length==3) {
					Player t = Bukkit.getPlayer(args[0]);
					Player pl = Bukkit.getPlayer(args[2]);
					if(pl==null) {
						Loader.msg(Loader.PlayerNotOnline(args[2]), s);
						return true;
					}
					if(t==null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					}
					if(Loader.me.getString("Players."+t.getName()+".Homes."+args[1])!=null) {
						World w = Bukkit.getWorld(Loader.me.getString("Players."+t.getName()+".Homes."+args[1]+".World"));
						double x = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".X");
						double y = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".Y");
						double z = Loader.me.getDouble("Players."+t.getName()+".Homes."+args[1]+".Z");
						float pitch = Loader.me.getInt("Players."+t.getName()+".Homes."+args[1]+".Pitch");
						float yaw = Loader.me.getInt("Players."+t.getName()+".Homes."+args[1]+".Yaw");
						API.setBack(pl);
						Location loc = new Location(w,x,y,z,yaw,pitch);
						if(w!=null){
							if(setting.tp_safe)
								TheAPI.getPlayerAPI(p).safeTeleport(loc);
						 else
							TheAPI.getPlayerAPI(p).teleport(loc);
						Loader.msg(Loader.s("Prefix")+Loader.s("Homes.TeleportingOtherToOther")
			            .replace("%player%", pl.getName())
			            .replace("%playername%", pl.getDisplayName())
			            .replace("%target%", t.getDisplayName())
						.replace("%home%", args[1]), s);
						return true;
						}}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.NotExistsOther")
			        .replace("%player%", p.getName())
			        .replace("%playername%", p.getDisplayName())
			        .replace("%target%", t.getDisplayName())
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
    			Player t = Bukkit.getPlayer(args[0]);
    			try {
            		Set<String> homes = Loader.me.getConfigurationSection("Players."+t.getName()+".Homes").getKeys(false);
            		if(!homes.isEmpty() && homes != null)
            		c.addAll(StringUtil.copyPartialMatches(args[1], homes, new ArrayList<>()));
            		}catch(Exception e) {
            			
            		}	
    			return c;
    		}
    		if(args.length==3) {
    			return null;
    		}
    	}
		return c;	
	}

}
