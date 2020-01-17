package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class UnMute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.UnMute")) {
		if(args.length==0) {
			Loader.Help(s, "/UnMute <player>", "BanSystem.UnMute");
			return true;
		}
		if(args.length==1) {
			if(TheAPI.getPunishmentAPI().hasMute(args[0])||TheAPI.getPunishmentAPI().hasTempMute(args[0])) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.UnMute").replace("%player%", args[0])
						.replace("%playername%", args[0]),s);

				TheAPI.getPunishmentAPI().unMute(args[0]);
				TheAPI.getPunishmentAPI().unTempMute(args[0]);
				if(Bukkit.getPlayer(args[0])!=null)Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.UnMuted")
				.replace("%player%", args[0]).replace("%playername%", args[0]),Bukkit.getPlayer(args[0]));
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.UnMute")
						.replace("%operator%", s.getName()).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.UnMute");
				return true;
			}
			BanSystem.notMuted(s, args);
			return true;
			
		}}return true;
	}

}
