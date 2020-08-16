package Commands.Economy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.EconomyAPI;
import me.DevTec.TheAPI;

public class Balance implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			TheAPI.msg(Loader.s("Prefix") + "&cMissing Vault plugin for economy.", s);
			return true;
		}
		if (args.length == 0) {
			if (s instanceof Player) {
				if (API.hasPerm(s, "ServerControl.Balance")) {
					Player p = (Player) s;
					TheAPI.msg(Loader.s("Economy.Balance")
							.replace("%money%",
									API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true))
							.replace("%currently%",
									API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true))
							.replace("%prefix%", Loader.s("Prefix")).replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName()), s);
					return true;
				}
				return true;
			}
			return true;
		}
		if (TheAPI.existsUser(args[0])) {
			if (API.hasPerm(s, "ServerControl.Balance.Other")) {

				String world = Bukkit.getWorlds().get(0).getName();
				if (TheAPI.getPlayer(s.getName()) != null)
					world = ((Player) s).getWorld().getName();

				TheAPI.msg(Loader.s("Economy.BalanceOther")
						.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(args[0], world), true))
						.replace("%currently%",
								API.setMoneyFormat(EconomyAPI.getBalance(args[0], world), true))
						.replace("%prefix%", Loader.s("Prefix")).replace("%player%", args[0])
						.replace("%playername%", args[0]), s);
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return null;
	}
}
