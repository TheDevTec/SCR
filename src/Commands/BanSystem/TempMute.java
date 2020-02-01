package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class TempMute implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.TempMute")) {
		if(args.length==0) {
			Loader.Help(s, "/TempMute <player> <time> <reason>", "BanSystem.TempMute");
			return true;
		}
		if(args.length==1) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");
				long cooldownTime = TheAPI.getTimeConventorAPI().getTimeFromString(Loader.config.getString("BanSystem.TempMute.Time"));

				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setTempMute(args[0], msg,cooldownTime);
				return true;
			
		}
		if(args.length==2) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.TempMute.Reason");

				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setTempMute(args[0], msg,TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
				return true;
		}
		if(args.length>=3) {
				if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempMute").replace("%target%", args[0]), s);
					return true;
				}
				String msg = BanSystem.BuildString(2, 1, args);
			TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
			TheAPI.getPunishmentAPI().setTempMute(args[0], msg,TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
			return true;
		}}
		return true;
	}

}
