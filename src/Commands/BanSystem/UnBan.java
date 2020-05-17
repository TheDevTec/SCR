package Commands.BanSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.PlayerBanList;
import me.Straiker123.TheAPI;

public class UnBan implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.UnBan")) {
		if(args.length==0) {
			Loader.Help(s, "/UnBan <player>", "BanSystem.UnBan");
			return true;
		}
		PlayerBanList p  =TheAPI.getPunishmentAPI().getBanList(args[0]);
			if(p.isBanned()||p.isTempBanned()) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.UnBan").replace("%player%", args[0]).replace("%playername%", args[0]),s);
				TheAPI.getPunishmentAPI().unban(args[0]);
				return true;
			}
			if(TheAPI.existsUser(args[0])) Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.PlayerHasNotBan")
			.replace("%player%", args[0]).replace("%playername%", args[0]),s);else
				Loader.msg(Loader.PlayerNotEx(args[0]),s);
			return true;
			
		}return true;
	}

}
