package Commands.Economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Pagination;
import me.Straiker123.MultiMap;
import me.Straiker123.RankingAPI;
import me.Straiker123.TheAPI;

public class EcoTop implements CommandExecutor {
	//world, rankingapi
	MultiMap<String> h= TheAPI.getMultiMap();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.econ==null) {
			Loader.msg(Loader.s("Prefix")+"&cMissing Vault plugin for economy.",s);
			return true;
		}
				if (API.hasPerm(s, "ServerControl.BalanceTop")) {
					String world = Bukkit.getWorlds().get(0).getName();
					if(s instanceof Player)world=((Player) s).getWorld().getName();
						RankingAPI m = (h.containsKey(world) ? (RankingAPI)h.getValues(world).get(0) : null);
					if(TheAPI.getCooldownAPI("scr.baltop").expired("scr")||m==null) {
						TheAPI.getCooldownAPI("scr.baltop").createCooldown("scr", 300); //5min update
						HashMap<String, Double> money = new HashMap<String, Double>();
						for (String sa : Loader.me.getConfigurationSection("Players").getKeys(false)) {
							money.put(sa, Loader.econ.getBalance(sa,world));
						}
						if(m!=null)
						h.remove(world);
						m=TheAPI.getRankingAPI(money);
						h.put(world,m);
					}
					List<String> list = new ArrayList<String>();
					for(Object o : m.getKeySet()) {
						list.add(o.toString()+":"+m.getValue(o));
					}
					Pagination<String> g=new Pagination<>(10, list);
					if (Loader.me.getString("Players") != null) {
							Loader.msg(Loader.s("Prefix") +"&e----------------- &bTOP 10 Players &e-----------------", s);
							Loader.msg("",s);
							int page = 1;
							if(args.length!=0)
								page=TheAPI.getStringUtils().getInt(args[0]);
							if(g.totalPages() < page)page=g.totalPages();
							if(1 > page)page=1;
							--page;
							HashMap<String, Double> money = new HashMap<String, Double>();
							for (String sa : g.getPage(page)) {
								String[] f = sa.split(":");
								money.put(f[0], TheAPI.getStringUtils().getDouble(f[1]));
							}
							RankingAPI ms =TheAPI.getRankingAPI(money);
							for (int i = 1; i <ms.getKeySet().size()+1; i++) {
								String player = ms.getObject(i).toString();
								Loader.msg(Loader.config.getString("Options.Economy.BalanceTop")
										.replace("%position%",i+"")
										.replace("%player%", player)
										.replace("%playername%", player(player))
										.replace("%money%", API.setMoneyFormat(m.getValue(player), true)), s);
							}
							return true;
						}
					Loader.msg(Loader.s("Economy.NoPlayers"), s);
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
