package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Warn implements CommandExecutor {
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Warn")) {
		if(args.length==0) {
			Loader.Help(s, "/Warn <player> <reason>", "BanSystem.Warn");
			return true;
		}
		if(args.length==1) {
			if(TheAPI.existsUser(args[0])) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Warn")
							.replace("%target%", args[0]), s);
					return true;
				}
				API.getBanSystemAPI().setWarn(args[0], s, Loader.config.getString("BanSystem.Warn.Reason"));
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.Warn")
						.replace("%operator%", s.getName()).replace("%reason%", Loader.config.getString("BanSystem.Warn.Reason")).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.Warn");
				return true;
			}
			BanSystem.notExist(s, args);
			return true;}
		if(args.length>=2) {
			if(TheAPI.existsUser(args[0])) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Warn")
							.replace("%target%", args[0]), s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg = msg.replaceFirst(args[0]+" ",	"");
				API.getBanSystemAPI().setWarn(args[0], s, msg);
				TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.Warn")
						.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.Warn");
				return true;
			}
			BanSystem.notExist(s, args);
			return true;
			
		}
	}
		return true;
	}
}
