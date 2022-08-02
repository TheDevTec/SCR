package me.devtec.scr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.api.API;
import me.devtec.shared.components.ComponentAPI;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class MessageUtils {
	public static class Placeholders {
		private final Map<String, String> set = new HashMap<>();
		private final Map<String, Player> player_set = new HashMap<>();

		public Placeholders add(String placeholder, Object replace) {
			set.put(placeholder, replace + "");
			return this;
		}

		public static Placeholders c() {
			return new Placeholders();
		}

		public Placeholders replace(String placeholder, Object replace) {
			return add(placeholder, replace);
		}
		
		public Placeholders addPlayer(String placeholder, Player player) {
			player_set.put(placeholder, player);
			return this;
		}
		public Placeholders addPlayer(String placeholder, CommandSender player) {
			if(player instanceof Player)
				player_set.put(placeholder, Bukkit.getPlayer(player.getName()));
			else
				replace(placeholder, "CONSOLE");
			return this;
		}
	}

	public static String getPrefix() {
		return Loader.config.getString("prefix");
	}

	// Replacing placeholders in message
	public static String placeholder(CommandSender sender, String string, Placeholders placeholders) {
		if (getPrefix() != null)
			string = string.replace("%prefix%", getPrefix());
		if (placeholders != null) {
			if(!placeholders.player_set.isEmpty()) {
				for (Entry<String, Player> players : placeholders.player_set.entrySet()) {
					/* %playe%r, %target%, etc...
					 * %player% - Real or Nickname
					 * %player_name% - Just Real name
					 * %player_displayname%
					 * %player_customname%
					 */
					if(players.getKey().endsWith("_name")) {
						string = string.replace("%"+players.getKey()+"%", players.getValue().getName());
						continue;
						}
					if(players.getKey().endsWith("_displayname")) {
						string = string.replace("%"+players.getKey()+"%", players.getValue().getDisplayName());
					continue;
					}
					if(players.getKey().endsWith("_customname")) {
						string = string.replace("%"+players.getKey()+"%", players.getValue().getCustomName());
					continue;
					}
					string = string.replace("%"+players.getKey()+"%", API.getPlayerName(players.getValue()));
				}
				
			}
			if(sender instanceof Player && !placeholders.set.containsKey("player"))
				string = string.replace("%player%", API.getPlayerName(sender));
			for (Entry<String, String> placeholder : placeholders.set.entrySet())
				string = string.replace("%"+placeholder.getKey() + "%", placeholder.getValue() + "");
		}
		return string;
	}

	// Translation messages
	public static void message(CommandSender player, String path, Placeholders placeholders) {
		msgConfig(player, Loader.translations, path, placeholders, player);
	}

	public static void message(CommandSender player, String path, Placeholders placeholders, CommandSender... targets) {
		msgConfig(player, Loader.translations, path, placeholders, targets);
	}

	// Specific config messages
	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders) {
		msgConfig(player, config, path, placeholders, player);
	}

	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders, CommandSender... targets) {
		msgConfig(player, config, path, placeholders, targets);
	}

	// Other
	public static void msgConsole(String message) {
		Bukkit.getConsoleSender().sendMessage(StringUtils.colorize(placeholder(null, message, null)));
	}

	public static void msgConfig(CommandSender player, Config config, String path, Placeholders placeholders, CommandSender... targets) {
		Object text = config.get(path);

		if (text == null) {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + config.getFile().getName() + ", report this bug to the DevTec discord.");
			return;
		}

		if (text instanceof Collection) {
			if (config.isJson(path)) {
				String line = config.getString(path);
				String trimmed = line.trim();
				if (trimmed.equals("[]") || trimmed.equals("{}"))
					return; // Do not send empty json
				msgJson(player, line, placeholders, targets);
				return;
			}
			for (String line : config.getStringList(path))
				msg(player, line, placeholders, targets);
			return;
		}
		String line = config.getString(path);
		if (line.isEmpty())
			return; // Do not send empty strings
		msg(player, line, placeholders, targets);
	}

	@SuppressWarnings("unchecked")
	private static void msgJson(CommandSender s, String original, Placeholders placeholders, CommandSender[] targets) {
		Object json = Json.reader().simpleRead(original);
		List<Map<String, Object>> jsonList = new ArrayList<>();
		if (json instanceof Collection) {
			for (Object val : (Collection<?>) json)
				if (val instanceof Map)
					jsonList.add((Map<String, Object>) val);
				else {
					if (val.toString().isEmpty())
						continue; // You are trying to fix json by yourself?
					jsonList.addAll(ComponentAPI.toJsonList(ComponentAPI.fromString(val.toString())));
				}
		} else if (json instanceof Map)
			jsonList.add((Map<String, Object>) json);
		else
			return; // Bug?

		// PROCESS PLACEHOLDER & COLORS
		for (Map<String, Object> map : jsonList)
			replaceJson(s, map, placeholders);

		jsonList = ComponentAPI.fixJsonList(jsonList);
		Object packet = BukkitLoader.getNmsProvider().chatBase(Json.writer().simpleWrite(jsonList));

		for (CommandSender target : targets)
			if (target instanceof Player)
				BukkitLoader.getPacketHandler().send((Player) target, packet);
			else
				target.sendMessage(ComponentAPI.listToString(jsonList));
	}

	@SuppressWarnings("unchecked")
	private static void replaceJson(CommandSender s, Map<String, Object> map, Placeholders placeholders) {
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().equals("text")) {
				String text = entry.getValue() + "";

				text = placeholder(s, text, placeholders);

				/*
				 * int pos = 0; for(Object placeholder : placeholders) { text =
				 * text.replace("{"+(pos++)+"}", placeholder.toString()); }
				 */
				text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null));
				entry.setValue(text);
				continue;
			}
			if (entry.getKey().equals("hoverEvent") || entry.getKey().equals("clickEvent")) {
				replaceJson(s, (Map<String, Object>) entry.getValue(), placeholders);
				continue;
			}
			if (entry.getKey().equals("contents") || entry.getKey().equals("value")) {
				if (entry.getValue() instanceof Map) {
					replaceJson(s, (Map<String, Object>) entry.getValue(), placeholders);
					continue;
				}
				if (entry.getValue() instanceof List) {
					List<Object> col = (List<Object>) entry.getValue();
					ListIterator<Object> itr = col.listIterator();
					while (itr.hasNext()) {
						Object val = itr.next();
						if (val instanceof Map)
							replaceJson(s, (Map<String, Object>) val, placeholders);
						else {
							String text = val + "";

							text = placeholder(s, text, placeholders);

							/*
							 * int pos = 0; for(Object placeholder : placeholders) { text =
							 * text.replace("{"+(pos++)+"}", placeholder.toString()); }
							 */
							text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null));
							itr.set(text);
						}
					}
					continue;
				}
				String text = entry.getValue() + "";

				text = placeholder(s, text, placeholders);

				/*
				 * int pos = 0; for(Object placeholder : placeholders) { text =
				 * text.replace("{"+(pos++)+"}", placeholder.toString()); }
				 */
				text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null));
				entry.setValue(text);
			}
		}
	}

	private static void msg(CommandSender s, String original, Placeholders placeholders, CommandSender[] targets) {
		String text = original;
		text = placeholder(s, text, placeholders);
		/*
		 * int pos = 0; for(Object placeholder : placeholders) { text =
		 * text.replace("{"+(pos++)+"}", placeholder.toString()); }
		 */
		for (CommandSender target : targets)
			target.sendMessage(StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null)));
	}

	public static void noPerm(CommandSender player, String permission) {
		message(player, "noPerms", Placeholders.c().replace("permission", permission));
	}
}
