
package Commands.BanSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Jail implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Jail")) {
			if(args.length==0) {
				Loader.Help(s, "/Jail <player> <reason>", "BanSystem.Jail");
				return true;
			}
			if(args.length==1) {
				if(Loader.config.getString("Jails")!=null) {
					if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish")
						.replace("%punishment%", "Jail").replace("%target%", args[0]), s);
						return true;
					}
					TheAPI.getPunishmentAPI().jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", Loader.config.getString("BanSystem.Jail.Reason")));
					return true;
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			if(args.length>=2) {
				if(!TheAPI.getPunishmentAPI().getjails().isEmpty()) {
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Jail").replace("%target%", args[0]), s);
						return true;
					}
				String msg = TheAPI.buildString(args);
				msg=msg.replaceFirst(args[0]+" ", "");
				
				TheAPI.getPunishmentAPI().jail(args[0], Loader.config.getString("BanSystem.Jail.Text").replace("%reason%", msg));
				
					return true;
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			
		}
		return true;
	}

}
