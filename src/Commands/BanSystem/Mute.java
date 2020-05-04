package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Mute implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Mute")) {
		if(args.length==0) {
			Loader.Help(s, "/Mute <player> <reason>", "BanSystem.Mute");
			return true;
		}
		if(args.length==1) {
			if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]), s);
					return true;
				}
				String msg = Loader.config.getString("BanSystem.Mute.Reason");
				TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
				TheAPI.getPunishmentAPI().setMute(args[0], msg);
				return true;
		}
		if(args.length>=2) {
			if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]), s);
					return true;
				}
			String msg = TheAPI.buildString(args);
			msg=msg.replaceFirst(args[0]+" ", "");
			TheAPI.getPunishmentAPI().setSilent(Loader.config.getBoolean("BanSystem.Broadcast-Silent"));
			TheAPI.getPunishmentAPI().setMute(args[0], msg);
			return true;
		}}
		return true;
	}

}
