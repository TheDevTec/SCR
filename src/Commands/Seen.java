package Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ServerControl.API;
import ServerControl.API.SeenType;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Seen implements CommandExecutor {
	List<String> getS(String a){
		List<String> l = new ArrayList<String>();
		for(String s : Loader.me.getConfigurationSection("Players").getKeys(false)) {
			if(a.equalsIgnoreCase(s))l.add(s);
		}
		return l;
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Seen")) {
		if(args.length==0) {
			Loader.Help(s,"/Seen <player>","Seen");
			return true;
		}
			String a = Loader.me.getString("Players."+args[0]);
			if(a!=null) {
				if(TheAPI.getPlayer(args[0]) != null && TheAPI.getPlayer(args[0]).getName().equals(args[0])) {
					Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Seen.Online"), args[0])
					.replace("%online%", API.getSeen(args[0], SeenType.Online)), s);
					return true;
				}else {
					Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Seen.Offline"), args[0])
					.replace("%offline%", API.getSeen(args[0], SeenType.Offline)), s);
					return true;
				}
				
			}
			List<String> sim = getS(args[0]);
			if(sim.isEmpty())
			Loader.msg(Loader.PlayerNotEx(args[0]),s);
			else {
				Loader.msg(Loader.s("Seen.SimiliarNames").replace("%names%", StringUtils.join(sim,", "))
						.replace("%list%", StringUtils.join(sim,", ")),s);
			}
			return true;
		
		}
		return true;
	}

}
