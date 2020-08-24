package Commands.Economy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import Utils.Pagination;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.SortedMap.RankingAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class EcoTop implements CommandExecutor {
	// world, rankingapi
	@SuppressWarnings("rawtypes")
	HashMap<String, RankingAPI> h = Maps.newHashMap();

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			TheAPI.msg(Loader.s("Prefix") + "&cMissing Vault plugin for economy.", s);
			return true;
		}
		if (API.hasPerm(s, "ServerControl.BalanceTop")) {
			String world = Bukkit.getWorlds().get(0).getName();
			if (s instanceof Player)
				world = ((Player) s).getWorld().getName();
			@SuppressWarnings("unchecked")
			RankingAPI<String, BigDecimal> m = h.containsKey(world) ? h.get(world) : null;
			if (TheAPI.getCooldownAPI("ServerControlReloaded").expired("scr") || m == null) {
				TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr", 300); // 5min update
				HashMap<String, BigDecimal> money = Maps.newHashMap();
				for (UUID sa : TheAPI.getUsers()) {
					if(Bukkit.getOfflinePlayer(sa).getName()==null)continue;
					if(Bukkit.getOfflinePlayer(sa).getName().equals("ServerControlReloaded"))continue;
					money.put(Bukkit.getOfflinePlayer(sa).getName(),
							new BigDecimal(EconomyAPI.getBalance(Bukkit.getOfflinePlayer(sa).getName(), world)));
				}
				if (m != null)
					h.remove(world); 
				m = new RankingAPI<String, BigDecimal>(money);
				h.put(world, m);
			}
			List<String> list = new ArrayList<String>();
			for (String o : m.getKeySet()) {
				if(o!=null && m.containsKey(o))
				list.add(o.toString() + ":" + m.getValue(o));
			}
			Pagination<String> g = new Pagination<>(10, list);
			TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bTOP 10 Players &e-----------------", s);
			TheAPI.msg("", s);
			int page = 1;
			if (args.length != 0)
				page = StringUtils.getInt(args[0]);
			if (g.totalPages() < page)
				page = g.totalPages();
			if (1 > page)
				page = 1;
			--page;
			HashMap<String, BigDecimal> money = new HashMap<String, BigDecimal>();
			for (String sa : g.getPage(page)) {
				String[] f = sa.split(":");
				money.put(f[0], new BigDecimal(f[1]));
			}
			RankingAPI<String, BigDecimal> ms = new RankingAPI<String, BigDecimal>(money);
			int i = 0;
			for(Entry<String, BigDecimal> item : ms.entrySet()){
				if(i==10)break;
				++i;
				TheAPI.msg(Loader.config.getString("Options.Economy.BalanceTop").replace("%position%", i + "")
						.replace("%player%", item.getKey()).replace("%playername%", player(item.getKey()))
						.replace("%money%", API.setMoneyFormat(item.getValue(), true)), s);
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
