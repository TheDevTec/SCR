package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.DevTec.EconomyAPI;
import me.DevTec.MemoryAPI;
import me.DevTec.TabListAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.Ref;
import me.DevTec.Other.StringUtils;
import me.DevTec.Placeholders.PlaceholderAPI;

public class TabList {
	private static String group(Player p) {
		if (API.existVaultPlugin()) {
			if (Loader.perms != null && Loader.vault != null)
				if (Loader.perms.getPrimaryGroup(p) != null) {
					return Loader.perms.getPrimaryGroup(p);
				}
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
		if (Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Prefix") != null) {
			return replace(Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Prefix"), p);
		}
		return null;
	}

	public static String getNameFormat(Player p) {
		if (Loader.tab.getString("Groups." + group(p) + ".Name") != null) {
			return replace(Loader.tab.getString("Groups." + group(p) + ".Name"), p);
		}
		return "%prefix% "+p.getName()+" %suffix%";
	}

	public static String getSuffix(Player p, boolean nametag) {
		if (Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Suffix") != null) {
			return replace(Loader.tab.getString("Groups." + group(p) + "."+(nametag?"NameTag":"TabList")+".Suffix"), p);
		}
		return null;
	}

	public static String replace(String header, Player p) {
		String customname = p.getName();
		String group = Loader.FormatgetGroup(p);
		if (Loader.vault != null)
			group = Loader.vault.getPrimaryGroup(p);
		if (p.getCustomName() != null)
			customname = p.getCustomName();
		String displayname = p.getName();
		if (p.getDisplayName() != null)
			displayname = p.getDisplayName();
		
		double hp = 0.0;
		try {
			hp=(double)p.getHealth();
		}catch(Exception e) {
			hp=(double)Ref.invoke(p, "getHealthScale");
		}
		return TheAPI.colorize(PlaceholderAPI.setPlaceholders(p, header
				
				.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), false))
				.replace("%colored_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true))
				.replace("%online%", TheAPI.getOnlinePlayers().size() + "")
				.replace("%max_players%", TheAPI.getMaxPlayers() + "")
				.replace("%ping%", Loader.getInstance.pingPlayer(p))
				.replace("%time%", new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
				.replace("%date%", new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
				.replace("%world%", p.getWorld().getName()).replace("%hp%", hp + "")
				.replace("%health%", hp+ "").replace("%food%", p.getFoodLevel() + "")
				.replace("%x%", p.getLocation().getBlockX() + "").replace("%y%", p.getLocation().getBlockY() + "")
				.replace("%z%", p.getLocation().getBlockZ() + "").replace("%vault-group%", group)
				.replace("%vault-prefix%", Loader.getInstance.getPrefix(p))
				.replace("%vault-suffix%", Loader.getInstance.getSuffix(p))
				.replace("%group%", getGroup(p))
				.replace("%tps%", TheAPI.getServerTPS() + "").replace("%ping%", Loader.getInstance.pingPlayer(p) + "")
				.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS))
				.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS))
				.replace("%player%", p.getName())
				.replace("%playername%", displayname).replace("%customname%", customname)
				.replace("%ram_free%", MemoryAPI.getFreeMemory(false) + "")
				.replace("%ram_free_percentage%", MemoryAPI.getFreeMemory(true) + "%")
				.replace("%ram_usage%", MemoryAPI.getUsedMemory(false) + "")
				.replace("%ram_usage_percentage%", MemoryAPI.getUsedMemory(true) + "%")
				.replace("%ram_max%", MemoryAPI.getMaxMemory() + "").replace("%ram_max_percentage%", "100%")																								// :D
				.replace("%afk%", Loader.getInstance.isAfk(p))));
	}

	public static void setNameTag(Player p) {
		String p1 = TabList.getPrefix(p, true);
		String p2 = TabList.getPrefix(p, false);
		String s1 = TabList.getSuffix(p, true);
		String s2 = TabList.getSuffix(p, false);
		String name = getNameFormat(p).replace("%prefix%", (p2!=null?TabList.replace(p2, p):"")).replace("%suffix%", (s2!=null?TabList.replace(s2, p):""));
		TabListAPI.setTabListName(p,name);
		NameTagChanger.setNameTag(p, p1!=null?TabList.replace(p1, p):"", s1!=null?TabList.replace(s1, p):"");
	}

	static int test;

	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			p.setPlayerListName(p.getName());
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
