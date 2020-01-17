package Commands;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;

public class Homes implements CommandExecutor {

	ArrayList<String> ne = new ArrayList<String>();
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {

			
		if(args.length==0) {
			if(s instanceof Player) {
				Player p = (Player)s;
				if(API.hasPerm(s, "ServerControl.Home")) {
					if(Loader.me.getString("Players."+p.getName()+".Homes")!=null) {
					ne.clear();
					for(String a:Loader.me.getConfigurationSection("Players."+p.getName()+".Homes").getKeys(false)) {
						ne.add(a);
					}
					if(!ne.isEmpty()) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.List")
					.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
					.replace("%list%", StringUtils.join(ne,", ")), s);
					return true;
				}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.ListEmpty")
					.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;}
					Loader.msg(Loader.s("Prefix")+Loader.s("Homes.ListEmpty")
					.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
					return true;
					}
					return true;
			}
			Loader.msg(Loader.s("Prefix")+Loader.s("ConsoleErrorMessage"),s);
			return true;
		}
		
		if(args.length==1) {
			if(API.hasPerm(s, "ServerControl.Home.Other")) {
				SPlayer target = new SPlayer((Player)Bukkit.getServer().getPlayer(args[0]));
					if(target.getPlayer() == null) {
						Loader.msg(Loader.PlayerNotOnline(args[0]), s);
						return true;
					}
					else {
						if(Loader.me.getString("Players."+target.getName()+".Homes")!=null) {
							ne.clear();
							for(String a:Loader.me.getConfigurationSection("Players."+target.getName()+".Homes").getKeys(false)) {
								ne.add(a);
							}
							if(!ne.isEmpty()) {
							Loader.msg(Loader.s("Prefix")+Loader.s("Homes.ListOther")
							.replace("%target%", target.getName()).replace("%playername%", target.getDisplayName())
								.replace("%list%", StringUtils.join(ne,", ")), s);
								return true;
							}
								Loader.msg(Loader.s("Prefix")+Loader.s("Homes.ListEmpty")
								.replace("%target%", target.getName()).replace("%playername%", target.getDisplayName()), s);
								return true;}
								Loader.msg(Loader.s("Prefix")+Loader.s("Homes.ListEmpty")
								.replace("%target%", target.getName()).replace("%playername%", target.getDisplayName()), s);
								return true;
					}
			}
			return true;
		}
		return true;
	}
}
