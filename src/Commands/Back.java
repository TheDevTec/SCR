package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Back implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==0) {
		if(API.hasPerm(s, "ServerControl.Back")) {
		if(s instanceof Player) {
			Player p = (Player)s;
			API.TeleportBack(p);
				return true;
		}
		Loader.Help(s, "/Back <player>", "Back");
		return true;
		}}
		if(args.length==1) {
			Player p = Bukkit.getPlayer(args[0]);
			if(p==null) {
				Loader.msg(Loader.PlayerNotEx(args[0]), s);
				return true;
			}else {
			if(p==s) {
				if(API.hasPerm(s, "ServerControl.Back")) {
					API.TeleportBack(p);
			return true;
			}return true;}
			if(p!=s) {
				if(API.hasPerm(s, "ServerControl.Back.Other")) {
			Loader.msg(Loader.s("Prefix")+Loader.s("Back.PlayerTeleported").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()), s);
			API.TeleportBack(p);
			return true;
			}return true;}
		}
		}
		return true;
	}}