package Commands.Economy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;

import static java.util.Map.Entry.*;

public class EcoTop implements CommandExecutor {

	public HashMap<String, Double> exampleBank = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.econ==null) {
			Loader.msg(Loader.s("Prefix")+"&cMissing Vault plugin for economy.",s);
			return true;
		}
				if (API.hasPerm(s, "ServerControl.BalanceTop")) {
					if (Loader.me.getString("Players") != null) {
							Loader.msg(Loader.s("Prefix") +"&e----------------- &bTOP 10 Players &e-----------------", s);
							Loader.msg("",s);
							String world = Bukkit.getWorlds().get(0).getName();
							if(s instanceof Player)world=((Player) s).getWorld().getName();
							for (String sa : Loader.me.getConfigurationSection("Players").getKeys(false)) {
								exampleBank.put(sa, Loader.econ.getBalance(sa,world));
							}

							HashMap<String, Double> sorted = exampleBank.entrySet().stream().sorted(comparingByValue())
									.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
											LinkedHashMap::new));
							int size = sorted.size();
							int counter = 1;
							List keys = new ArrayList(sorted.keySet());
							for (int i = size - 1; i >= 0; i--) {
								if (counter > 10) {
									break;
								}
								Object obj = keys.get(i);
								Loader.msg(Loader.config.getString("Options.Economy.BalanceTop")
										.replace("%position%", String.valueOf(counter))
										.replace("%player%", obj.toString())
										.replace("%playername%", player(obj.toString()))
										.replace("%money%", API.setMoneyFormat(exampleBank.get(obj), true)), s);
								counter++;
							}
							exampleBank.clear();
							sorted.clear();
							return true;
						}
					Loader.msg(Loader.s("Economy.NoPlayers"), s);
					return true;
				}
				return true;
	}

	public String player(String s) {
		if (Bukkit.getPlayer(s) != null)
			return Bukkit.getPlayer(s).getDisplayName();
		return s;
	}
}
