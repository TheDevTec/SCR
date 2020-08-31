package Commands.Economy;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Eco;
import Utils.Pagination;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.SortedMap.RankingAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class EcoTop implements CommandExecutor {
	private HashMap<String, Pagination<Entry<String, Double>>> h = Maps.newHashMap();

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			TheAPI.msg(Loader.s("Prefix") + "&cMissing Vault plugin for economy.", s);
			return true;
		}
		if (API.hasPerm(s, "ServerControl.BalanceTop")) {
			String world = Eco.getEconomyGroupByWorld(Bukkit.getWorlds().get(0).getName());
			if (s instanceof Player)
				world = Eco.getEconomyGroupByWorld(((Player) s).getWorld().getName());
			Pagination<Entry<String, Double>> m = h.containsKey(world) ? h.get(world) : null;
			if (TheAPI.getCooldownAPI("ServerControlReloaded").expired("scr") || m == null) {
				TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr", 300); // 5min update
				HashMap<String, Double> money = Maps.newHashMap();
				for (UUID sa : TheAPI.getUsers()) {
					if(Bukkit.getOfflinePlayer(sa).getName()==null||Bukkit.getOfflinePlayer(sa).getName().equals("ServerControlReloaded"))continue;
					money.put(Bukkit.getOfflinePlayer(sa).getName(),EconomyAPI.getBalance(Bukkit.getOfflinePlayer(sa).getName(), world));
				}
				if (m != null)
					h.remove(world); 
				m = new Pagination<>(10,new RankingAPI<>(money).entrySet());
				h.put(world, m);
			}
			TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bTOP 10 Players &e-----------------", s);
			TheAPI.msg("", s);
			int page =args.length!=0?StringUtils.getInt(args[0]):1;
			--page;
			if(m.totalPages()<=page)page=m.totalPages()-1;
			int i = 0;
			for(Entry<String, Double> sf : m.getPage(page)){
				String key = sf.getKey();
				++i;
				TheAPI.msg(Loader.config.getString("Options.Economy.BalanceTop").replace("%position%", (i+(10*(page+1))-10) + "")
						.replace("%player%", key).replace("%playername%", player(key))
						.replace("%money%", API.setMoneyFormat(sf.getValue(),true)), s);
			}
			return true;
		}
		return true;
	}

	public String player(String s) {
		if (TheAPI.getPlayer(s) != null)
			return TheAPI.getPlayer(s).getDisplayName();
		return s;
	}
}
