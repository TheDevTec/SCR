package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class Exp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(args.length==0) {
			if(s.hasPermission("servercontrol.xp.give"))
			Loader.Help(s, "/Xp Give <player> <amount>", "Xp.Give");
			if(s.hasPermission("servercontrol.xp.take"))
			Loader.Help(s, "/Xp Take <player> <amount>", "Xp.Take");
			if(s.hasPermission("servercontrol.xp.balance"))
			Loader.Help(s, "/Xp Balance <player>", "Xp.Balance");
			return true;
		}
		if(args[0].equalsIgnoreCase("bal")||args[0].equalsIgnoreCase("balance")) {
			if(API.hasPerm(s, "servercontrol.xp.balance")){
			if(args.length==1) {
				Loader.Help(s, "/Xp Balance <player>", "Xp.Balance");
				return true;
			}
			Player p = Bukkit.getPlayer(args[1]);
			if(p==null) {
				Loader.msg(Loader.PlayerNotOnline(args[1]), s);
				return true;
			}
			Loader.msg(Loader.s("Prefix")+Loader.s("Xp.Balance").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()).replace("%amount%", ""+p.getExp()), s);
			return true;
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("give")||args[0].equalsIgnoreCase("add")) {
			if(API.hasPerm(s, "servercontrol.xp.give")){
			Loader.Help(s, "/Xp Give <player> <amount> <exp/level>", "Xp.Give");
			if(args.length==1||args.length==2) {
				return true;
			}
			Player p = Bukkit.getPlayer(args[1]);
			if(p==null) {
				if(args[0].equals("*")) {
					Repeat.a(s,"xp give * "+TheAPI.getNumbersAPI(args[2]).getInt());
					return true;
				}
				Loader.msg(Loader.PlayerNotOnline(args[1]), s);
				return true;
			}
			p.setTotalExperience(p.getTotalExperience()+TheAPI.getNumbersAPI(args[2]).getInt());
			Loader.msg(Loader.s("Prefix")+Loader.s("Xp.Given").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()).replace("%amount%", ""+TheAPI.getNumbersAPI(args[2]).getInt()), s);
			return true;
			}return true;
		}
		if(args[0].equalsIgnoreCase("take")) {
			if(API.hasPerm(s, "servercontrol.xp.take")){
			if(args.length==1||args.length==2) {
				Loader.Help(s, "/Xp Take <player> <amount>", "Xp.Take");
				return true;
			}
			Player p = Bukkit.getPlayer(args[1]);
			if(p==null) {
				if(args[0].equals("*")) {
					Repeat.a(s,"xp take * "+TheAPI.getNumbersAPI(args[2]).getInt());
					return true;
				}
				Loader.msg(Loader.PlayerNotOnline(args[1]), s);
				return true;
			}
			int take = (int)p.getExp();
			if(take-TheAPI.getNumbersAPI(args[2]).getInt() < 0) {
				p.setTotalExperience(take);
			}else
				p.setTotalExperience(take-TheAPI.getNumbersAPI(args[2]).getInt());
			Loader.msg(Loader.s("Prefix")+Loader.s("Xp.Taken").replace("%player%", p.getName()).replace("%playername%", p.getDisplayName()).replace("%amount%", ""+TheAPI.getNumbersAPI(args[2]).getInt()), s);
			return true;
			}return true;
		}
		return false;
	}

}
