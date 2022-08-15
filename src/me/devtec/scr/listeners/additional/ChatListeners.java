package me.devtec.scr.listeners.additional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

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
import me.devtec.shared.components.ComponentAPI;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class ChatListeners implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void chatFormat(AsyncPlayerChatEvent e) {
		if(e.isCancelled()) return;
		Player player = e.getPlayer();
		User u = API.getUser(player);
		
		//CooldownCheck
		
		//ChatFormat
		if(ChatFormat.isEnabled()) {
			String message = e.getMessage();
			//ChatLock
			if(ChatUtils.isChatLocked() && 
					!u.isAutorized(Loader.commands.getString("chatlock.permission.bypass"))) {
				e.setCancelled(true);
				if(Loader.translations.exists("chatlock.isLocked"))
					MessageUtils.message(player, "chatlock.isLocked", null, true);
				MessageUtils.message(player, "chatlock.format", Placeholders.c().addPlayer("player", player)
						.add("message", message), true,
						API.getOnlinePlayersWith(Loader.commands.getString("chatlock.permission.bypass") ).toArray(new CommandSender[0]) );
				return;
			}
			
			//ChatType loading variables
			Iterator<Player> targets = e.getRecipients().iterator();
			String type = Loader.chat.getString("options.type");
			double distance = Loader.chat.getDouble("options.distance");
			
			List<String> worlds = new ArrayList<>();
			if(type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
				for(String path : Loader.chat.getKeys("options.per_world"))
					if(Loader.chat.getStringList("options.per_world."+path)
							.contains(player.getLocation().getWorld().getName()))
						worlds = Loader.chat.getStringList("options.per_world."+path);
				if(worlds.isEmpty())
					worlds.add(player.getLocation().getWorld().getName());
			}
			
			//ChatType - removing bad players
			while (targets.hasNext()) {
				Player target = targets.next();
				if(target.equals(player)) continue; // sender je target
				//TODO - ignore
				
				if(target.hasPermission("SCR.Other.ChatTypeBypass")) continue; //TODO - permise do configu
				
				//PER_WORLD type
				if(type.equalsIgnoreCase("PER_WORLD") || type.equalsIgnoreCase("PERWORLD") || type.equalsIgnoreCase("WORLD")) {
					if( !worlds.contains(target.getLocation().getWorld().getName()) ) //If group of worlds contains target world
						target.remove();
					continue;
				}
			
				//DISTANCE
				if( type.equalsIgnoreCase("DISTANCE") ) {
					if(!player.getWorld().equals(target.getWorld())) //If they are in same world
						target.remove();
					else if(target.getLocation().distance(player.getLocation()) > distance)
						target.remove();
					continue;
				}
			}
			
			//TODO - flood, antispam, ...
			
			if(me.devtec.scr.utils.ChatUtils.Notification.isEnabled()) {
				//TODO - notification
			}
			
			if(message != null) {
				String nmsg = colors(message, player);
				String format_path = ChatFormat.getPath(player);
				e.setMessage(nmsg);
				setFormat(player, format_path, Placeholders.c().addPlayer("player", player)
						.add("message", nmsg.replace("%", "%%")).add("playername", 
								StringUtils.colorize(
										PlaceholderAPISupport.replace(
												Loader.chat.getString(format_path+".playername"), player, true
												)
										))
						, e);
				
				
			}
			
		}
	}
	
	public static String colors(String msg, Player p) {
		return Colors.colorize(msg, false, p);
	}
	

	//path - path.chat
	public static void setFormat(CommandSender player, String path, Placeholders placeholders, AsyncPlayerChatEvent event) {
		if(player==null)
			return;
		
		Object text = Loader.chat.get(path+".chat");

		if (text instanceof Collection) {
			if (Loader.chat.isJson(path)) {
				String line = Loader.chat.getString(path);
				String trimmed = line.trim();
				if (trimmed.equals("[]") || trimmed.equals("{}"))
					return; // Do not send empty json
				msgJson(player, line, placeholders, event);
				return;
			}
			for (String line : Loader.chat.getStringList(path))
				msg(player, line, placeholders, event);
			return;
		}
		String line = Loader.chat.getString(path+".chat");
		if (line.isEmpty())
			return; // Do not send empty strings
		msg(player, line, placeholders, event);
	}

	//original - from chat.yml - chat
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
		event.setFormat(ComponentAPI.listToString(jsonList));

		jsonList = ComponentAPI.fixJsonList(jsonList);
		Object packet = BukkitLoader.getNmsProvider().chatBase(Json.writer().simpleWrite(jsonList));

		BukkitLoader.getPacketHandler().send(event.getRecipients(), packet);
	}

	//original - from chat.yml - chat
	@SuppressWarnings("unchecked")
	private static void replaceJson(CommandSender s, Map<String, Object> map, Placeholders placeholders) {
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getKey().equals("text")) {
				String text = entry.getValue() + "";

				text = StringUtils.colorize(
						PlaceholderAPI.apply(text, s instanceof Player ? ((Player) s).getUniqueId() : null) );
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

							text = PlaceholderAPISupport.replace(text, s, true, null);
							text = MessageUtils.placeholder(s, text, placeholders);
							itr.set(text);
						}
					}
					continue;
				}
				String text = entry.getValue() + "";

				text = StringUtils.colorize( PlaceholderAPISupport.replace(text, s, true, null) );
				text = MessageUtils.placeholder(s, text, placeholders);
				entry.setValue(text);
			}
		}
	}
	//original - from chat.yml - chat
	private static void msg(CommandSender s, String original, Placeholders placeholders, AsyncPlayerChatEvent event) {
		String text = original;
		//first replacing chat palceholders, then adding %message% from player
		text = MessageUtils.placeholder(s, StringUtils.colorize( PlaceholderAPISupport.replace(text, s, true)), placeholders);
		event.setFormat(text);
	}
}
