package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;

public class Vanish implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {

	if(API.hasPerm(s, "ServerControl.Vanish")) {
		if(args.length==0) {
		if(s instanceof Player) {
			Player p = (Player)s;
		if(!Loader.me.getBoolean("Players."+s.getName()+".Vanish")) {
			Loader.me.set("Players."+s.getName()+".Vanish", true);
			Configs.chatme.save();
			TheAPI.vanish(p, "ServerControl.Vanish", true);
			Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.Enabled"),s);
		    return true;
		}
		Loader.me.set("Players."+s.getName()+".Vanish", false);
		Configs.chatme.save();
		TheAPI.vanish(p, "ServerControl.Vanish", false);
		Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.Disabled"),s);
	    return true;
		}
		Loader.Help(s, "/Vanish <player>","Vanish");
		return true;
		}
		if(args.length==1) {
			Player t = Bukkit.getPlayer(args[0]);
			if(t!=null) {
				if(!Loader.me.getBoolean("Players."+t.getName()+".Vanish")) {
					Loader.me.set("Players."+t.getName()+".Vanish", true);
					Configs.chatme.save();TheAPI.vanish(t, "ServerControl.Vanish", true);
				Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.Enabled")
				.replace("%player%", t.getName())
				.replace("%playername%", t.getDisplayName()),t);
				Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.EnabledPlayer")
				.replace("%player%", t.getName())
				.replace("%playername%", t.getDisplayName()),s);
			    return true;
			}
				Loader.me.set("Players."+t.getName()+".Vanish", false);
				Configs.chatme.save();
			TheAPI.vanish(t, "ServerControl.Vanish", false);
			Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.Disabled")
			.replace("%player%", t.getName())
			.replace("%playername%", t.getDisplayName()),t);
			Loader.msg(Loader.s("Prefix")+Loader.s("Vanish.DisabledPlayer")
			.replace("%player%", t.getName())
			.replace("%playername%", t.getDisplayName()),s);
		    return true;
			}
			Loader.msg(Loader.PlayerNotOnline(args[0]),s);
		    return true;
		}}return true;}}