package me.devtec.scr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.shared.components.ComponentAPI;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.json.Json;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class MessageUtils {

	public static void msgConfig(CommandSender s, String path, Object[] placeholders, CommandSender[] targets) {
		msgConfig(s, Loader.translations, path, placeholders, targets);
	}

	public static void msgConfig(CommandSender s, Config config, String path, Object[] placeholders, CommandSender[] targets) {
		Object text = config.get(path);
		
		if(text == null) {
			Loader.plugin.getLogger().info("Path "+path+" not found in config "+config.getFile().getName()+", report this bug to the DevTec discord.");
			return;
		}
		
		if(text instanceof Collection) {
			if(config.isJson(path)) {
				String line = config.getString(path);
				String trimmed = line.trim();
				if(trimmed.equals("[]") || trimmed.equals("{}"))return; //Do not send empty json
				msgJson(s, line, placeholders, targets);
				return;
			}
			for(String line : config.getStringList(path))
				msg(s, line, placeholders, targets);
			return;
		}
		String line = config.getString(path);
		if(line.isEmpty())return; //Do not send empty strings
		msg(s, line, placeholders, targets);
	}

	@SuppressWarnings("unchecked")
	public static void msgJson(CommandSender s, String original, Object[] placeholders, CommandSender[] targets) {
		Object json = Json.reader().simpleRead(original);
		List<Map<String, Object>> jsonList = new ArrayList<>();
		if(json instanceof Collection) {
			for(Object val : (Collection<?>)json) {
				if(val instanceof Map) {
					jsonList.add((Map<String, Object>)val);
				}else {
					if(val.toString().isEmpty())continue; //You are trying to fix json by yourself?
					jsonList.addAll(ComponentAPI.toJsonList(ComponentAPI.fromString(val.toString())));
				}
			}
		}else {
			if(json instanceof Map) {
				jsonList.add((Map<String, Object>)json);
			}else return; //Bug?
		}
		
		//PROCESS PLACEHOLDER & COLORS
		for(Map<String, Object> map : jsonList)
			replaceJson(s, map, placeholders);

		jsonList = ComponentAPI.fixJsonList(jsonList);
		Object packet = BukkitLoader.getNmsProvider().chatBase(Json.writer().simpleWrite(jsonList));
		
		for(CommandSender target : targets) {
			if(target instanceof Player) {
				BukkitLoader.getPacketHandler().send((Player)target, packet);
			}else {
				target.sendMessage(ComponentAPI.listToString(jsonList));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void replaceJson(CommandSender s, Map<String, Object> map, Object[] placeholders) {
		for(Entry<String, Object> entry : map.entrySet()) {
			if(entry.getKey().equals("text")) {
				int pos = 0;
				String text = entry.getValue()+"";
				for(Object placeholder : placeholders) {
					text = text.replace("{"+(pos++)+"}", placeholder.toString());
				}
				text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player)s).getUniqueId() : null));
				entry.setValue(text);
				continue;
			}
			if(entry.getKey().equals("hoverEvent")||entry.getKey().equals("clickEvent")) {
				replaceJson(s, (Map<String, Object>)entry.getValue(), placeholders);
				continue;
			}
			if(entry.getKey().equals("contents")||entry.getKey().equals("value")) {
				if(entry.getValue() instanceof Map) {
					replaceJson(s, (Map<String, Object>)entry.getValue(), placeholders);
					continue;
				}
				if(entry.getValue() instanceof List) {
					List<Object> col = (List<Object>)entry.getValue();
					ListIterator<Object> itr = col.listIterator();
					while(itr.hasNext()) {
						Object val = itr.next();
						if(val instanceof Map) {
							replaceJson(s, (Map<String, Object>)val, placeholders);
						}else {
							int pos = 0;
							String text = val+"";
							for(Object placeholder : placeholders) {
								text = text.replace("{"+(pos++)+"}", placeholder.toString());
							}
							text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player)s).getUniqueId() : null));
							itr.set(text);
						}
					}
					continue;
				}
				int pos = 0;
				String text = entry.getValue()+"";
				for(Object placeholder : placeholders) {
					text = text.replace("{"+(pos++)+"}", placeholder.toString());
				}
				text = StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player)s).getUniqueId() : null));
				entry.setValue(text);
			}
		}
	}

	public static void msg(CommandSender s, String original, Object[] placeholders, CommandSender[] targets) {
		String text = original;
		int pos = 0;
		for(Object placeholder : placeholders) {
			text = text.replace("{"+(pos++)+"}", placeholder.toString());
		}
		for(CommandSender target : targets)
			target.sendMessage(StringUtils.colorize(PlaceholderAPI.apply(text, s instanceof Player ? ((Player)s).getUniqueId() : null)));
	}
}
