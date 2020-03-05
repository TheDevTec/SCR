package Commands.Economy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import Commands.BanSystem.BanSystem;
import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Balance implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.econ==null) {
			Loader.msg(Loader.s("Prefix")+"&cMissing Vault plugin for economy.",s);
			return true;
		}
		if(args.length==0) {
			if(s instanceof Player) {
			if(API.hasPerm(s, "ServerControl.Balance")) {
				Player p = (Player)s;
				Loader.msg(Loader.s("Economy.Balance")
						.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
						.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName()), s);
				return true;
			}return true;}
		return true;
		}
		if(Loader.me.getString("Players."+args[0])!=null) {
			if(API.hasPerm(s, "ServerControl.Balance.Other")) {

				String world = Bukkit.getWorlds().get(0).getName();
				if(Bukkit.getPlayer(s.getName())!=null)world=((Player) s).getWorld().getName();
				
				Loader.msg(Loader.s("Economy.BalanceOther")
						.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[0],world), true))
						.replace("%currently%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[0],world), true))
						.replace("%prefix%", Loader.s("Prefix"))
						.replace("%player%", args[0])
						.replace("%playername%", BanSystem.getName(args[0])), s);
				return true;
			}return true;}
		return true;
	}
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		return null;
	}
}
