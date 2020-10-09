package Utils;

import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.Loader.Item;
import me.DevTec.TheAPI.APIs.MemoryAPI;
import me.DevTec.TheAPI.APIs.TabListAPI;
import me.DevTec.TheAPI.EconomyAPI.EconomyAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.TheAPI;

public class TabList {
	private static String group(Player p) {
		if (API.existVaultPlugin()) {
			if (Loader.perms != null && Loader.vault != null)
				if (Loader.perms.getPrimaryGroup(p) != null)
					return Loader.perms.getPrimaryGroup(p);
			return "default";
		}
		return "default";
	}

	public static String getGroup(Player p) {
		if (Loader.tab.getString("Groups." + group(p) + ".Priorite") != null) {
			return Loader.tab.getString("Groups." + group(p) + ".Priorite");
		}
		return "z";
	}

	public static String getPrefix(Player p, boolean nametag) {
		if (Loader.tab.exists("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Prefix"))
			return replace(Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Prefix"), p);
		return null;
	}

	public static String getNameFormat(Player p) {
		if (Loader.tab.exists("Groups." + group(p) + ".Name"))
			return replace(Loader.tab.getString("Groups." + group(p) + ".Name"), p);
		return "%tab-prefix% "+p.getName()+" %tab-suffix%";
	}

	public static String getSuffix(Player p, boolean nametag) {
		if (Loader.tab.exists("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Suffix"))
			return replace(Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Suffix"), p);
		return null;
	}

	public static String replace(String header, Player p) {
		header=TheAPI.colorize(header);
		if(p!=null) {
		if(header.contains("%money%"))
				header=header.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), false));
		if(header.contains("%colored_money%"))
			header=header.replace("%colored_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%online%")) {
			List<Player> seen = Lists.newArrayList();
			for(Player s : TheAPI.getPlayers())
				if(!TheAPI.canSee(p,s))
					seen.add(s);
			header=header.replace("%online%", seen.size() + "");
		}
		if(header.contains("%ping%"))
			header=header.replace("%ping%", Loader.getInstance.pingPlayer(p));
		;if(header.contains("%world%"))
			header=header.replace("%world%", p.getWorld().getName())
		;if(header.contains("%hp%")) {
			double hp = 0.0;
			try {
				hp=(double)p.getHealth();
			}catch(Exception e) {
				hp=(double)Ref.invoke(p, "getHealthScale");
			}
			header=header.replace("%hp%", hp + "");
		}
		if(header.contains("%health%")) {
			double hp = 0.0;
			try {
				hp=(double)p.getHealth();
			}catch(Exception e) {
				hp=(double)Ref.invoke(p, "getHealthScale");
			}
			header=header.replace("%health%", hp+ "");
		}
		if(header.contains("%food%"))
			header=header.replace("%food%", p.getFoodLevel() + "")
		;if(header.contains("%x%"))
			header=header.replace("%x%", p.getLocation().getBlockX() + "").replace("%y%", p.getLocation().getBlockY() + "")
		;if(header.contains("%z%"))
			header=header.replace("%z%", p.getLocation().getBlockZ() + "");
		if(header.contains("%vault-group%")) {
			String group = Loader.get(p, Item.GROUP);
		if (Loader.vault != null)
			group = Loader.vault.getPrimaryGroup(p);
			header=header.replace("%vault-group%", group);
		}
		if(header.contains("%vault-prefix%"))
			header=header.replace("%vault-prefix%", Loader.get(p, Item.PREFIX))
		;if(header.contains("%vault-suffix%"))
			header=header.replace("%vault-suffix%", Loader.get(p, Item.SUFFIX))
		;if(header.contains("%group%"))
			header=header.replace("%group%", getGroup(p));
		if(header.contains("%kills%"))
			header=header.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS))
		;if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS))
		;if(header.contains("%player%"))
			header=header.replace("%player%", p.getName())
		;if(header.contains("%playername%")) {
			String displayname = p.getName();
			if (p.getDisplayName() != null)
				displayname = p.getDisplayName();
			header=header.replace("%playername%", displayname);
		}
		if(header.contains("%customname%")) {
			String customname = p.getName();
		if (p.getCustomName() != null)
			customname = p.getCustomName();
		if(TheAPI.getUser(p).exists("DisplayName"))
			customname = TheAPI.getUser(p).getString("DisplayName");
			header=header.replace("%customname%", customname);
		}
		if(header.contains("%afk%"))
			header=header.replace("%afk%", Loader.isAfk(p));
		}else
			if(header.contains("%online%"))
				header=header.replace("%online%", TheAPI.getPlayers().size()+"");
		if(header.contains("%max_players%"))
			header=header.replace("%max_players%", TheAPI.getMaxPlayers() + "");
		;if(header.contains("%time%"))
			header=header.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
		;if(header.contains("%date%"))
			header=header.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()));
		;if(header.contains("%tps%"))
			header=header.replace("%tps%", TheAPI.getServerTPS() + "")
		;if(header.contains("%ram_free%"))
			header=header.replace("%ram_free%", MemoryAPI.getFreeMemory(false) + "")
		;if(header.contains("%ram_free_percentage%"))
			header=header.replace("%ram_free_percentage%", MemoryAPI.getFreeMemory(true) + "%")
		;if(header.contains("%ram_usage%"))
			header=header.replace("%ram_usage%", MemoryAPI.getUsedMemory(false) + "")
		;if(header.contains("%ram_usage_percentage%"))
			header=header.replace("%ram_usage_percentage%", MemoryAPI.getUsedMemory(true) + "%")
		;if(header.contains("%ram_max%"))
			header=header.replace("%ram_max%", MemoryAPI.getMaxMemory() + "").replace("%ram_max_percentage%", "100%")
		;
		return PlaceholderAPI.setPlaceholders(p, header);
	}
	
	public static void setName(Player p) {
		String p1 = getPrefix(p, true);
		String p2 = getPrefix(p, false);
		String s1 = getSuffix(p, true);
		String s2 = getSuffix(p, false);
		String name = getNameFormat(p).replace("%tab-prefix%", (p2!=null?replace(p2, p):"")).replace("%tab-suffix%", (s2!=null?replace(s2, p):""));
		TabListAPI.setTabListName(p, name);
		NameTagChanger.setNameTag(p, p1!=null?replace(p1, p):"", s1!=null?replace(s1, p):"");
	}
	
	static int test;
	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			TabListAPI.setTabListName(p,p.getName());
			TabListAPI.setHeaderFooter(p, "", "");
		}
	}

	private static String get(String path, Player p) {
		if (setting.tab_header || setting.tab_footer) {
			if (Loader.tab.getStringList(path) != null) {
				String a = StringUtils.join(Loader.tab.getStringList(path), "\n");
				return replace(a, p);
			}
		}
		return "";
	}

	private static String getPath(Player p, String what) {
		if (what.equalsIgnoreCase("footer") && setting.tab_footer
				|| what.equalsIgnoreCase("header") && setting.tab_header) {
			if (Loader.tab.getString("PerPlayerTabList." + p.getName() + "." + what) != null)
				return get("PerPlayerTabList." + p.getName() + "." + what, p);
			else if (Loader.tab.getString("PerWorldTabList." + p.getWorld().getName() + "." + what) != null)
				return get("PerWorldTabList." + p.getWorld().getName() + "." + what, p);
			else {
				return get(what, p);
			}
		}
		return "";
	}

	public static void setFooterHeader(Player p) {
		TabListAPI.setHeaderFooter(p, getPath(p, "Header"), getPath(p, "Footer"));
	}
}
