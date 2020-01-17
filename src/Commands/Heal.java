package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.Repeat;
import me.Straiker123.CooldownAPI;
import me.Straiker123.TheAPI;

public class Heal implements CommandExecutor {
	CooldownAPI a= TheAPI.getCooldownAPI("SCR-Heal");
	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {
		if(!API.hasPerm(s, "ServerControl.Heal"))return true;
		if(args.length == 0) {
		if(s instanceof Player == false) {
			Loader.Help(s, "/Heal <player>", "Heal");
			return true;
		}
		Player p = (Player)s;
		if(a.expired(p.getName()) || s.hasPermission("ServerControl.Heal.Bypass")) {
			new SPlayer(p).heal();
			if(!s.hasPermission("ServerControl.Heal.Bypass"))
    			a.createCooldown(p.getName(), TheAPI.getTimeConventorAPI().getTimeFromString(
    					Loader.config.getString("Heal-Cooldown")));
			Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Healed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),s);
			return true;
		}
				Loader.msg(Loader.s("Heal.CooldownMessage").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
				.replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString(a.getTimeToExpire(p.getName()))),s);
                return true;
            }
			if(args.length == 1){
				if(args[0].equals("*")) {
					Repeat.a(s,"feed * ");
					return true;
				}
        		Player target = (Player)Bukkit.getServer().getPlayer(args[0]);
        		if(target == null) {
        			Loader.msg(Loader.PlayerNotOnline(args[0]),s);
        			return true;
        		}
        		if(target == s) {
        			Player p = (Player)s;
        			if(a.expired(p.getName()) || s.hasPermission("ServerControl.Heal.Bypass")) {
        				new SPlayer(p).heal();
            			if(!s.hasPermission("ServerControl.Heal.Bypass"))
                			a.createCooldown(p.getName(), TheAPI.getTimeConventorAPI().getTimeFromString(
                					Loader.config.getString("Heal-Cooldown")));
        				Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Healed").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()),s);
        				return true;
        			}
        					Loader.msg(Loader.s("Heal.CooldownMessage").replace("%player%", p.getName())
        							.replace("%playername%", p.getDisplayName())
        					.replace("%time%", TheAPI.getTimeConventorAPI().setTimeToString(a.getTimeToExpire(p.getName()))),s);
        	                return true;
        	            }
    				if(API.hasPerm(s, "ServerControl.Heal.Other")){
    					new SPlayer(target).heal();
    					Loader.msg(Loader.s("Prefix")+Loader.s("Heal.Healed").replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()),target);
            			Loader.msg(Loader.s("Prefix")+Loader.s("Heal.SpecifyPlayerHealed").replace("%player%", target.getName()).replace("%playername%", target.getDisplayName()),s);
            			return true;
            		}return true;
            		}
		
		return false;
		}
}
