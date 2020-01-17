package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class UnBanIP implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.UnBanIP")) {
		if(args.length==0) {
			Loader.Help(s, "/UnBanIP <player>", "BanSystem.UnBanIP");
			return true;
		}
		if(args.length==1) {
			if(TheAPI.getPunishmentAPI().hasBanIP(args[0])||TheAPI.getPunishmentAPI().hasTempBanIP(args[0])) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.UnBanIP").replace("%player%", args[0]).replace("%playername%", args[0]),s);

				TheAPI.getPunishmentAPI().unBanIP(args[0]);
				TheAPI.getPunishmentAPI().unTempBanIP(args[0]);
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.UnBanIP")
						.replace("%operator%", s.getName()).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.UnBanIP");
				return true;
				}
			BanSystem.notExist(s, args);
			return true;
			
		}}return true;
	}

}
