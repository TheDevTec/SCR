package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class TabList {
	private static String group(Player p) {
		if(API.existVaultPlugin()) {
			if(Loader.perms != null && Loader.vault != null)
			if(Loader.perms.getPrimaryGroup(p) != null) {
				return Loader.perms.getPrimaryGroup(p);
			}
			return "default";
		}
		return "default";
	}

	public static String getGroup(Player p) {
		if(Loader.tab.getString("Groups."+group(p)+".Priorite") != null) {
			return Loader.tab.getString("Groups."+group(p)+".Priorite");
		}
		return "z";
	}
	public static String getPrefix(Player p) {
			if(Loader.tab.getString("Groups."+group(p)+".Prefix")!=null) {
			return replace(Loader.tab.getString("Groups."+group(p)+".Prefix"),p);
	}
		return Loader.getInstance.getPrefix(p);
	}
	public static String getSuffix(Player p) {
		if(Loader.tab.getString("Groups."+group(p)+".Suffix")!=null) {
		return replace(Loader.tab.getString("Groups."+group(p)+".Suffix"),p);
}
		return Loader.getInstance.getSuffix(p);
}
	public static String replace(String header, Player p) {
		header=TheAPI.getPlaceholderAPI().setPlaceholders(p, header);
		String customname = p.getName();
		String group = Loader.FormatgetGroup(p);
		if(Loader.vault!=null)
			group=Loader.vault.getPrimaryGroup(p);
		if(p.getCustomName()!=null)customname=p.getCustomName();
		String displayname = p.getName();
		if(p.getDisplayName()!=null)displayname=p.getDisplayName();
		return header.replace("%money%", API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p.getName()), true))
				.replace("%online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
				.replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()))
				.replace("%ping%", String.valueOf(Loader.getInstance.pingPlayer(p)))
				.replace("%time%", String.valueOf( new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date())))
				.replace("%date%", String.valueOf( new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date())))
				.replace("%world%", p.getWorld().getName())
				.replace("%hp%", String.valueOf(p.getHealth()))
				.replace("%health%", String.valueOf(p.getHealth()))
				.replace("%food%", String.valueOf(p.getFoodLevel()))
				.replace("%x%", String.valueOf(p.getLocation().getBlockX()))
				.replace("%y%", String.valueOf(p.getLocation().getBlockY()))
				.replace("%z%", String.valueOf(p.getLocation().getBlockZ()))
				.replace("%vault-group%", group)
				.replace("%vault-prefix%", Loader.getInstance.getPrefix(p))
				.replace("%prefix%", Loader.getInstance.getPrefix(p))
				.replace("%vault-suffix%", Loader.getInstance.getSuffix(p))
				.replace("%suffix%", Loader.getInstance.getSuffix(p))
				.replace("%group%", getGroup(p))
				.replace("%tps%", TheAPI.getServerTPS()+"")
				.replace("%ping%", Loader.getInstance.pingPlayer(p)+"")
				.replace("%kills%", String.valueOf(Loader.me.getInt("Players."+p.getName()+".Kills")))
				.replace("%player%", p.getName())
				.replace("%playername%", displayname)
				.replace("%customname%", customname)
				.replace("%ram_free%",TheAPI.getMemoryAPI().getFreeMemory(false)+"")
				.replace("%ram_free_percentage%", TheAPI.getMemoryAPI().getFreeMemory(true)+"%")
				.replace("%ram_usage%", TheAPI.getMemoryAPI().getUsedMemory(false)+"")
				.replace("%ram_usage_percentage%", TheAPI.getMemoryAPI().getUsedMemory(true)+"%")
				.replace("%ram_max%", TheAPI.getMemoryAPI().getMaxMemory()+"")
				.replace("%ram_max_percentage%", "100%") //lol, why ? :D
				.replace("%afk%", Loader.getInstance.isAfk(p));
	}

	public static void setTab(Player p) {
		String prefix = TheAPI.colorize(TabList.replace(TabList.getPrefix(p), p));
		String suffix = TheAPI.colorize(TabList.replace(TabList.getSuffix(p), p));
		TheAPI.getTabListAPI().setTabListName(p, prefix + p.getName() + suffix);
          NameTagChanger.setNameTag(p,prefix,suffix);
	}
	static int test;
	public static void removeTab() {
		 for(Player p:TheAPI.getOnlinePlayers()) {
			 NameTagChanger.remove(p);
			 p.setPlayerListName(p.getName());
		p.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		TheAPI.getTabListAPI().setHeaderFooter(p, "", "");
	}}
	static boolean ex(String s) {
		if(Loader.tab.getString(s)!=null)return true;
		return false;
	}
	
	static String setHeaderFooter(String FooterOrHeader, String path, Player p) {
		if(FooterOrHeader.equalsIgnoreCase("header") && setting.tab_header || FooterOrHeader.equalsIgnoreCase("footer") && setting.tab_footer) {
		if(Loader.tab.getStringList(path)!=null) {
			List<String> L = Loader.tab.getStringList(path);
			L=TheAPI.getPlaceholderAPI().setPlaceholders(p, L);
			return replace(StringUtils.join(L, "\n"),p);
	}}
		return null;
	}
	public static String getHeader(Player p) {
		if(setting.tab_footer && setting.tab_header) {
		if(ex("PerPlayerTabList."+p.getName()+".Header"))
			return setHeaderFooter("Header","PerPlayerTabList."+p.getName()+".Header", p);
	else
			if(ex("PerWorldTabList."+p.getWorld().getName()+".Header"))
				return setHeaderFooter("Header","PerWorldTabList."+p.getWorld().getName()+".Header", p);
		else {
			return setHeaderFooter("Header","Header", p);
		}}
			return "";
	}
	public static String getFooter(Player p) {
		if(setting.tab_footer && setting.tab_header) {
		if(ex("PerPlayerTabList."+p.getName()+".Footer"))
			return setHeaderFooter("Footer","PerPlayerTabList."+p.getName()+".Footer", p);
		else
			if(ex("PerWorldTabList."+p.getWorld().getName()+".Footer"))
				return setHeaderFooter("Footer","PerWorldTabList."+p.getWorld().getName()+".Footer", p);
		else {
			return setHeaderFooter("Footer","Footer", p);
		}}
			return "";
	}
    
	public static void setNameTag() {
		for(Player p:TheAPI.getOnlinePlayers()) {
			if(TheAPI.getPlayer(p.getName())!=null) {
			setTab(p);
		}}
	}
	public static void setFooterHeader() {
		for(Player p:TheAPI.getOnlinePlayers()) {
			if(TheAPI.getPlayer(p.getName())!=null) {
				TheAPI.getTabListAPI().setHeaderFooter(p, getHeader(p), getFooter(p));
		}}}
}
