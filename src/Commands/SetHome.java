package Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
public class SetHome implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

		if(s instanceof Player) {
			Player p = (Player)s;
			Location l = p.getLocation();
			if(API.hasPerm(s, "ServerControl.SetHome")) {
            if(Loader.vault == null) {
                if(args.length == 0) {
                	Loader.me.set("Players."+p.getName()+".Homes.home.World", p.getLocation().getWorld().getName());
                	Loader.me.set("Players."+p.getName()+".Homes.home.X", l.getX());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Y", l.getY());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Z", l.getZ());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Pitch", p.getLocation().getPitch());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Yaw", p.getLocation().getYaw());
                	Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Created")
        			.replace("%home%", "home"),s);
                    return true;   
            }
            }else {
                if(args.length == 0) {
                	Loader.me.set("Players."+p.getName()+".Homes.home.World", p.getLocation().getWorld().getName());
                	Loader.me.set("Players."+p.getName()+".Homes.home.X", l.getX());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Y", l.getY());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Z", l.getZ());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Pitch", p.getLocation().getPitch());
                	Loader.me.set("Players."+p.getName()+".Homes.home.Yaw", p.getLocation().getYaw());
                	Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Created")
                    .replace("%player%", p.getName())
                    .replace("%playername%", p.getDisplayName())
                    .replace("%home%", "home"),s);
                    return true;   
            }
                if(args.length == 1) {
                	if(Loader.me.getString("Players."+p.getName()+".Homes")!=null) {
                		if(Loader.config.getString("Homes."+ Loader.vault.getPrimaryGroup(p))!=null) {
                    if(Loader.me.getConfigurationSection("Players."+p.getName()+".Homes").getKeys(false).size() < Loader.config.getInt("Homes."+ Loader.vault.getPrimaryGroup(p))) {
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".World", p.getLocation().getWorld().getName());
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".X", l.getX());
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Y", l.getY());
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Z", l.getZ());
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Pitch", p.getLocation().getPitch());
                    	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Yaw", p.getLocation().getYaw());
                    	Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Created")
                        .replace("%player%", p.getName())
                        .replace("%playername%", p.getDisplayName())
                        .replace("%home%", args[0]),s);
                        return true;
                }
                    if(Loader.me.getConfigurationSection("Players."+p.getName()+".Homes").getKeys(false).size() >= Loader.config.getInt("Homes."+ Loader.vault.getPrimaryGroup(p))) {
                    	Loader.msg(Loader.s("Prefix")+Loader.s("Homes.LimitReached")
            			.replace("%limit%", String.valueOf(Loader.config.getInt("Homes."+ Loader.vault.getPrimaryGroup(p)))),s);
                        return true;
                }}
                		if(Loader.me.getConfigurationSection("Players."+p.getName()+".Homes").getKeys(false).size() >= 1) {
                			Loader.msg(Loader.s("Prefix")+Loader.s("Homes.LimitReached")
                			.replace("%limit%", String.valueOf(1)),s);
                            return true;	
                	}
                }
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".World", p.getLocation().getWorld().getName());
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".X", l.getX());
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Y", l.getY());
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Z", l.getZ());
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Pitch", p.getLocation().getPitch());
                	Loader.me.set("Players."+p.getName()+".Homes."+args[0]+".Yaw", p.getLocation().getYaw());
                	Loader.msg(Loader.s("Prefix")+Loader.s("Homes.Created")
                    .replace("%player%", p.getName())
                    .replace("%playername%", p.getDisplayName())
                    .replace("%home%", args[0]),s);
                    return true;
            }}}return true;}
		Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"),s);
	return true;
}}
