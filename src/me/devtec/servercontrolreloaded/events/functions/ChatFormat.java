package me.devtec.servercontrolreloaded.events.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.servercontrolreloaded.commands.message.PrivateMessageManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.ChatFormatter;
import me.devtec.servercontrolreloaded.utils.Colors;
import me.devtec.servercontrolreloaded.utils.Rule;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.servercontrolreloaded.utils.multiworlds.MWGUI;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.utils.ChatMessage;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.json.Json;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

//TODO Add options to disable AntiFlood & AntiCaps
//Options.AntiSpam.Flood
//Options.AntiSpam.Caps.Chat

/**
 * 1.9. 2021
 * 
 * @author StraikerinaCZ
 *
 */
public class ChatFormat implements Listener {
	private static final Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|§[Xx](§[A-Fa-f0-9]){6}|§[A-Fa-f0-9UuXx])");
	private static Pattern getLast = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9K-Ok-oUuXx])");

	@SuppressWarnings("unchecked")
	public static Collection<?> colorizeList(Collection<?> json, Player p, String msg, boolean colors) {
		List<Object> colorized = new ArrayList<>(json.size());
		for (Object e : json) {
			if (e instanceof Collection) {
				colorized.add(colorizeList((Collection<?>) e,p,msg,colors));
				continue;
			}
			if (e instanceof Map) {
				colorized.add(colorizeMap((Map<String, Object>) e,p,msg,colors));
				continue;
			}
			if (e instanceof String) {
				colorized.add(r(p,e,msg, true,colors));
				continue;
			}
			colorized.add(e);
		}
		return colorized;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> colorizeMap(Map<String, Object> jj, Player p, String msg, boolean colors) {
		for (Entry<String, Object> e : jj.entrySet()) {
			if (e.getValue() instanceof Collection) {
				e.setValue(colorizeList((Collection<?>) e.getValue(), p, msg,colors));
				continue;
			}
			if (e.getValue() instanceof Map) {
				e.setValue(colorizeMap((Map<String, Object>) e.getValue(), p, msg,colors));
				continue;
			}
			if (e.getValue() instanceof String && !e.getKey().equals("color")) {
				e.setValue(r(p, e.getValue(),msg, true,colors));
				continue;
			}
		}
		return jj;
	}

	@SuppressWarnings("unchecked")
	public static Object r(Player p, Object s, String msg, boolean usejson, boolean colors) {
		if(s==null||s.toString().trim().isEmpty())return s;
		if (usejson) {
			try {
				if(s instanceof Map) {
					return colorizeMap((Map<String, Object>) s,p,msg,colors);
				} //else continue in code below
				if(s instanceof Collection) {
					return colorizeList((Collection<Object>) s,p,msg,colors);
				} //else continue in code below
			}catch(Exception err) {}
		}
		if(colors) {
		s=s.toString().replace("&u", "§[u]").replace("&U", "§[u]");
		String orig = (String)s;
		s=TabList.replace(s.toString(), p, true);
		if(s==null)s=orig;
		if (msg != null && s.toString().contains("%message%"))
			s=s.toString().replace("%message%", msg);
		if(s.toString().contains("§[u]"))
			s=rainbow(s.toString().replace("§[u]", "§u"));
		}else {
			String orig = (String)s;
			s=TabList.replace(s.toString(), p, false);
			if(s==null)s=orig;
		}
		return s;
	}
	
	private static String rainbow(String b) {
		StringBuilder d = new StringBuilder(b.length());
		String[] split = fixedSplit.split(b);
		//atempt to add colors to split
		Matcher m = fixedSplit.matcher(b);
		int id = 1;
		while(m.find()) {
			try {
			split[id]=m.group(1)+split[id++];
			}catch(Exception err) {
			}
		}
		//colors
		for (String ff : split) {
			if (ff.toLowerCase().contains("§u"))
				ff = StringUtils.color.colorize(ff.replace("§u",""));
			d.append(ff);
		}
		return d.toString();
	}
	
	public static String r(String msg, CommandSender p) {
		if (setting.color_chat)
			return Colors.colorize(msg, false, p);
		else
			return msg;
	}

	public static String getLastColors(String s) {
		Matcher m = getLast.matcher(s);
		String colors = "";
		while (m.find()) {
			String last = m.group(1);
			if (last.matches("[&§][A-Fa-f0-9Uu]|#[A-Fa-f0-9]{6}|[&§][Xx]([§&][A-Fa-f0-9]){6}"))
				colors = last;
			else
				colors += last;
		}
		return colors;
	}

	String replacePlayer(String color, String format, String msg, String player, Player p) {
		String c = StringUtils.colorize(color + player);
		Pattern g = Pattern.compile(player, Pattern.CASE_INSENSITIVE);
		StringBuilder buf = new StringBuilder(msg.length());
		String last = format;
		int count = 1;
		String[] split = g.split(msg);
		for (String aa : split) {
			last = getLastColors(last + aa);
			buf.append(aa);
			if (count++ < split.length)
				buf.append(c).append(((last.toLowerCase().contains("&u") || last.toLowerCase().contains("§u")) ? last : StringUtils.colorize(last)));
		}
		if (msg.equalsIgnoreCase(player))
			buf.append(c);
		else if (msg.toLowerCase().endsWith(player.toLowerCase()))
			buf.append(c);
		return buf.toString();
	}

	private static String replace(String format, Player player) {
		return StringUtils.colorize(
				format.replace("%player%", player.getName()).replace("%playername%", player.getDisplayName()));
	}

	public static String r(Player p, String s, String msg) {
		s = s.replace("&u", "§[u]").replace("&U", "§[u]");
		String orig = s;
		s = replace(s, p);
		if (s == null)
			s = orig;
		if (msg != null && s.contains("%message%"))
			s = s.replace("%message%", msg);
		if (s.contains("§[u]"))
			s = rainbow(s.replace("§[u]", "§u"));
		return s;
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.MONITOR)
	public void chatFormat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();
		ChatFormatter.setupName(p);
		CooldownAPI cool = TheAPI.getCooldownAPI(p);
		if (cool.getTimeToExpire("world-create") != -1) {
			e.setCancelled(true);
			if (e.getMessage().equalsIgnoreCase("cancel")) {
				User d = TheAPI.getUser(p);
				cool.removeCooldown("world-create");
				d.remove("MultiWorlds-Create");
				d.remove("MultiWorlds-Generator");
				d.save();
				TheAPI.sendTitle(p, "", "&6Cancelled");
			} else if (cool.expired("world-create")) {
				cool.removeCooldown("world-create");
				MWGUI.openInvCreate(p);
			} else {
				cool.removeCooldown("world-create");
				TheAPI.getUser(p).setAndSave("MultiWorlds-Create", Colors.remove(e.getMessage()));
				MWGUI.openInvCreate(p);
			}
			return;
		}
		String msg = e.getMessage();
		if(Loader.config.getBoolean("ChatFormat.enabled")) {
			if (PrivateMessageManager.hasChatLock(p)) {
				if (PrivateMessageManager.getLockType(p).equalsIgnoreCase("msg")) {
					PrivateMessageManager.reply(p, msg);
					String r = PrivateMessageManager.getReply(p);
					PrivateMessageManager.setReply(r.equalsIgnoreCase("console") ? TheAPI.getConsole() : TheAPI.getPlayerOrNull(r), p.getName());
				} else if (PrivateMessageManager.getLockType(p).equalsIgnoreCase("helpop")) {
					TheAPI.broadcast(Loader.config.getString("Format.HelpOp").replace("%sender%", p.getName())
							.replace("%sendername%", TheAPI.getPlayerOrNull(p.getName()) != null ? TheAPI.getPlayerOrNull(p.getName()).getDisplayName() : p.getName()).replace("%message%", msg), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive");
					if (!Loader.has(p, "Helpop", "Message", "Receive"))
						TheAPI.msg(Loader.config.getString("Format.HelpOp").replace("%sender%", p.getName()).replace("%sendername%", TheAPI.getPlayerOrNull(p.getName()) != null ? TheAPI.getPlayerOrNull(p.getName()).getDisplayName() : p.getName()).replace("%message%", msg), p);
				}
				e.setCancelled(true);
				return;
			}
			if (setting.lock_chat && !Loader.has(p, "ChatLock", "Other")) {
				e.setCancelled(true);
				Loader.sendMessages(p, "ChatLock.IsLocked");
				Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
						.add("%playername%", p.getDisplayName()).add("%message%", msg), Loader.getPerm("ChatLock", "Other"));
				e.setMessage(r(msg, p));
				return;
			}
			Iterator<Player> a = e.getRecipients().iterator();
			String ty = Loader.config.getString("Options.Chat.Type");
			double distance = Loader.config.getDouble("Options.Chat.Distance");
			while (a.hasNext()) {
				Player s = a.next();
				if (s.equals(p)) continue;
				if (PrivateMessageManager.getIgnoreList(s.getName()).contains(p.getName())) {
					a.remove();
					continue;
				}
				if (s.hasPermission("SCR.Other.ChatTypeBypass")) continue;
				if (ty.equalsIgnoreCase("per_world")
						|| ty.equalsIgnoreCase("aperworld") ||
						ty.equalsIgnoreCase("world")) {
					if (!p.getWorld().equals(s.getWorld())) {
						a.remove();
						continue;
					}
				}
				if (ty.equalsIgnoreCase("per_distance")
						|| ty.equalsIgnoreCase("distance")) {
					if (!p.getWorld().equals(s.getWorld())) {
						a.remove();
					} else if (p.getLocation().distance(s.getLocation()) > distance) {
						a.remove();
					}
				}
			}
			List<String> ps = new ArrayList<>();
			for (Player s : e.getRecipients())
				ps.add(s.getName());
			msg = chat(p.hasPermission("scr.other.chat.bypass"), e.getMessage(), ps);
			if (msg == null) {
				e.setCancelled(true);
				return;
			}

			if (Loader.config.getBoolean("Options.ChatNotification.Enabled")) {
				Object[] format = ChatFormatter.getChatFormat(p, 1);
				String colorOfFormat = getColorOf(ChatFormat.r(p, format[0], msg, (format[0] instanceof Map || format[0] instanceof List) && ChatFormatter.getStatus(p, (int) format[1], "json"), false));
				Sound sound = Sound.ENTITY_PLAYER_LEVELUP;
				String[] title = {Loader.config.getString("Options.ChatNotification.Title"), Loader.config.getString("Options.ChatNotification.SubTitle")};
				String actionbar = Loader.config.getString("Options.ChatNotification.ActionBar").replace("%target%", p.getName()).replace("%targetname%", ChatFormatter.displayName(p)).replace("%targetcustomname%", ChatFormatter.customName(p));
				String color = Loader.config.getString("Options.ChatNotification.Color");
				try {
					sound = Sound.valueOf(Loader.config.getString("Options.ChatNotification.Sound").toUpperCase());
				} catch (Exception | NoSuchFieldError err) {
				}
				for (Player s : e.getRecipients())
					if (!API.hasVanish(s) && p != s && msg.contains(s.getName())) {
						msg = replacePlayer(color, colorOfFormat, msg, s.getName(), p);
						if (ChatFormatter.getNotify(s)) {
							if (sound != null)
								s.playSound(s.getLocation(), sound, 1, 1);
							if (title[0] != null && title[1] != null && !(title[0].trim().isEmpty() && title[1].trim().isEmpty()))
								TheAPI.sendTitle(s, title[0].trim().isEmpty() ? "" : TabList.replace(title[0], s, true), title[1].trim().isEmpty() ? "" : TabList.replace(title[1], s, true));
							if (!actionbar.trim().isEmpty())
								TheAPI.sendActionBar(s, TabList.replace(actionbar, s, true));
						}
					}
			}
			if (msg != null) {
				String ff = r(msg, p);
				e.setMessage(ff);
				Object formatt = ChatFormatter.chat(p, ff);
				if (formatt != null) {
					if (formatt instanceof String)
						e.setFormat(((String) formatt).replace("%", "%%"));
					else if (formatt instanceof Map || formatt instanceof Collection) {
						List<Map<String, Object>> o = new ArrayList<>();
						if (formatt instanceof Map) {
							o.add((Map<String, Object>) formatt);
						} else {
							for (Object w : ((Collection<Object>) formatt)) {
								if (w instanceof String) w = Json.reader().simpleRead((String) w);
								if (w instanceof Map) {
									o.add((Map<String, Object>) w);
								} else {
									Map<String, Object> g = new HashMap<>();
									g.put("text", w + "");
									o.add(g);
								}
							}
						}
						formatt = o;
						List<Map<String, Object>> list = ChatMessage.fixListMap((List<Map<String, Object>>) formatt);
						e.setFormat(ChatMessage.toLegacy((List<Object>)(List<?>)list).replace("%", "%%"));
						if (!e.isCancelled()) {
							String jsons = Json.writer().simpleWrite(list);
							jsons="[\"\","+jsons.substring(1);
							Ref.sendPacket(e.getRecipients(),NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(jsons)));
						}
						e.getRecipients().clear(); //for our custom chat
					}
				}
			} else e.setCancelled(true);
		}
	}

	private static String chat(boolean bypass, String string, List<String> players) {
		if(Loader.config.getBoolean("Options.AntiSpam.Caps"))
		if (string.length() > 4) {
			boolean startWithCaps = Character.isUpperCase(string.charAt(0));
			// CAPS LOCK
			boolean turningDown=false;
			
			StringBuilder caps = new StringBuilder(string.length());
			for (String s : string.split(" ")) {
				caps.append(' ');
				StringBuilder add = new StringBuilder(caps.capacity());
				int upper = 0;
				for (char a : s.toCharArray()) {
					if (Character.isUpperCase(a))
						++upper;
					add.append(a);
				}
				if (turningDown||upper > 3 && ((double) caps.length() / 100) * 30 <= upper) {
					turningDown=true;
					boolean added = false;
					for (String player : players) {
						if (!add.toString().contains(player)) {
							continue;
						}
						String[] split = add.toString().split(player);
						if (split.length == 0) {
							added = true;
							caps.append(player);
							continue;
						}
						added = true;
						caps.append(split[0].toLowerCase());
						caps.append(player);
						if (split.length > 1)
							caps.append(split[1].toLowerCase());
					}
					if (!added)
						caps.append(add.toString().toLowerCase());
				} else
					caps.append(add.toString());
			}
			caps.delete(0, 1);
			if (startWithCaps && !Character.isUpperCase(caps.charAt(0)))
				caps.setCharAt(0, Character.toUpperCase(caps.charAt(0)));
			string = caps.toString();

			caps.delete(0, caps.length());
			for (String s : string.split(" ")) {
				caps.append(' ');
				StringBuilder add = new StringBuilder(caps.capacity());
				int upper = 0;
				for (char a : s.toCharArray()) {
					if (Character.isUpperCase((char) a))
						++upper;
					add.append((char) a);
				}
				if (upper > 3 && ((double) caps.length() / 100) * 30 <= upper) {
					boolean added = false;
					for (String player : players) {
						if (!add.toString().contains(player)) {
							continue;
						}
						String[] split = add.toString().split(player);
						if (split.length == 0) {
							added = true;
							caps.append(player);
							continue;
						}
						added = true;
						caps.append(split[0].toLowerCase());
						caps.append(player);
						if (split.length > 1)
							caps.append(split[1].toLowerCase());
					}
					if (!added)
						caps.append(add.toString().toLowerCase());
				} else
					caps.append(add.toString());
			}
			caps.delete(0, 1);
			if (startWithCaps && !Character.isUpperCase(caps.charAt(0)))
				caps.setCharAt(0, Character.toUpperCase(caps.charAt(0)));
			string = caps.toString();
		}
		Map<Integer, String> pId = new HashMap<>();
		int i = 0;
		for (String p : players) {
			pId.put(i, p);
			string = string.replace(p, "§§" + i + "§h");
			++i;
		}

		// FLOOD
		if(Loader.config.getBoolean("Options.AntiSpam.Flood") && !bypass) {
			StringBuilder add = new StringBuilder(string.length());
			int cPrev = 0;
			char prev = 0;
			for (char a : string.toCharArray()) {
				char low = Character.toLowerCase(a);
				if(prev == low) {
					if(++cPrev >= 3) {
						continue;
					}
				}else {
					cPrev=0;
				}
				prev=low;
				add.append(a);
			}
			string=add.toString();
		}
		for (Rule r : Loader.rules)
			if(!bypass || !r.isBypassable())
				string = r.apply(string);
		if (string == null)
			return null;
		for (Entry<Integer, String> p : pId.entrySet()) {
			string = string.replace("§§" + p.getKey() + "§h", p.getValue());
		}
		return string;
	}
	
	String getColor(String color) {
		if(color.trim().isEmpty())return "";
		if(color.startsWith("#"))return color;
		try {
		return ChatColor.valueOf(color.toUpperCase())+"";
		}catch(Exception | NoSuchFieldError err) {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	private String getColorOf(Object format) {
		String text = null;
		if(format instanceof Map) {
			try {
			if(((Map<String, Object>) format).containsKey("color") && ((Map<String, Object>) format).containsKey("text")) {
				text=((Map<String, Object>) format).get("color").toString();
			}else {
				if((((Map<String, Object>)format).get("text")+"").contains("%message%")) {
					text=(((Map<String, Object>) format).get("text")+"").split("\\%message\\%")[0];
				}
			}
			}catch(Exception er) {}
		}else
		if(format instanceof Collection) {
			for(Object o : ((Collection<?>)format)) {
				if(o instanceof Map) {
					try {
					if(((Map<String, Object>) o).containsKey("color") && ((Map<String, Object>) o).containsKey("text")) {
						if((((Map<String, Object>) o).get("text")+"").contains("%message%")) {
							return ((Map<String, Object>) o).get("color").toString();
						}
						text=((Map<String, Object>) o).get("color").toString();
					}else {
						if((((Map<String, Object>) o).get("text")+"").contains("%message%")) {
							text=(((Map<String, Object>) o).get("text")+"").split("\\%message\\%")[0];
						}
					}
					}catch(Exception er) {}
				}else {
					try {
					if((""+o).contains("%message%")) {
						text=(""+o).split("\\%message\\%")[0];
					}
				}catch(Exception er) {}
				}
			}
		}else
			try {
			text=(""+format).split("\\%message\\%")[0];
			}catch(Exception er) {}
		if(text==null)return "";
		return getLastColors(text);
	}
}