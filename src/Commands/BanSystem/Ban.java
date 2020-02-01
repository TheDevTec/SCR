package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Ban implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Ban")) {
		if(args.length==0) {
			Loader.Help(s, "/Ban <player> <reason>", "BanSystem.Ban");
			return true;
		}
		if(args.length==1) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Ban").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.Ban");
				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setBan(args[0], msg);
				return true;
		}
		if(args.length>=2) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true || Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Ban").replace("%target%", args[0]), s);
					return true;
				}
				String msg = BanSystem.BuildString(1, 1, args);
			TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
			TheAPI.getPunishmentAPI().setBan(args[0], msg);
			return true;
		}}
		return true;
	}

}
