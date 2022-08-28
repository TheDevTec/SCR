package me.devtec.scr.listeners.additional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.utils.ChatUtils;
import me.devtec.scr.utils.ChatUtils.ChatFormat;
import me.devtec.scr.utils.ChatUtils.Colors;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.components.Component;
import me.devtec.shared.components.ComponentAPI;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.nms.NmsProvider.ChatType;

public class ChatListeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void chatFormat(AsyncPlayerChatEvent e) {
		if (e.isCancelled())
			return;
		Player player = e.getPlayer();
		User u = API.getUser(player);

		String message = e.getMessage();
		// ChatLock
		if (ChatUtils.isChatLocked() && !u.isAutorized(Loader.commands.getString("chatlock.permission.bypass"))) {
			e.setCancelled(true);
			if (Loader.translations.exists("chatlock.isLocked"))
				MessageUtils.message(player, "chatlock.isLocked", null, true);
			MessageUtils.message(player, "chatlock.format", Placeholders.c().addPlayer("player", player).add("message", message), true,
					API.getOnlinePlayersWith(Loader.commands.getString("chatlock.permission.bypass")).toArray(new CommandSender[0]));
			return;
		}

		// CooldownCheck

		// ChatFormat
		if (ChatFormat.isEnabled()) {

			// ChatType loading variables
			Iterator<Player> targets = e.getRecipients().iterator();
			String type = Loader.chat.getString("options.type");
			double distance = Loader.chat.getDouble("options.distance");

			List<String> worlds = new ArrayList<>();
			if (type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
				for (String path : Loader.chat.getKeys("options.per_world"))
					if (Loader.chat.getStringList("options.per_world." + path).contains(player.getLocation().getWorld().getName()))
						worlds = Loader.chat.getStringList("options.per_world." + path);
				if (worlds.isEmpty())
					worlds.add(player.getLocation().getWorld().getName());
			}

			// ChatType - removing players
			while (targets.hasNext()) {
				Player target = targets.next();
				// sender je target
				// TODO - ignore

				if (target.equals(player) || target.hasPermission("SCR.Other.ChatTypeBypass"))
					continue; // TODO - permise do configu

				// PER_WORLD type
				if (type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
					if (!worlds.contains(target.getLocation().getWorld().getName())) // If group of worlds contains target world
						target.remove();
					continue;
				}

				// DISTANCE
				if (type.equalsIgnoreCase("DISTANCE")) {
					if (!player.getWorld().equals(target.getWorld()) || target.getLocation().distance(player.getLocation()) > distance) // If they are not in same world
						target.remove();
					continue;
				}
			}

			// TODO - flood, antispam, ...
			
			String format_path = ChatFormat.getPath(player);
			
			if(Loader.chat.exists(format_path+".message")) // Adding config message color
				message = StringUtils.colorize( PlaceholderAPISupport.replace(Loader.chat.getString(format_path+".message"), player))
				.replace("%message%", message);
			
			message = colors(message, player);
			List<String> ignoredStrings = new ArrayList<>();
			for (Player p : BukkitLoader.getOnlinePlayers())
				if (!p.getUniqueId().equals(e.getPlayer().getUniqueId()))
					ignoredStrings.add(p.getName());
			
			//Chat notigications
			if (ChatUtils.Notification.isEnabled())
				message = ChatUtils.Notification.notificationReplace(player, message, e.getRecipients());
				//message = notificationReplace(player, message, "§c");

			if (message != null) {
				e.setMessage(message);
				setFormat(player, format_path, 
						Placeholders.c().addPlayer("player", player).add("message", message.replace("%", "%%")).add("playername",
						StringUtils.colorize(PlaceholderAPISupport.replace(Loader.chat.getString(format_path + ".playername"), player))), e);

			}

		}
	}

	public static String colors(String msg, Player p) {
		return Colors.colorize(msg, false, p);
	}

	// path - path.chat
	public static void setFormat(CommandSender player, String path, Placeholders placeholders, AsyncPlayerChatEvent event) {
		if (player == null)
			return;

		Object text = Loader.chat.get(path + ".format");

		if (text instanceof Collection || text instanceof Map) {
			if (Loader.chat.isJson(path + ".format")) {
				String line = Loader.chat.getString(path + ".format");
				String trimmed = line.trim();
				if (trimmed.equals("[]") || trimmed.equals("{}"))
					return; // Do not send empty json
				msgJson(player, line, placeholders, event);
				return;
			}
			for (String line : Loader.chat.getStringList(path + ".format"))
				msg(player, line, placeholders, event);
			return;
		}
		String line = Loader.chat.getString(path + ".format");
		if (line.isEmpty())
			return; // Do not send empty strings
		msg(player, line, placeholders, event);
	}

	// original - from chat.yml - chat
	@SuppressWarnings("unchecked")
	private static void msgJson(CommandSender s, String original, Placeholders placeholders, AsyncPlayerChatEvent event) {
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
		//Replacing to minecraft json
		jsonList = fixJsonList(jsonList);
		//Setting format
		event.setFormat(ComponentAPI.listToString(jsonList));
		//Sending packet
		Object packet = BukkitLoader.getNmsProvider().chatBase(Json.writer().simpleWrite(jsonList));
		BukkitLoader.getPacketHandler().send(event.getRecipients(),
				BukkitLoader.getNmsProvider().packetChat(ChatType.SYSTEM, packet) );
		
		event.getRecipients().clear();
	}

	// original - from chat.yml - chat
	@SuppressWarnings("unchecked")
	private static void replaceJson(CommandSender s, Map<String, Object> map, Placeholders placeholders) {
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().equals("text")) {
				String text = entry.getValue() + "";

				text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null));
				text = MessageUtils.placeholder(s, text, placeholders);
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

							text = StringUtils.colorize(PlaceholderAPISupport.replace(text, s, true, null));
							text = MessageUtils.placeholder(s, text, placeholders);
							itr.set(text);
						}
					}
					continue;
				}
				String text = entry.getValue() + "";

				text = StringUtils.colorize(PlaceholderAPISupport.replace(text, s, true, null));
				text = MessageUtils.placeholder(s, text, placeholders);
				entry.setValue(text);
			}
		}
	}

	// original - from chat.yml - chat
	private static void msg(CommandSender s, String original, Placeholders placeholders, AsyncPlayerChatEvent event) {
		String text = original;
		// first replacing chat palceholders, then adding %message% from player
		text = MessageUtils.placeholder(s, StringUtils.colorize(PlaceholderAPISupport.replace(text, s)), placeholders);
		event.setFormat(text);
	}
	
	//From TheAPI componentAPI
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> fixJsonList(List<Map<String, Object>> lists) { // usable for ex. chat format
		if (lists == null)
			return null;
		ListIterator<Map<String, Object>> it = lists.listIterator();
		while (it.hasNext()) {
			Map<String, Object> text = it.next();
			Map<String, Object> hover = (Map<String, Object>) text.get("hoverEvent");
			Map<String, Object> click = (Map<String, Object>) text.get("clickEvent");
			if (hover != null)
				hover = convertMapValues("hoverEvent", hover);
			if (click != null)
				click = convertMapValues("clickEvent", click);
			String interact = (String) text.get("insertion");
			boolean remove = false;
			for (Entry<String, Object> s : text.entrySet()) {
				if (s.getKey().equals("color") || s.getKey().equals("insertion"))
					continue;
				if (s.getValue() instanceof String) {
					Component c = ComponentAPI.fromString((String) s.getValue(), true);
					if (c.getText() != null && !c.getText().isEmpty() || c.getExtra() != null) {
						try {
							if (!remove) {
								it.remove();
								remove = true;
							}
						} catch (Exception err) {
						}
						Map<String, Object> d = c.toJsonMap();
						if (!d.containsKey("color") && text.containsKey("color"))
							d.put("color", text.get("color"));
						if (hover != null && !d.containsKey("hoverEvent"))
							d.put("hoverEvent", hover);
						if (click != null && !d.containsKey("clickEvent"))
							d.put("clickEvent", click);
						if (interact != null && !d.containsKey("insertation"))
							d.put("insertion", interact);
						it.add(d);
						if (c.getExtra() != null)
							fixJsonListAll(it, c.getExtra());
					}

				} else if (s.getValue() instanceof Map) // hoverEvent or clickEvent
					text.put(s.getKey(), s.getValue());
				else if (s.getValue() instanceof List) // extras
					text.put(s.getKey(), fixJsonList((List<Map<String, Object>>) s.getValue()));
			}
		}
		return lists;
	}
	
	private static void fixJsonListAll(ListIterator<Map<String, Object>> list, List<Component> extra) {
		for (Component c : extra) {
			list.add(c.toJsonMap());
			if (c.getExtra() != null)
				fixJsonListAll(list, c.getExtra());
		}
	}
	
	private static Map<String, Object> convertMapValues(String key, Map<String, Object> hover) {
		Object val = hover.getOrDefault("value", hover.getOrDefault("content", hover.getOrDefault("contents", null)));
		if (val == null)
			hover.put("value", "");
		else if (key.equalsIgnoreCase("hoverEvent")) {
			if (val instanceof Collection || val instanceof Map) {
				Object ac = hover.get("action");
				hover.clear();
				hover.put("action", ac);
				hover.put("value", val);
			} else {
				Object ac = hover.get("action");
				hover.clear();
				hover.put("action", ac);
				hover.put("value", ComponentAPI.toJsonList(val + ""));
			}
		} else if (val instanceof Collection || val instanceof Map) {
			Object ac = hover.get("action");
			hover.clear();
			hover.put("action", ac);
			hover.put("value", val);
		} else {
			Object ac = hover.get("action");
			hover.clear();
			hover.put("action", ac);
			hover.put("value", val + "");
		}
		return hover;
	}
}
