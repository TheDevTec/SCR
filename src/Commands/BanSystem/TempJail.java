package Commands.BanSystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class TempJail implements CommandExecutor, TabCompleter {

	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.TempJail")) {
			if(args.length==0||args.length==1) {
				Loader.Help(s, "/TempJail <player> <time> <reason>", "BanSystem.TempJail");
				return true;
			}
			if (TheAPI.getUser(args[0]).getBoolean("Immune")|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "Mute").replace("%target%", args[0]), s);
				return true;
			}
			if(Loader.config.getString("Jails")==null) {
				Loader.msg(Loader.s("Prefix")+Loader.s("BanSystem.MissingJail"), s);
				return true;
			}
			List<String> jails = new ArrayList<String>();
		for(String f:Loader.config.getConfigurationSection("Jails").getKeys(false))jails.add(f);
			if(args.length==2) {
					String msg = Loader.config.getString("BanSystem.Jail.Reason");
					long time = TheAPI.getStringUtils().getTimeFromString(args[1]);
					API.getBanSystemAPI().setTempJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size()-1)),msg,time);
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.TempJail")
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%time%", ""+TheAPI.getStringUtils().setTimeToString(time))
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.jail");
					return true;
			}
			if(args.length>=3) { 
				String msg = TheAPI.buildString(args);
				msg=msg.replaceFirst(args[0]+" "+args[1]+" ", "");
					long time = TheAPI.getStringUtils().getTimeFromString(args[1]);
					API.getBanSystemAPI().setTempJail(s, args[0], jails.get(TheAPI.generateRandomInt(jails.size()-1)),msg,time);
					TheAPI.broadcast(Loader.s("Prefix")+Loader.s("BanSystem.Broadcast.TempJail")
							.replace("%operator%", s.getName()).replace("%reason%", msg).replace("%player%", args[0])
							.replace("%time%", ""+TheAPI.getStringUtils().setTimeToString(time))
							.replace("%playername%", BanSystem.getName(args[0])), "servercontrol.jail");
					return true;
				}
			}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if(s.hasPermission("ServerControl.TempJail")) {
			if(args.length==1) {
				return null;
			}
		}
		return c;
	}
}
