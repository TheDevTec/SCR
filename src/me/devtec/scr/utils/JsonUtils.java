package me.devtec.scr.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.placeholderapi.PlaceholderAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.components.ComponentAPI;
import me.devtec.theapi.utils.json.Json;
import me.devtec.theapi.utils.nms.NmsProvider.ChatType;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class JsonUtils {
	
	public static String getChatFormatColorOf(Object value) {
		if(value instanceof Map) {
			if(((Map<?,?>)value).containsKey("text")) {
				if(((Map<?,?>)value).get("text").toString().contains("%message%")) {
					String[] split = ((Map<?,?>)value).get("text").toString().split("\\%message\\%");
					String result = StringUtils.getLastColors(StringUtils.colorize(split[0].replace("&u", "§u")));
					if(result.isEmpty())result=((Map<?,?>)value).get("color")+"";
					return result.equals("null")?"":null;
				}
			}
			if(((Map<?,?>)value).containsKey("extra")) {
				return getChatFormatColorOf(((Map<?,?>)value).get("extra"));
			}
		}
		if(value instanceof Collection) {
			for(Object o : (Collection<?>)value) {
				String result = getChatFormatColorOf(o);
				if(result.isEmpty())continue;
				return result;
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public static String msgRaw(Object value, PlaceholderBuilder builder, Player sender, Player... targets) {
		if(value==null||sender==null||targets==null||targets.length==0)return value+"";
		if(value instanceof Collection || value instanceof Map) {
			value=parse(value);
			Object json;
			if(value instanceof Collection) {
				json = colorizeList((Collection<?>)value,builder);
			}else
				json = colorizeMap((Map<String, Object>)value,builder);
			List<Map<String,Object>> oo = new ArrayList<>();
			if(json instanceof Map) {
				oo.add((Map<String, Object>) json);
			}else {
				for(Object w : ((Collection<Object>)json)) {
					if(w instanceof String)w= Json.reader().simpleRead((String)w);
					if(w instanceof Map) {
						oo.add((Map<String, Object>) w);
					}else {
						Map<String, Object> g = new HashMap<>();
						g.put("text", w+"");
						oo.add(g);
					}
				}
			}
			oo=ComponentAPI.fixJsonList(oo);
			String jsons = Json.writer().simpleWrite(oo);
			jsons="[\"\","+jsons.substring(1);
			Ref.sendPacket(targets,LoaderClass.nmsProvider.packetChat(ChatType.SYSTEM, LoaderClass.nmsProvider.chatBase(jsons)));
			return convertToLegacy(oo);
		}
		if(value instanceof Collection) {
			for(Object d : (Collection<?>)value)
				for(Player target : targets)
					TheAPI.msg(PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender:null,d+""), target);
		}else
			for(Player target : targets)
				TheAPI.msg(PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender:null,value+""), target);
		return value+"";
	}
	
	@SuppressWarnings("unchecked")
	public static void msgRaw(Object value, boolean isJson, PlaceholderBuilder builder, CommandSender... players) {
		if(value==null||players==null||players.length==0)return;
		if(isJson) {
			for(CommandSender sender : players) {
				value=parse(value);
				Object json;
				if(value instanceof Collection) {
					json = colorizeList((Collection<?>)value,builder);
				}else
					json = colorizeMap((Map<String, Object>)value,builder);
				
				List<Map<String,Object>> oo = new ArrayList<>();
				if(json instanceof Map) {
					oo.add((Map<String, Object>) json);
				}else {
					for(Object w : ((Collection<Object>)json)) {
						if(w instanceof String)w= Json.reader().simpleRead((String)w);
						if(w instanceof Map) {
							oo.add((Map<String, Object>) w);
						}else {
							Map<String, Object> g = new HashMap<>();
							g.put("text", w+"");
							oo.add(g);
						}
					}
				}
				oo=ComponentAPI.fixJsonList(oo);
				if(sender instanceof Player) {
					String jsons = Json.writer().simpleWrite(oo);
					jsons="[\"\","+jsons.substring(1);
					Ref.sendPacket((Player)sender,LoaderClass.nmsProvider.packetChat(ChatType.SYSTEM, LoaderClass.nmsProvider.chatBase(jsons)));
				}else {
					sender.sendMessage(convertToLegacy(oo));
				}
			}
			return;
		}
		for(CommandSender sender : players) {
			if(value instanceof Collection) {
				for(Object d : (Collection<?>)value)
					TheAPI.msg(PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender:null,d+""), sender);
			}else
				TheAPI.msg(PlaceholderAPI.setPlaceholders(sender instanceof Player ? (Player)sender:null,value+""), sender);
		}
	}
	
	private static String getStats(Map<String, Object> text) {
		String s = "";
		if(text.containsKey("bold") && (boolean)text.get("bold"))s+="&l";
		if(text.containsKey("italic") && (boolean)text.get("italic"))s+="&o";
		if(text.containsKey("strikethrough") && (boolean)text.get("strikethrough"))s+="&m";
		if(text.containsKey("underlined") && (boolean)text.get("underlined"))s+="&n";
		if(text.containsKey("obfuscated") && (boolean)text.get("obfuscated"))s+="&k";
		return s;
	}
	
	private static String getColor(String color) {
		if(color.trim().isEmpty())return "";
		if(color.startsWith("#"))return color;
		try {
		return ChatColor.valueOf(color.toUpperCase())+"";
		}catch(Exception | NoSuchFieldError err) {
			return "";
		}
	}
	
	private static String convertToLegacy(List<Map<String, Object>> list) {
		StringBuilder b = new StringBuilder();
		for(Map<String, Object> text : list)
			b.append(StringUtils.colorize(getColor(""+text.getOrDefault("color",""))+getStats(text)+text.get("text")));
		return b.toString();
	}
	
	private static Object parse(Object string) {
		if(string instanceof String == false)return string instanceof Collection?cloneCollection((Collection<?>)string) : string instanceof Map ? cloneMap((Map<?,?>)string):string;
		string=new String((String)string);
		Object result = Json.reader().simpleRead((String)string);
		if(result instanceof String)result=Json.reader().read((String)string);
		return result;
	}

	private static Object cloneMap(Map<?, ?> string) {
		Map<Object,Object> o = new LinkedHashMap<>();
		for(Entry<?, ?> entry : string.entrySet()) {
			Object a = entry.getValue();
			o.put(entry.getKey(), a instanceof Collection ? cloneCollection((Collection<?>)a) : a instanceof Map ? cloneMap((Map<?,?>)a) : a);
		}return o;
		
	}

	private static Object cloneCollection(Collection<?> string) {
		LinkedList<Object> o = new LinkedList<>();
		for(Object a : string)
			o.add(a instanceof Collection ? cloneCollection((Collection<?>)a) : a instanceof Map ? cloneMap((Map<?,?>)a) : a);
		return o;
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<?> colorizeList(Collection<?> json, PlaceholderBuilder c) {
		ArrayList<Object> colorized = new ArrayList<>(json.size());
		for (Object e : json) {
			if (e instanceof Collection) {
				colorized.add(colorizeList((Collection<?>) e,c));
				continue;
			}
			if (e instanceof Map) {
				colorized.add(colorizeMap((Map<String, Object>) e,c));
				continue;
			}
			if (e instanceof String) {
				colorized.add(r(e,c));
				continue;
			}
			colorized.add(e);
		}
		return colorized;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> colorizeMap(Map<String, Object> jj, PlaceholderBuilder c) {
		HashMap<String, Object> json = new HashMap<>(jj.size());
		for (Entry<String, Object> e : jj.entrySet()) {
			if (e.getValue() instanceof Collection) {
				json.put(e.getKey(), colorizeList((Collection<?>) e.getValue(), c));
				continue;
			}
			if (e.getValue() instanceof Map) {
				json.put(e.getKey(), colorizeMap((Map<String, Object>) e.getValue(),c ));
				continue;
			}
			if (e.getValue() instanceof String && !e.getKey().equals("color")) {
				json.put(e.getKey(), r(e.getValue(), c));
				continue;
			}
			json.put(e.getKey(), e.getValue());
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	private static Object r(Object s, PlaceholderBuilder c) {
		if(s==null||s.toString().trim().isEmpty())return s;
		try {
			if(s instanceof Map) {
				return colorizeMap((Map<String, Object>) s, c);
			}
			if(s instanceof Collection) {
				return colorizeList((Collection<Object>) s, c);
			} //else continue in code below
		}catch(Exception err) {}
		String orig = s.toString();
		orig=c.apply(orig);
		return orig;
	}
}
