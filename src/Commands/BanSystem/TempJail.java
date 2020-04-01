package Commands.BanSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.LoaderClass;
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
			if (Loader.me.getBoolean("Players."+args[0]+".Immune")==true|| Bukkit.getOperators().contains(Bukkit.getOfflinePlayer(args[0]))) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Immune.NoPunish").replace("%punishment%", "TempJail").replace("%target%", args[0]), s);
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
					String msg = BanSystem.BuildString(2, 1, args);
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
			/*if(args.length==2) {
				if(args[1].equalsIgnoreCase("")) { 
				c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("1","2","3","4","5","6","7","8","9","0"), new ArrayList<>()));
				}
				String sec =LoaderClass.config.getConfig().getString("Words.Second");
				String min =LoaderClass.config.getConfig().getString("Words.Minute");
				String h =LoaderClass.config.getConfig().getString("Words.Hour");
				String d =LoaderClass.config.getConfig().getString("Words.Day");
				String w =LoaderClass.config.getConfig().getString("Words.Week");
				String mon = LoaderClass.config.getConfig().getString("Words.Month");
				String y =LoaderClass.config.getConfig().getString("Words.Year");
				if(args[1].startsWith("1")||args[1].startsWith("2")||args[0].startsWith("3")||
						args[1].startsWith("4")||args[1].startsWith("5")||args[1].startsWith("6")||
						args[1].startsWith("7")||args[1].startsWith("8")||args[1].startsWith("9")){
					if(args[1].endsWith("0")||args[1].endsWith("1")||args[1].endsWith("2")||args[0].endsWith("3")||
						args[1].endsWith("4")||args[1].endsWith("5")||args[1].endsWith("6")||
						args[1].endsWith("7")||args[1].endsWith("8")||args[1].endsWith("9")||args[1].endsWith("0")) {
					c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList(args[1]+sec,args[1]+min,args[1]+h,args[1]+d,args[1]+w,args[1]+mon,
							args[1]+y), new ArrayList<>()));
					}
				}
				if(args[1].contains(sec)) {
					
				}
			}*/
		}
		return c;
	}
}
