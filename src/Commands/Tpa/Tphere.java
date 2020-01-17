package Commands.Tpa;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Tphere implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Tphere")) {
			if(args.length==0) {
				Loader.Help(s, "/Tphere <player>", "TpaSystem.Tphere");
			}
			if(args.length==1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target==null) {
				Loader.msg(Loader.PlayerNotOnline(args[0]),s);
				return true;
			}else {
			if(!Loader.me.getBoolean("Players."+target.getName()+".TpBlock."+s.getName())&&!Loader.me.getBoolean("Players."+target.getName()+".TpBlock-Global")) {
			Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Teleportedhere").replace("%player%",target.getName()).replace("%playername%", target.getDisplayName()), s);
			target.teleport(((Player) s));
			return true;
			}else {
			if(s.hasPermission("ServerControl.Tphere.Blocked")) {
				Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.Teleportedhere").replace("%player%",target.getName()).replace("%playername%", target.getDisplayName()), s);
				target.teleport(((Player) s));
				return true;
			}else {
				Loader.msg(Loader.s("Prefix")+Loader.s("TpaSystem.TpBlocked")
				.replace("%playername%", target.getDisplayName())
				.replace("%player%", target.getName()), s);
				return true;
			}}}}
			return true;	
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
			if(args.length==1)
			return null;
		return c;
	}

}