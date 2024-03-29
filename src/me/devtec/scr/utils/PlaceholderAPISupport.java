package me.devtec.scr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.info.AFK;
import me.devtec.scr.commands.info.Ping;
import me.devtec.scr.commands.server_managment.TPS;
import me.devtec.scr.commands.server_managment.TPS.TPSType;
import me.devtec.scr.commands.teleport.home.HomeManager;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.MemoryAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.shared.utility.StringUtils.FormatType;
import net.milkbowl.vault.economy.Economy;

public class PlaceholderAPISupport {

	public static List<String> replace(List<String> text, CommandSender s) {
		List<String> replaced = new ArrayList<>();
		for (String t : text)
			replaced.add(replace(t, s, true, Placeholders.c()));
		return replaced;
	}

	public static String replace(String text, CommandSender s) {
		return replace(text, s, true, Placeholders.c());
	}

	public static String replace(String text, CommandSender s, boolean placeholderapi) {
		return replace(text, s, placeholderapi, Placeholders.c());
	}

	public static String replace(String text, CommandSender s, Placeholders placeholders) {
		return replace(text, s, true, placeholders == null ? Placeholders.c() : placeholders);
	}

	public static String replace(String text, CommandSender s, boolean placeholderapi, Placeholders placeholders) {
		if (text == null)
			return null;
		if (placeholders == null)
			placeholders = Placeholders.c();

		if (s instanceof Player) {
			Player p = (Player) s;
			User user = API.getUser(p);
			placeholders.addPlayer("player", p);

			// %money_symbol% - money symbol
			// %money% - vault format (probably with currency symbol?)
			// %money_raw% - is raw balance (1102100.540000614)
			// %money_raw_formatted% - is raw balance (1102100.54)
			// %money_formatted1% - is formatted balance v1 (1,102,100.54)
			// %money_formatted2% - is formatted balance v2(1.1M)

			if (text.contains("%money%"))
				placeholders.replace("money", Loader.economy == null ? "0" : ((Economy) Loader.economy).format(((Economy) Loader.economy).getBalance(p)));
			// text = text.replace("%money%",
			// ((Economy)Loader.economy).format(((Economy)Loader.economy).getBalance(p)) );
			if (text.contains("%money_symbol%"))
				placeholders.replace("money_symbol", Loader.economy == null ? "$" : ((Economy) Loader.economy).currencyNamePlural());
			if (text.contains("%money_raw%") || text.contains("%money_raw_formatted%") || text.contains("%money_formatted1%") || text.contains("%money_formatted2%"))
				text = ScrEconomy.publicFormat(Loader.economy == null ? 0 : ((Economy) Loader.economy).getBalance(p), text);
			// %world%
			// %world_custom% - custom name of world (for example you can repalce 'world'
			// into 'Survival' in placeholders.yml)
			// %x%
			// %y%
			// %z%
			// %yaw%
			// %pitch%
			if (text.contains("%world%"))
				placeholders.replace("world", p.getWorld().getName());
			if (text.contains("%world_custom%"))
				if (Loader.placeholders.exists("world_custom." + p.getWorld().getName()))
					placeholders.replace("world_custom", Loader.placeholders.getString("world_custom." + p.getWorld().getName()));
				else
					placeholders.replace("world_custom", p.getWorld().getName());
			if (text.contains("%x%"))
				placeholders.replace("x", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getX()));
			if (text.contains("%y%"))
				placeholders.replace("y", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getY()));
			if (text.contains("%z%"))
				placeholders.replace("z", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getZ()));
			if (text.contains("%yaw%"))
				placeholders.replace("yaw", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getYaw()));
			if (text.contains("%pitch%"))
				placeholders.replace("pitch", StringUtils.formatDouble(FormatType.BASIC, p.getLocation().getPitch()));

			// %ping%
			// %hp%
			// %health%
			// %food%
			// %kills%
			// %deaths%
			// %xp%
			// %exp%
			// %level%
			if (text.contains("%ping%"))
				placeholders.replace("ping", Ping.pingPlayer(p));

			// %home_count%
			// %home_limit%
			// %home_list% - list of homes

			if (text.contains("%home_count%"))
				placeholders.replace("home_count", HomeManager.homesOf(p.getUniqueId()));
			if (text.contains("%home_limit%"))
				placeholders.replace("home_limit", HomeManager.getLimit(p));
			if (text.contains("%home_max%"))
				placeholders.replace("home_max", HomeManager.getLimit(p));
			if (text.contains("%home_list%")) {
				StringBuilder homeNames = new StringBuilder();
				for (String home : HomeManager.homesOf(p.getUniqueId())) {
					if (homeNames.length() != 0)
						homeNames.append(Loader.translations.getString("home.list_split"));
					homeNames.append(home);
				}
				placeholders.replace("home_list", homeNames);
			}
			// %afk%
			// %vanish%
			// %god%
			// %fly%
			if (text.contains("%afk%"))
				if (AFK.players.containsKey(p.getName()))
					placeholders.add("afk", Loader.placeholders.getString("afk.enabled"));
				else
					placeholders.add("afk", Loader.placeholders.getString("afk.disabled"));
			if (text.contains("%god%"))
				if (user.god())
					placeholders.add("god", Loader.placeholders.getString("god.enabled"));
				else
					placeholders.add("god", Loader.placeholders.getString("god.disabled"));
			if (text.contains("%fly%"))
				if (user.fly())
					placeholders.add("fly", Loader.placeholders.getString("fly.enabled"));
				else
					placeholders.add("fly", Loader.placeholders.getString("fly.disabled"));

			// %online%
			// %online_max%
			// %time%
			// %date%
			if (text.contains("%online%"))
				placeholders.add("online", API.getOnlinePlayersFor(s).size());
			if (text.contains("%online_max%"))
				placeholders.add("online_max", Bukkit.getMaxPlayers());

			// %ram_free%
			// %ram_max%
			// %ram_used%
			// %tps%
			// %tps_1%
			// %tps_5%
			// %tps_15%
			if (text.contains("%ram_free%"))
				placeholders.add("ram_free", MemoryAPI.getFreeMemory(Loader.config.getBoolean("options.ram-percentage")));
			if (text.contains("%ram_max%"))
				placeholders.add("ram_max", MemoryAPI.getMaxMemory());
			if (text.contains("%ram_used%"))
				placeholders.add("ram_used", MemoryAPI.getUsedMemory(Loader.config.getBoolean("options.ram-percentage")));

			if (text.contains("%tps%"))
				placeholders.add("tps", TPS.getServerTPS());
			if (text.contains("%tps_1%"))
				placeholders.add("tps_1", TPS.getServerTPS(TPSType.ONE_MINUTE));
			if (text.contains("%tps_5%"))
				placeholders.add("tps_5", TPS.getServerTPS(TPSType.FIVE_MINUTES));
			if (text.contains("%tps_15%"))
				placeholders.add("tps_15", TPS.getServerTPS(TPSType.FIFTEEN_MINUTES));

			// %user_path%
			Pattern pat = Pattern.compile("%user:(.+?)%");
			Matcher mat = pat.matcher(text);
			if (mat.find()) {
				String path = mat.group(1);
				placeholders.add("user:" + path, me.devtec.shared.API.getUser(p.getName()).get(path));
			}

			/*
			 * text = me.devtec.shared.placeholders.PlaceholderAPI.apply(
			 * MessageUtils.placeholder(p, text, placeholders), s instanceof Player ?
			 * ((Player) s).getUniqueId() : null);
			 */
			if (placeholderapi)
				text = PlaceholderAPI.apply(MessageUtils.placeholder(p, text, placeholders), p.getUniqueId());
			else
				text = MessageUtils.placeholder(p, text, placeholders);
		}
		if (placeholders != null)
			text = MessageUtils.placeholder(s, text, placeholders);
		return text;
	}
}
