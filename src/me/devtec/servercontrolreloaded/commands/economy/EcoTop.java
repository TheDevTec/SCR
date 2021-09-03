package me.devtec.servercontrolreloaded.commands.economy;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.Eco;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class EcoTop implements CommandExecutor, TabCompleter {
	public static HashMap<String, TreeMap<Double, String>> h = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (EconomyAPI.getEconomy() == null) {
			return true;
		}
		if (Loader.has(s, "BalanceTop", "Economy")) {
			if(!CommandsManager.canUse("Economy.BalanceTop", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Economy.BalanceTop", s))));
				
				return true;
			
			}
			Loader.sendMessages(s, "Economy.BalanceTop.Loading");
			new Tasker() {
				public void run() {
				String world = Eco.getEconomyGroupByWorld(Bukkit.getWorlds().get(0).getName());
				if (s instanceof Player)
					world = Eco.getEconomyGroupByWorld(((Player) s).getWorld().getName());
				TreeMap<Double, String> m = h.get(world);
				if (TheAPI.getCooldownAPI("ServerControlReloaded").expired("scr") || m == null) {
					TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr", 300*20); 
					TreeMap<Double, String> money = new TreeMap<>((var1, var2) -> var2.compareTo(var1));
					for (UUID sa : TheAPI.getUsers()) {
						String n = LoaderClass.cache.lookupNameById(sa);
						if(n!=null) {
							double bal = EconomyAPI.getBalance(n, world);
							if(bal>0)
								money.put(bal,n);
						}
					}
					h.put(world, m=money);
				}
				int pages = (int) Math.ceil((double) m.size() / 10);
				int page =args.length!=0?StringUtils.getInt(args[0]):1;
				if(page<=0)page=1;
				if(pages<page)page=pages;
				--page;
				
				
				Loader.sendMessages(s, "Economy.BalanceTop.Header", Placeholder.c().replace("%page%",(page+1)+"")
						.replace("%pages%", pages+""));
				int min = page * 10;
				int max = ((page * 10) + 10);

				if (max > m.size())
					max = m.size();
				
				int slot=0;
				int i = 0;
					for (Entry<Double, String> sf : m.entrySet()) {
						if (slot < max && slot >= min) {
							String key = sf.getValue();
							++i;
							TheAPI.msg(Loader.config.getString("Options.Economy.BalanceTop").replace("%position%", (i + (10 * (page + 1)) - 10) + "")
									.replace("%player%", key).replace("%playername%", player(s, key))
									.replace("%money%", API.setMoneyFormat(sf.getKey(), true)), s);
						}
						++slot;
					}
				Loader.sendMessages(s, "Economy.BalanceTop.Footer", Placeholder.c().replace("%page%",(page+1)+"")
						.replace("%pages%", pages+""));
			}}.runTask();
			return true;
		}
		Loader.noPerms(s, "BalanceTop", "Economy");
		return true;
	}

	public static String player(CommandSender d, String s) {
		if (TheAPI.getPlayerOrNull(s) != null)
			return API.getPlayers(d).contains(TheAPI.getPlayerOrNull(s)) ? TheAPI.getPlayerOrNull(s).getDisplayName() : s;
		return s;
	}
	public static void reload(Player p) {
		String world = Eco.getEconomyGroupByWorld(Bukkit.getWorlds().get(0).getName());
		if (p!=null &&p instanceof Player )
			world = Eco.getEconomyGroupByWorld(p.getWorld().getName());
		
		TheAPI.getCooldownAPI("ServerControlReloaded").createCooldown("scr", 300*20); 
		TreeMap<Double, String> money = new TreeMap<>((var1, var2) -> var2.compareTo(var1));
		for (UUID sa : TheAPI.getUsers()) {
			String n = LoaderClass.cache.lookupNameById(sa);
			if(n!=null) {
				double bal = EconomyAPI.getBalance(n, world);
				if(bal>0)
					money.put(bal,n);
			}
		}
		h.put(world, money);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
