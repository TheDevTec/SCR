package Commands.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.DevTec.EconomyAPI;
import me.DevTec.TheAPI;

public class Pay implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			TheAPI.msg(Loader.s("Prefix") + "&cMissing Vault plugin for economy.", s);
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
					if (args[0].equals("*")) {
						Repeat.a(s, "pay * " + API.convertMoney(args[1]));
						return true;
					}
					if (TheAPI.existsUser(args[0])) {
						String moneyfromargs = args[1];
						if (moneyfromargs.startsWith("-"))
							moneyfromargs = "0.0";
						double money = API.convertMoney(args[1]);
						if (EconomyAPI.has(p.getName(), money)
								|| s.hasPermission("ServerControl.Economy.InMinus")) {
							String w = p.getWorld().getName();
							EconomyAPI.withdrawPlayer(p.getName(),w, money);
							EconomyAPI.depositPlayer(args[0],w, money);
							TheAPI.msg(Loader.s("Economy.PaidTo")
									.replace("%money%", API.setMoneyFormat(money, true))
									.replace("%currently%",
											API.setMoneyFormat(EconomyAPI.getBalance(s.getName()),
													true))
									.replace("%prefix%", Loader.s("Prefix")).replace("%player%", args[0])
									.replace("%playername%", args[0]), s);
							if (get(args[0]) != null && get(args[0]).getWorld().getName()
									.equals(TheAPI.getPlayer(s.getName()).getWorld().getName())) {
								TheAPI.msg(
										Loader.s("Economy.PaidFrom")
												.replace("%money%", API.setMoneyFormat(money, true))
												.replace("%currently%", API.setMoneyFormat(
														EconomyAPI.getBalance(args[0]), true))
												.replace("%prefix%", Loader.s("Prefix"))
												.replace("%player%", s.getName()).replace("%playername%",
														((Player) s).getDisplayName()),
										TheAPI.getPlayer(args[0]));
							}
							return true;
						}
						TheAPI.msg(Loader.s("Economy.NoMoney")
								.replace("%money%",
										API.setMoneyFormat(EconomyAPI.getBalance(s.getName()), true))
								.replace("%currently%",
										API.setMoneyFormat(EconomyAPI.getBalance(s.getName()), true))
								.replace("%player%", args[0]).replace("%playername%", args[0]), s);
						return true;
					}
					TheAPI.msg(Loader.PlayerNotEx(args[0]), s);
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