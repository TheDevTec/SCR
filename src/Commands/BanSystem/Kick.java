package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Kick implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Kick")) {
		if(args.length==0) {
			Loader.Help(s, "/Kick <player> <reason>", "BanSystem.Kick");
			return true;
		}
		if(args.length==1) {
			Player p = TheAPI.getPlayer(args[0]);
			if(p!=null) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "kick"
							).replace("%target%", p.getName()), s);
					return true;
				}
				TheAPI.getPunishmentAPI().kick(args[0], Loader.config.getString("BanSystem.Kick.Text").replace("%reason%", Loader.config.getString("BanSystem.Kick.Reason")));
				return true;
			}
			Loader.msg(Loader.PlayerNotOnline(args[0]), s);
			return true;
		}
		if(args.length>=2) {
			Player p = TheAPI.getPlayer(args[0]);
			if(p!=null) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "kick")
							.replace("%target%", p.getName()), s);
					return true;
				}
				String msg = TheAPI.buildString(args);
				msg=msg.replaceFirst(args[0]+" ", "");
				TheAPI.getPunishmentAPI().kick(args[0], Loader.config.getString("BanSystem.Kick.Text").replace("%reason%", msg));
			return true;
			}
			Loader.msg(Loader.PlayerNotOnline(args[0]), s);
			return true;
		}
		}
		return true;
	}

}
