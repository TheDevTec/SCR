package me.DevTec.ServerControlReloaded.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import me.DevTec.ServerControlReloaded.Commands.Info.Staff;
import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Item;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.MemoryAPI;
import me.devtec.theapi.apis.TabListAPI;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class TabList {
	// GROUP, PRIORITE
	private static HashMap<String, String> sorting = new HashMap<>();

	public static void reload() {
		sorting.clear();
		List<String> priorites = generate(Loader.tab.getKeys("Groups").size());
		int current = 0;
		for(String a : Loader.tab.getKeys("Groups"))
			sorting.put(priorites.get(current++), a);
		
	}
	
	//limit 1000 groups
	private static List<String> generate(int size) {
		List<String> a = new ArrayList<>();
		for(int i = 0; i < size; ++i) {
			String s = "";
			int limit = 4-(i+"").length();
			if(limit > 0)
			for(int d = 0; d < limit; ++d)
				s+="0";
			s+=i;
			a.add(s);
		}
		return a;
	}

	public static String getPrefix(Player p, boolean nametag) {
		if (Loader.tab.exists("Groups." + Staff.getGroup(p) + "."+(nametag?"NameTag":"TabList")+".Prefix"))
			return replace(Loader.tab.getString("Groups." + Staff.getGroup(p) + "."+(nametag?"NameTag":"TabList")+".Prefix"), p, true);
		return null;
	}

	public static String getNameFormat(Player p) {
		if (Loader.tab.exists("Groups." + Staff.getGroup(p) + ".Format"))
			return replace(Loader.tab.getString("Groups." + Staff.getGroup(p) + ".Format"), p, true);
		return "%tab_prefix% "+p.getName()+" %tab_suffix%";
	}

	public static String getSuffix(Player p, boolean nametag) {
		if (Loader.tab.exists("Groups." + Staff.getGroup(p) + "."+(nametag?"NameTag":"TabList")+".Suffix"))
			return replace(Loader.tab.getString("Groups." + Staff.getGroup(p) + "."+(nametag?"NameTag":"TabList")+".Suffix"), p, true);
		return null;
	}

	public static String replace(String header, Player p, boolean color) {
		if(color)
		header=TheAPI.colorize(header);
		if(p!=null) {
		if(header.contains("%money%"))
				header=header.replace("%money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), false));
		if(header.contains("%colored_money%"))
			header=header.replace("%colored_money%", API.setMoneyFormat(EconomyAPI.getBalance(p.getName()), true));
		if(header.contains("%online%")) {
			List<Player> seen = Lists.newArrayList();
			for(Player s : TheAPI.getPlayers())
				if(TheAPI.canSee(p,s.getName()) || s == p)
					seen.add(s);
			header=header.replace("%online%", seen.size() + "");
		}
		if(header.contains("%ping%"))
			header=header.replace("%ping%", Loader.getInstance.pingPlayer(p));
		;if(header.contains("%world%"))
			header=header.replace("%world%", p.getWorld().getName())
		;if(header.contains("%hp%"))
			header=header.replace("%health%", String.format("%2.02f", ((Damageable)p).getHealth()).replace(",00", "").replace(",", "."));
		if(header.contains("%health%"))
			header=header.replace("%health%", String.format("%2.02f", ((Damageable)p).getHealth()).replace(",00", "").replace(",", "."));
		if(header.contains("%food%"))
			header=header.replace("%food%", p.getFoodLevel() + "")
		;if(header.contains("%x%"))
			header=header.replace("%x%", p.getLocation().getBlockX() + "");
		if(header.contains("%y%"))header=header.replace("%y%", p.getLocation().getBlockY() + "")
		;if(header.contains("%z%"))
			header=header.replace("%z%", p.getLocation().getBlockZ() + "");
		if(header.contains("%vault_group%")) {
			String group = Loader.get(p, Item.GROUP);
		if (Loader.vault != null)
			group = Loader.vault.getPrimaryGroup(p);
			header=header.replace("%vault_group%", group);
		}
		if(header.contains("%vault_prefix%"))
			header=header.replace("%vault_prefix%", Loader.get(p, Item.PREFIX))
		;if(header.contains("%vault_suffix%"))
			header=header.replace("%vault_suffix%", Loader.get(p, Item.SUFFIX))
		;if(header.contains("%group%"))
			header=header.replace("%group%", Staff.getGroup(p));
		if(header.contains("%kills%"))
			header=header.replace("%kills%", "" + p.getStatistic(Statistic.PLAYER_KILLS))
		;if(header.contains("%deaths%"))
			header=header.replace("%deaths%", "" + p.getStatistic(Statistic.DEATHS))
		;if(header.contains("%player%"))
			header=header.replace("%player%", p.getName())
		;if(header.contains("%xp%"))
			header=header.replace("%xp%", p.getTotalExperience()+"");
		if(header.contains("%level%"))
			header=header.replace("%level%", p.getLevel()+"");
		if(header.contains("%exp%"))
			header=header.replace("%exp%", p.getTotalExperience()+"")
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
		String p1 = getPrefix(p, true), p2 = getPrefix(p, false), s1 = getSuffix(p, true), s2 = getSuffix(p, false);
		String name = getNameFormat(p).replace("%tab_prefix%", (p2!=null?replace(p2, p, true):"")).replace("%tab_suffix%", (s2!=null?replace(s2, p, true):""));
		p.setPlayerListName(AnimationManager.replace(p,name));
		NameTagChanger.setNameTag(p, p1!=null?replace(p1, p, true):"", s1!=null?replace(s1, p, true):"");
	}
	
	static int test;
	public static void removeTab() {
		for (Player p : TheAPI.getOnlinePlayers()) {
			NameTagChanger.remove(p);
			TabListAPI.setTabListName(p,p.getName());
			Ref.sendPacket(p, NMSAPI.getPacketPlayOutPlayerListHeaderFooter(Ref.IChatBaseComponent(""), Ref.IChatBaseComponent("")));
		}
	}

	private static String get(String path, Player p) {
		if (setting.tab_header || setting.tab_footer) {
			if (!Loader.tab.getStringList(path).isEmpty()) {
				String a = StringUtils.join(Loader.tab.getStringList(path), "\n");
				return replace(a, p, true);
			}
		}
		return "";
	}

	private static String getPath(Player p, String what) {
		if (what.equalsIgnoreCase("footer") && setting.tab_footer
				|| what.equalsIgnoreCase("header") && setting.tab_header) {
			if (Loader.tab.exists("PerPlayer." + p.getName() + "." + what))
				return get("PerPlayer." + p.getName() + "." + what, p);
			else if (Loader.tab.exists("PerWorld." + p.getWorld().getName() + "." + what))
				return get("PerWorld." + p.getWorld().getName() + "." + what, p);
			else {
				return get(what, p);
			}
		}
		return "";
	}

	public static void setFooterHeader(Player p) {
		TabListAPI.setHeaderFooter(p, AnimationManager.replace(p,getPath(p, "Header")), AnimationManager.replace(p,getPath(p, "Footer")));
	}
}
