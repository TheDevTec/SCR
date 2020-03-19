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

public class TempJail implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Jail")) {
			if(args.length==0||args.length==1) {
				Loader.Help(s, "/TempJail <player> <time> <reason>", "BanSystem.TempJail");   //TODO MSG
				return true;
			}
			if(args.length==2) {
				if(Loader.config.getString("Jails")!=null) {
					List<String> jails = new ArrayList<String>();
					for(String f:Loader.config.getConfigurationSection("Jails").getKeys(false))jails.add(f);
					if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish")
						.replace("%punishment%", "TempJail").replace("%target%", args[0]), s);
						return true;
					}
					long time = TheAPI.getTimeConventorAPI().getTimeFromString(args[1]);
					String msg = Loader.config.getString("BanSystem.Jail.Reason");
					API.getBanSystemAPI().setTempJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size())),msg,
							TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.TempJail")   //TODO MSG
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%time%", ""+TheAPI.getTimeConventorAPI().setTimeToString(time))
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.jail");
					return true;
					
				}
				return true;
			}
			if(args.length>=3) {
				if(Loader.config.getString("Jails")!=null) {
					List<String> jails = new ArrayList<String>();
				for(String f:Loader.config.getConfigurationSection("Jails").getKeys(false))jails.add(f);
					if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
						Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempJail").replace("%target%", args[0]), s);
						return true;
					}
					long time = TheAPI.getTimeConventorAPI().getTimeFromString(args[1]);
					String msg = BanSystem.BuildString(2, 1, args);
					API.getBanSystemAPI().setTempJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size())),msg,
							TheAPI.getTimeConventorAPI().getTimeFromString(args[1]));
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.TempJail")
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%time%", ""+TheAPI.getTimeConventorAPI().setTimeToString(time))
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
