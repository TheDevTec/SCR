
package Commands.BanSystem;

import java.util.ArrayList;
import java.util.List;

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
				List<String> jails = new ArrayList<String>();
				if(Loader.config.getString("Jails")!=null) {
				for(String f:Loader.config.getConfigurationSection("Jails").getKeys(false))jails.add(f);
					if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish")
						.replace("%punishment%", "Jail").replace("%target%", args[0]), s);
						return true;
					}
					String msg = Loader.config.getString("BanSystem.Jail.Reason");
					API.getBanSystemAPI().setJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size())),msg);
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.Jail")
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.jail");
					return true;
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			if(args.length>=2) {
				List<String> jails = new ArrayList<String>();
				if(Loader.config.getString("Jails")!=null) {
				for(String f:Loader.config.getConfigurationSection("Jails").getKeys(false))jails.add(f);
				if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Jail").replace("%target%", args[0]), s);
						return true;
					}
				String msg = TheAPI.buildString(args);
				msg=msg.replaceFirst(args[0]+" ", "");
					API.getBanSystemAPI().setJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size())),msg);
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.Jail")
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.jail");
					return true;
				}
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			
		}
		return true;
	}

}
