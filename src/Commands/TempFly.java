package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import me.Straiker123.TheAPI;

public class TempFly implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.TempFly")) {
			if(args.length==0) {
				Loader.Help(s, "/TempFly <Player> <Time>", "TempFly");
				return true;
			}
			if(args.length==1) {
				if(TheAPI.getStringUtils().getInt(args[0])>0 && s instanceof Player) {
					if(API.hasPerm(s, "ServerControl.TempFly")) {
						new SPlayer(Bukkit.getPlayer(s.getName())).enableTempFly((int) TheAPI.getStringUtils().getTimeFromString(args[1]));
						return true;}return true;
				}
				Loader.Help(s, "/TempFly <Player> <Time>", "TempFly");
				return true;
			}
			if(args.length==2) {
				SPlayer t = new SPlayer(Bukkit.getPlayer(args[0]));
				if(t.getPlayer() == null) {
					Loader.msg(Loader.PlayerNotOnline(args[0]), s);
					return true;
				}
				int sec =(int) TheAPI.getStringUtils().getTimeFromString(args[1]);
				if(t.getPlayer() == s) {
					if(API.hasPerm(s, "ServerControl.TempFly")) {
						t.enableTempFly(sec);
						return true;}return true;
					}
					if(API.hasPerm(s, "ServerControl.TempFly.Other")) {
						Loader.msg(Loader.s("Prefix")+Loader.s("TempFly.EnabledOther")
						.replace("%player%", t.getName())
						.replace("%playername%", t.getName())
						.replace("%target%", t.getName())
						.replace("%time%", TheAPI.getStringUtils().setTimeToString(sec)), s);
						
						t.enableTempFly(sec);
						return true;
					}
				return true;
			}
			return true;
		}
		return true;
	}
}
