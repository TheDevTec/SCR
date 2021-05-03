package me.devtec.servercontrolreloaded.events;

import me.devtec.servercontrolreloaded.commands.message.PrivateMessageManager;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.*;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.ChatMessage;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.json.Writer;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatFormat implements Listener {
	static Loader plugin = Loader.getInstance;
	static Pattern colorPattern = Pattern.compile("[XxA-Fa-fUu0-9]");

	@SuppressWarnings("unchecked")
	public static Collection<?> colorizeList(Collection<?> json, Player p, String msg,boolean colors) {
		ArrayList<Object> colorized = new ArrayList<>(json.size());
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
				colorized.add(r(p,(String)e,msg, true,colors));
				continue;
			}
			colorized.add(e);
		}
		return colorized;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> colorizeMap(Map<String, Object> jj, Player p, String msg,boolean colors) {
		HashMap<String, Object> json = new HashMap<>(jj.size());
		for (Entry<String, Object> e : jj.entrySet()) {
			if (e.getValue() instanceof Collection) {
				json.put(e.getKey(), colorizeList((Collection<?>) e.getValue(), p, msg,colors));
				continue;
			}
			if (e.getValue() instanceof Map) {
				json.put(e.getKey(), colorizeMap((Map<String, Object>) e.getValue(), p, msg,colors));
				continue;
			}
			if (e.getValue() instanceof String && !e.getKey().equals("color")) {
				json.put(e.getKey(), r(p,(String) e.getValue(),msg, true,colors));
				continue;
			}
			json.put(e.getKey(), e.getValue());
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	public static Object r(Player p, Object s, String msg, boolean usejson, boolean colors) {
		if(s.toString().trim().isEmpty())return s;
		if (usejson) {
			try {
				if(s instanceof Map && s!=null) {
					return colorizeMap((Map<String, Object>) s,p,msg,colors);
				} //else continue in code below
				if(s instanceof Collection && s!=null) {
					return colorizeList((Collection<Object>) s,p,msg,colors);
				} //else continue in code below
			}catch(Exception err) {}
		}
		if(colors) {
		s=s.toString().replace("&u", "§[u]").replace("&U", "§[u]");
		String orig = (String)s;
		s=TabList.replace(s.toString(), p, colors);
		if(s==null)s=orig;
		if (msg != null && s.toString().contains("%message%"))
			s=s.toString().replace("%message%", msg);
		if(s.toString().contains("§[u]"))
			s=rainbow(s.toString().replace("§[u]", "§u"));
		return s;
		}else {
			String orig = (String)s;
			s=TabList.replace(s.toString(), p, colors);
			if(s==null)s=orig;
			return s;
		}
	}
	private static Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|§[Xx](§[A-Fa-f0-9]){6}|§[A-Fa-f0-9UuXx])");
	
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

	private boolean is(String s) {
		for (Player p : TheAPI.getOnlinePlayers()) {
			if (s.equalsIgnoreCase(p.getName()))
				return true;

		}
		return false;
	}

	private int count(String string) {
		int upperCaseCount = 0;
		for (char c : string.toCharArray())
			if (Character.isAlphabetic(c) && Character.isUpperCase(c))
				++upperCaseCount;
		return upperCaseCount;
	}

	private String removeDoubled(String s) {
		char prevchar = 0;
		int count = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (prevchar != c) {
				sb.append(c);
				count = 0;
			}else {
				prevchar = c;
				if(++count>=count(c))continue;
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	private int count(char c) {
		return Character.isDigit(c)?6:Character.isAlphabetic(c)?2:4;
	}

	static Map<Player, String> old = new HashMap<>();

	private boolean isSim(Player p, String msg) {
		if (Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
			String o = old.put(p, msg);
			if (o!=null && o.length() >= 5 && msg.length() >= o.length())
				return msg.contains(o.substring(1, o.length() - 1));
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.MONITOR)
	public void chatFormat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())return;
		Player p = e.getPlayer();
		ChatFormatter.setupName(p);
		if (TheAPI.getCooldownAPI(p.getName()).getTimeToExpire("world-create") != -1) {
			e.setCancelled(true);
			if (e.getMessage().toLowerCase().equals("cancel")) {
				User d = TheAPI.getUser(p);
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				d.remove("MultiWorlds-Create");
				d.remove("MultiWorlds-Generator");
				d.save();
				TheAPI.sendTitle(p,"", "&6Cancelled");
			}else
			if (TheAPI.getCooldownAPI(p.getName()).expired("world-create")) {
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				MultiWorldsGUI.openInvCreate(p);
			} else {
				TheAPI.getCooldownAPI(p.getName()).removeCooldown("world-create");
				TheAPI.getUser(p).setAndSave("MultiWorlds-Create", Colors.remove(e.getMessage()));
				MultiWorldsGUI.openInvCreate(p);
			}
			return;
		}
		String msg = e.getMessage();
		if (!p.hasPermission("SCR.Other.Admin")) {
			if (!p.hasPermission("SCR.Other.RulesBypass")) {
		for (Rule rule : Loader.rules) {
			if(!Loader.events.getStringList("onChat.Rules").contains(rule.getName()))continue;
			msg = rule.apply(msg);
			if (msg == null) break;
		}
		if (msg == null) {
			e.setCancelled(true);
			return;
		}}
		String message = msg;
		String d = ""; // anti doubled letters
		int up = 0; // anti caps
		if (setting.spam_double) {
			if (message.split(" ").length == 0) {
				if (!is(message)) {
					up = up + count(message);
					String removed = removeDoubled(message);
					d +=" " + (message.length() - removed.length() >= 5 ? removed : message);
				} else
					d +=" " + message;
			} else
				for (String s : message.split(" ")) {
					if (!is(s)) {
						up = up + count(s);
						String removed = removeDoubled(message);
						d = d + " " + (message.length() - removed.length() >= 5 ? removed : s);
					} else
						d = d + " " + s;
				}
			d = d.replaceFirst(" ", "");
		} else
			d = message;
		String build = d;
		if (setting.caps_chat && !p.hasPermission("SCR.Other.Caps")) {
			if (up != 0 && up / ((double) d.length() / 100) >= 60 && d.length() > 5) {
				build = "";
				if (d.split(" ").length == 0) {
					if (!is(d)) {
						build = build + " " + d.toLowerCase();
					} else
						build = build + " " + d;
				} else
					for (String s : d.split(" ")) {
						if (!is(s)) {
							build = build + " " + s.toLowerCase();
						} else
							build = build + " " + s;
					}
				build = build.replaceFirst(" ", "");
			}
		}
		message = build;
		if (Loader.config.getBoolean("SpamWords.SimiliarMessage") && !p.hasPermission("SCR.Other.SimiliarMessage"))
			if (isSim(p, message)) {
				e.setCancelled(true);
				return;
			}
		msg=message;
		}
		if(PrivateMessageManager.hasChatLock(p)) {
			if(PrivateMessageManager.getLockType(p).equalsIgnoreCase("msg")) {
				PrivateMessageManager.reply(p, msg);
				String r = PrivateMessageManager.getReply(p);
				PrivateMessageManager.setReply(r.equalsIgnoreCase("console")?TheAPI.getConsole():TheAPI.getPlayerOrNull(r), p.getName());
			}else
			if(PrivateMessageManager.getLockType(p).equalsIgnoreCase("helpop")) {
				TheAPI.broadcast(Loader.config.getString("Format.HelpOp").replace("%sender%", p.getName())
						.replace("%sendername%", TheAPI.getPlayerOrNull(p.getName())!=null?TheAPI.getPlayerOrNull(p.getName()).getDisplayName():p.getName()).replace("%message%", msg), Loader.cmds.getString("Message.Helpop.SubPermission.Receive"));
				if (!Loader.has(p, "Helpop", "Message", "Receive"))
					TheAPI.msg(Loader.config.getString("Format.HelpOp").replace("%sender%", p.getName()).replace("%sendername%", TheAPI.getPlayerOrNull(p.getName())!=null?TheAPI.getPlayerOrNull(p.getName()).getDisplayName():p.getName()).replace("%message%", msg), p);
			}
			e.setCancelled(true);
			return;
		}
		if (setting.lock_chat && !Loader.has(p, "ChatLock", "Other")) {
			e.setCancelled(true);
			Loader.sendMessages(p, "ChatLock.IsLocked");
			Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
					.add("%playername%", p.getDisplayName()).add("%message%", msg), Loader.getPerm("ChatLock", "Other"));
			if(msg!=null)
			e.setMessage(r(msg, p));
			return;
		}
		Iterator<Player> a = e.getRecipients().iterator();
		String ty = Loader.config.getString("Options.Chat.Type");
		double distance = Loader.config.getDouble("Options.Chat.Distance");
		while(a.hasNext()) {
			Player s = a.next();
			if(s.equals(p))continue;
			if(PrivateMessageManager.getIgnoreList(s.getName()).contains(p.getName())) {
				a.remove();
				continue;
			}
			if(s.hasPermission("SCR.Other.ChatTypeBypass"))continue;
			if(ty.equalsIgnoreCase("per_world")
					||Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("perworld")||
				Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("world")) {
					if(!p.getWorld().equals(s.getWorld())) {
						a.remove();
						continue;
				}
			}
			if(ty.equalsIgnoreCase("per_distance")
					||Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("distance")) {
					if(!p.getWorld().equals(s.getWorld())) {
						a.remove();
						continue;
				}
					else if(p.getLocation().distance(s.getLocation())>distance) {
						a.remove();
						continue;
				}
			}
		}
		Object[] format = ChatFormatter.getChatFormat(p, 1);
		String colorOfFormat = getColorOf(ChatFormat.r(p, format[0], null, (format[0] instanceof Map || format[0] instanceof List) && ChatFormatter.getStatus(p, (int)format[1], "json"), false));
		if(Loader.config.getBoolean("Options.ChatNotification.Enabled")) {
			Sound sound = null;
			String[] title = {Loader.config.getString("Options.ChatNotification.Title"), Loader.config.getString("Options.ChatNotification.SubTitle")};
			String actionbar = Loader.config.getString("Options.ChatNotification.ActionBar").replace("%target%", p.getName()).replace("%targetname%", ChatFormatter.displayName(p)).replace("%targetcustomname%", ChatFormatter.customName(p));
			String color = Loader.config.getString("Options.ChatNotification.Color");
			try {
				sound = Sound.valueOf(Loader.config.getString("Options.ChatNotification.Sound").toUpperCase());
			}catch(Exception | NoSuchFieldError err) {}
			for(Player s : e.getRecipients())
				if(p.canSee(s) && p!=s && msg.contains(s.getName())) {
					msg=replacePlayer(color, colorOfFormat, msg, s.getName(), p);
					if(sound!=null)
						s.playSound(s.getLocation(), sound, 1,1);
					if(!(title[0].trim().isEmpty() && title[1].trim().isEmpty()))
						TheAPI.sendTitle(s, title[0].trim().isEmpty()?"":TabList.replace(title[0], s, true), title[1].trim().isEmpty()?"":TabList.replace(title[1], s, true));
					if(!actionbar.trim().isEmpty())TheAPI.sendActionBar(s, TabList.replace(actionbar, s, true));
				}
		}
		if(msg!=null) {
			String ff = r(msg,p);
			e.setMessage(ff);
			Object formatt = ChatFormatter.chat(p, ff);
			if (formatt != null) {
				if(formatt instanceof String)
					e.setFormat(((String)formatt).replace("%", "%%"));
				else
				if (formatt instanceof Map || formatt instanceof Collection) {
					List<Map<String,Object>> o = new ArrayList<>();
					if(formatt instanceof Map) {
						o.add((Map<String, Object>) formatt);
						formatt=o;
					}else {
						for(Object w : ((Collection<Object>)formatt)) {
							if(w instanceof Map) {
								o.add((Map<String, Object>) w);
							}else {
								Map<String, Object> g = new HashMap<>();
								g.put("text", w+"");
								o.add(g);
							}
						}
						formatt=o;
					}
					List<Map<String,Object>> list = ChatMessage.fixListMap((List<Map<String,Object>>)formatt);
					e.setFormat(convertToLegacy(list).replace("%", "%%"));
					if(!e.isCancelled())
					Ref.sendPacket(e.getRecipients(), NMSAPI.getPacketPlayOutChat(NMSAPI.ChatType.SYSTEM, NMSAPI.getIChatBaseComponentJson(Writer.write(list))));
					e.getRecipients().clear(); //for our custom chat
				}
			}
		}else e.setCancelled(true);
	}
	
	String replacePlayer(String color, String format, String msg, String player, Player p) {
		String c = StringUtils.colorize(color+player);
		Pattern g = Pattern.compile(player, Pattern.CASE_INSENSITIVE);
		StringBuffer buf = new StringBuffer(msg.length());
		String last = format;
		int count = 1;
		String[] split = g.split(msg);
		for(String aa : split) {
			last=getLastColors(last+aa);
			if(count++<split.length)
				buf.append(aa+c+((last.toLowerCase().contains("&u")||last.toLowerCase().contains("§u"))?last:StringUtils.colorize(last)));
			else
				buf.append(aa);
		}
		if(msg.equalsIgnoreCase(player))
			buf.append(c);
		else
		if(msg.toLowerCase().endsWith(player.toLowerCase()))
			buf.append(c);
		return buf.toString();
	}

	private String convertToLegacy(List<Map<String, Object>> list) {
		StringBuilder b = new StringBuilder();
		for(Map<String, Object> text : list)
			b.append(StringUtils.colorize(getColor(""+text.getOrDefault("color","")))+text.get("text"));
		return b.toString();
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
							return text=((Map<String, Object>) o).get("color").toString();
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
	private static Pattern getLast = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9K-Ok-oUuXx])");

	public static String getLastColors(String s) {
		Matcher m = getLast.matcher(s);
		String colors = "";
		while(m.find()) {
			String last = m.group(1);
			if(last.matches("[&§][A-Fa-f0-9Uu]|#[A-Fa-f0-9]{6}|[&§][Xx]([§&][A-Fa-f0-9]){6}"))
				colors=last;
			else
				colors+=last;
		}
		return colors;
	}
}