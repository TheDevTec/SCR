package Commands.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.Straiker123.TheAPI;

public class Pay implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (TheAPI.getEconomyAPI().getEconomy() == null) {
			Loader.msg(Loader.s("Prefix") + "&cMissing Vault plugin for economy.", s);
			return true;
		}

		if (API.hasPerm(s, "ServerControl.Pay")) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (args.length == 0 || args.length == 1) {
					Loader.Help(s, "/Pay <player> <money>", "Economy.Pay");
					return true;
				}
				if (args.length == 2) {
					if (TheAPI.existsUser(args[0])) {
						String moneyfromargs = args[1];
						if (moneyfromargs.startsWith("-"))
							moneyfromargs = "0.0";
						double money = API.convertMoney(moneyfromargs);
						if (TheAPI.getEconomyAPI().has(p, p.getWorld().getName(), money)
								|| s.hasPermission("ServerControl.Economy.InMinus")) {
							TheAPI.getEconomyAPI().withdrawPlayer(p, p.getWorld().getName(), money);
							TheAPI.getEconomyAPI().depositPlayer(args[0], p.getWorld().getName(), money);

							Loader.msg(Loader.s("Economy.PaidTo").replace("%money%", API.setMoneyFormat(money, true))
									.replace("%currently%",
											API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
									.replace("%prefix%", Loader.s("Prefix")).replace("%player%", args[0])
									.replace("%playername%", args[0]), s);
							if (get(args[0]) != null) {
								Loader.msg(Loader.s("Economy.PaidFrom")
										.replace("%money%", API.setMoneyFormat(money, true))
										.replace("%currently%",
												API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(args[0]), true))
										.replace("%prefix%", Loader.s("Prefix")).replace("%player%", s.getName())
										.replace("%playername%", ((Player) s).getDisplayName()), get(args[0]));
							}
							return true;
						}
						Loader.msg(Loader.s("Economy.NoMoney")
								.replace("%money%",
										API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
								.replace("%currently%",
										API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(s.getName()), true))
								.replace("%player%", args[0]).replace("%playername%", args[0]), s);
						return true;
					}
					if (args[0].equals("*")) {
						Repeat.a(s, "pay * " + API.convertMoney(args[1]));
						return true;
					}
					Loader.msg(Loader.PlayerNotEx(args[0]), s);
					return true;
				}
			}
		}
		return true;
	}

	public Player get(String args) {
		if (TheAPI.getPlayer(args) != null) {
			return TheAPI.getPlayer(args);
		}
		return null;
	}
}