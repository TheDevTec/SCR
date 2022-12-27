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

import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.components.ComponentAPI;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.nms.NmsProvider.ChatType;

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
			if (player != null && placeholder != null)
				player_set.put(placeholder, player);
			return this;
		}

		public Placeholders addPlayer(String placeholder, CommandSender player) {
			if (player == null || placeholder == null)
				return this;
			if (player instanceof Player)
				player_set.put(placeholder, (Player) player);
			else
				replace(placeholder, "CONSOLE");
			return this;
		}

		public Placeholders addOffline(String palceholder, String player) {
			add(palceholder, player);
			add(palceholder + "_name", player);
			add(palceholder + "_displayname", player);
			add(palceholder + "_customname", player);
			return this;
		}
	}

	public static String getPrefix() {
		return Loader.config.getString("prefix");
	}

	public static List<String> placeholder(CommandSender sender, List<String> string, Placeholders placeholders) {
		List<String> clone = new ArrayList<>(string);
		clone.replaceAll(s -> placeholder(sender, s, placeholders));
		return clone;
	}

	// Replacing placeholders in message
	public static String placeholder(CommandSender sender, String string, Placeholders placeholders) {
		if (getPrefix() != null)
			string = string.replace("%prefix%", getPrefix());
		if (placeholders != null) {
			if (!placeholders.player_set.isEmpty())
				for (Entry<String, Player> players : placeholders.player_set.entrySet()) {
					/*
					 * %playe%r, %target%, etc... %player% - Real or Nickname %player_name% - Just
					 * Real name %player_displayname% %player_customname%
					 */
					if (string.contains("%" + players.getKey() + "_name%")) {
						string = string.replace("%" + players.getKey() + "_name%", players.getValue().getName());
						continue;
					}
					if (string.contains("%" + players.getKey() + "_displayname%")) {
						string = string.replace("%" + players.getKey() + "_displayname%", players.getValue().getDisplayName());
						continue;
					}
					if (string.contains("%" + players.getKey() + "_customname%")) {
						string = string.replace("%" + players.getKey() + "_customname%", players.getValue().getCustomName());
						continue;
					}
					string = string.replace("%" + players.getKey() + "%", players.getValue().getName());
				}
			if (sender instanceof Player && !placeholders.set.containsKey("player"))
				string = string.replace("%player%", sender.getName());
			for (Entry<String, String> placeholder : placeholders.set.entrySet())
				string = string.replace("%" + placeholder.getKey() + "%", placeholder.getValue() + "");
		}
		return string;
	}

	// Translation messages
	public static void message(CommandSender player, String path, Placeholders placeholders) {
		if (Loader.translations.exists(path))
			msgConfig(player, Loader.translations, path, placeholders, true, player);
		else {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + Loader.translations.getFile().getName() + ", please complete your translation.");
			msgConfig(player, Loader.engtrans, path, placeholders, true, player);
		}
	}

	public static void message(CommandSender player, String path, Placeholders placeholders, boolean split) {
		if (Loader.translations.exists(path))
			msgConfig(player, Loader.translations, path, placeholders, split, player);
		else {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + Loader.translations.getFile().getName() + ", please complete your translation.");
			msgConfig(player, Loader.engtrans, path, placeholders, split, player);
		}
	}

	public static void message(CommandSender player, String path, Placeholders placeholders, CommandSender... targets) {
		if (Loader.translations.exists(path))
			msgConfig(player, Loader.translations, path, placeholders, true, targets);
		else {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + Loader.translations.getFile().getName() + ", please complete your translation.");
			msgConfig(player, Loader.engtrans, path, placeholders, true, targets);
		}
	}

	public static void message(CommandSender player, String path, Placeholders placeholders, boolean split, CommandSender... targets) {
		if (Loader.translations.exists(path))
			msgConfig(player, Loader.translations, path, placeholders, split, targets);
		else {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + Loader.translations.getFile().getName() + ", please complete your translation.");
			msgConfig(player, Loader.engtrans, path, placeholders, split, targets);
		}
	}

	// Specific config messages
	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders) {
		msgConfig(player, config, path, placeholders, true, player);
	}

	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders, boolean split) {
		msgConfig(player, config, path, placeholders, split, player);
	}

	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders, CommandSender... targets) {
		msgConfig(player, config, path, placeholders, true, targets);
	}

	public static void msgConfig(CommandSender player, String path, Config config, Placeholders placeholders, boolean split, CommandSender... targets) {
		msgConfig(player, config, path, placeholders, split, targets);
	}

	// Other
	public static void msgConsole(String message, Placeholders placehholders) {
		Bukkit.getConsoleSender().sendMessage(StringUtils.colorize(placeholder(null, message, placehholders)));
	}

	public static void sendAnnoucment(String message, CommandSender... targets) { // AutoMessage annoucments
		boolean split = true;
		if (targets == null)
			return;
		if (message.startsWith("[") && message.endsWith("]") || message.startsWith("{") && message.endsWith("}")) {
			String trimmed = message.trim();
			if (trimmed.equals("[]") || trimmed.equals("{}"))
				return; // Do not send empty json
			msgJson(Bukkit.getConsoleSender(), message, null, targets);
			return;
		}

		if (message.isEmpty())
			return; // Do not send empty strings
		msg(Bukkit.getConsoleSender(), message, null, split, targets);
	}

	public static void msgConfig(CommandSender player, Config config, String path, Placeholders placeholders, boolean split, CommandSender... targets) {
		if (player == null)
			return;

		Object text = config.get(path);

		if (text == null) {
			Loader.plugin.getLogger().warning("Path " + path + " not found in config " + config.getFile().getName() + ", report this bug to the DevTec discord.");
			return;
		}

		if (text instanceof Collection || text instanceof Map) {
			if (config.isJson(path)) {
				String line = config.getString(path);
				String trimmed = line.trim();
				if (trimmed.equals("[]") || trimmed.equals("{}"))
					return; // Do not send empty json
				msgJson(player, line, placeholders, targets);
				return;
			}
			for (String line : config.getStringList(path))
				msg(player, line, placeholders, split, targets);
			return;
		}
		String line = config.getString(path);
		if (line.isEmpty())
			return; // Do not send empty strings
		msg(player, line, placeholders, split, targets);
	}

	@SuppressWarnings("unchecked")
	private static void msgJson(CommandSender s, String original, Placeholders placeholders, CommandSender... targets) {
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
		// Replacing to minecraft json
		jsonList = ComponentAPI.fixJsonList(jsonList);
		// Sending packet
		String written = Json.writer().simpleWrite(jsonList);
		written = "[\"\", " + written.substring(1);
		Object chat = BukkitLoader.getNmsProvider().chatBase(written);

		Object packet = BukkitLoader.getNmsProvider().packetChat(ChatType.SYSTEM, chat);
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
							text = StringUtils.colorize(PlaceholderAPISupport.replace(text, s, true, null));
							// text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ?
							// ((Player) s).getUniqueId() : null));
							itr.set(text);
						}
					}
					continue;
				}
				String text = entry.getValue() + "";

				text = placeholder(s, text, placeholders);
				text = StringUtils.colorize(PlaceholderAPISupport.replace(text, s, true, null));
				// text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ?
				// ((Player) s).getUniqueId() : null));
				entry.setValue(text);
			}
		}
	}

	private static void msg(CommandSender s, String original, Placeholders placeholders, boolean split, CommandSender... targets) {
		String text = original;
		text = StringUtils.colorize(placeholder(s, text, placeholders));
		if (split) {
			String lastcolor = null;
			for (String line : text.replace("\\n", "\n").split("\n")) {
				if (lastcolor != null && lastcolor.length() == 1)
					lastcolor = "&" + lastcolor;
				if (lastcolor != null && lastcolor.length() == 7) { // HEX
					lastcolor = "&" + lastcolor;
					lastcolor = lastcolor.replace("&x", "#");
				}
				if (lastcolor != null && lastcolor.length() > 7) {
					StringBuilder build = new StringBuilder();
					for (String c : lastcolor.split(""))
						build.append("&").append(c);
					lastcolor = build.toString();
				}
				String coloredText = StringUtils.colorize(PlaceholderAPISupport.replace(lastcolor == null ? line : lastcolor + "" + line, s, true, null));
				Object packet = BukkitLoader.getNmsProvider().packetChat(ChatType.SYSTEM, ComponentAPI.fromString(coloredText));
				for (CommandSender target : targets)
					if (target instanceof Player)
						BukkitLoader.getPacketHandler().send((Player) target, packet);
					else
						target.sendMessage(coloredText);
				lastcolor = StringUtils.getLastColors(StringUtils.colorize(line));
			}
		} else {
			String coloredText = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null));
			Object packet = BukkitLoader.getNmsProvider().packetChat(ChatType.SYSTEM, ComponentAPI.fromString(coloredText));
			for (CommandSender target : targets)
				if (target instanceof Player)
					BukkitLoader.getPacketHandler().send((Player) target, packet);
				else
					target.sendMessage(coloredText);
		}
	}

	public static void noPerm(CommandSender player, String permission) {
		message(player, "noPerms", Placeholders.c().replace("permission", permission));
	}
}
