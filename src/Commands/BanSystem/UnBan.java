package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class UnBan implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.UnBan")) {
		if(args.length==0) {
			Loader.Help(s, "/UnBan <player>", "BanSystem.UnBan");
			return true;
		}
		if(args.length==1) {
			if(TheAPI.getPunishmentAPI().hasBan(args[0])||TheAPI.getPunishmentAPI().hasTempBan(args[0])) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.UnBan").replace("%player%", args[0]).replace("%playername%", args[0]),s);
				TheAPI.getPunishmentAPI().unBan(args[0]);
				TheAPI.getPunishmentAPI().unTempBan(args[0]);
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.UnBan")
						.replace("%operator%", s.getName()).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.UnBan");
				return true;
			}
			BanSystem.notExist(s, args);
			return true;
			
		}}return true;
	}

}
