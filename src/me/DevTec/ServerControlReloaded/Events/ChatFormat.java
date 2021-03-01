package me.DevTec.ServerControlReloaded.Events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.DevTec.ServerControlReloaded.Commands.Message.PrivateMessageManager;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.ChatFormatter;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.Rule;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.HoverMessage;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.json.Reader;

public class ChatFormat implements Listener {
	static Loader plugin = Loader.getInstance;
	static Pattern colorPattern = Pattern.compile("[XxA-Fa-fUu0-9]");

	public static Object[] colorizeArray(Object[] json, Player p, String msg) {
		ArrayList<Object> colorized = new ArrayList<Object>();
		Object[] arrobject = json;
		int n = arrobject.length;
		int n2 = 0;
		while (n2 < n) {
			Object e = arrobject[n2];
			if (e instanceof Collection) {
				colorized.add(colorizeList((Collection<?>) e,p,msg));
			}
			if (e instanceof Map) {
				colorized.add(colorizeMap((Map<?, ?>) e,p,msg));
			}
			if (e instanceof Object[]) {
				colorized.add(colorizeArray((Object[]) e,p,msg));
			}
			if (e instanceof String) {
				colorized.add(r(p,(String)e,msg, true));
			} else {
				colorized.add(e);
			}
			++n2;
		}
		return colorized.toArray();
	}

	public static Collection<?> colorizeList(Collection<?> json, Player p, String msg) {
		ArrayList<Object> colorized = new ArrayList<>();
		for (Object e : json) {
			if (e instanceof Collection) {
				colorized.add(colorizeList((Collection<?>) e,p,msg));
			}
			if (e instanceof Map) {
				colorized.add(colorizeMap((Map<?, ?>) e,p,msg));
			}
			if (e instanceof Object[]) {
				colorized.add(colorizeArray((Object[]) e,p,msg));
			}
			if (e instanceof String) {
				colorized.add(r(p,(String)e,msg, true));
				continue;
			}
			colorized.add(e);
		}
		return colorized;
	}

	public static Map<?, ?> colorizeMap(Map<?, ?> json, Player p, String msg) {
		HashMap<Object, Object> colorized = new HashMap<>();
		for (Map.Entry<?, ?> e : json.entrySet()) {
			if (e.getKey() instanceof Collection) {
				if (e.getValue() instanceof Collection) {
					colorized.put(colorizeList((Collection<?>) e.getKey(), p, msg),
							colorizeList((Collection<?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Map) {
					colorized.put(colorizeList((Collection<?>) e.getKey(), p, msg),
							colorizeMap((Map<?, ?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Object[]) {
					colorized.put(colorizeList((Collection<?>) e.getKey(), p, msg),
							colorizeArray((Object[]) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof String) {
					colorized.put(colorizeList((Collection<?>) e.getKey(), p, msg),
							r(p,(String) e.getValue(),msg, true));
				} else {
					colorized.put(colorizeList((Collection<?>) e.getKey(), p, msg), e.getValue());
				}
			}
			if (e.getKey() instanceof Map) {
				if (e.getValue() instanceof Collection) {
					colorized.put(colorizeMap((Map<?, ?>) e.getKey(), p, msg),
							colorizeList((Collection<?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Map) {
					colorized.put(colorizeMap((Map<?, ?>) e.getKey(), p, msg), colorizeMap((Map<?, ?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Object[]) {
					colorized.put(colorizeMap((Map<?, ?>) e.getKey(), p, msg),
							colorizeArray((Object[]) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof String) {
					colorized.put(colorizeMap((Map<?, ?>) e.getKey(), p, msg), r(p,(String) e.getValue(),msg, true));
				} else {
					colorized.put(colorizeMap((Map<?, ?>) e.getKey(), p, msg), e.getValue());
				}
			}
			if (e.getKey() instanceof Object[]) {
				if (e.getValue() instanceof Collection) {
					colorized.put(colorizeArray((Object[]) e.getKey(), p, msg),
							colorizeList((Collection<?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Map) {
					colorized.put(colorizeArray((Object[]) e.getKey(), p, msg),
							colorizeMap((Map<?, ?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Object[]) {
					colorized.put(colorizeArray((Object[]) e.getKey(), p, msg),
							colorizeArray((Object[]) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof String) {
					colorized.put(colorizeArray((Object[]) e.getKey(), p, msg),
							r(p,(String) e.getValue(),msg, true));
				} else {
					colorized.put(colorizeArray((Object[]) e.getKey(), p, msg), e.getValue());
				}
			}
			if (e.getKey() instanceof String) {
				if (e.getValue() instanceof Collection) {
					colorized.put((String)r(p,(String) e.getKey(),msg, true),
							colorizeList((Collection<?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Map) {
					colorized.put((String)r(p,(String) e.getKey(),msg, true), colorizeMap((Map<?, ?>) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof Object[]) {
					colorized.put((String)r(p,(String) e.getKey(),msg, true),
							colorizeArray((Object[]) e.getValue(), p, msg));
				}
				if (e.getValue() instanceof String && !e.getKey().equals("color")) {
					colorized.put((String)r(p,(String) e.getKey(),msg, true), r(p,(String) e.getValue(),msg, true));
					continue;
				}
				colorized.put((String)r(p,(String) e.getKey(),msg, true), e.getValue());
				continue;
			}
			if (e.getValue() instanceof Collection) {
				colorized.put(e.getKey(), colorizeList((Collection<?>) e.getValue(), p, msg));
			}
			if (e.getValue() instanceof Map) {
				colorized.put(e.getKey(), colorizeMap((Map<?, ?>) e.getValue(), p, msg));
			}
			if (e.getValue() instanceof Object[]) {
				colorized.put(e.getKey(), colorizeArray((Object[]) e.getValue(), p, msg));
			}
			if (e.getValue() instanceof String) {
				colorized.put(e.getKey(), r(p,(String) e.getValue(),msg, true));
				continue;
			}
			colorized.put(e.getKey(), e.getValue());
		}
		return colorized;
	}

	public static Object r(Player p, String s, String msg, boolean usejson) {
		if (Loader.config.getBoolean("Chat-Groups-Options.Json") && usejson) {
			Map<?,?> json = (Map<?,?>) Reader.read(s.replace("%", "%%"));
			if(json!=null&&!json.isEmpty()) {
				json=colorizeMap(json,p,msg.replace("%", "%%"));
				return json;
			} //else continue in code below
		}
		s=s.replace("%", "%%");
		if(s.toLowerCase().contains("&u")) {
				List<String> sd = new ArrayList<>();
				StringBuffer d = new StringBuffer();
				int found = 0;
				for (char c : msg.toCharArray()) {
					if (c == '&') {
						if (found == 1)
							d.append(c);
						found = 1;
						continue;
					}
					if (found == 1 && colorPattern.matcher(c + "").find()) {
						found = 0;
						sd.add(d.toString());
						d = d.delete(0, d.length());
						d.append("&" + c);
						continue;
					}
					if (found == 1) {
						found = 0;
						d.append("&" + c);
						continue;
					}
					found = 0;
					d.append(c);
				}
				if (d.length() != 0)
					sd.add(d.toString());
				d = d.delete(0, d.length());
				for (String ff : sd) {
					if (ff.toLowerCase().startsWith("&u")) {
						if(ff.contains("%message%") && msg!=null) {
							ff = StringUtils.color.colorize(ff.substring(2));
							ff=TabList.replace(ff, p, true);
							ff=ff.replace("%message%", r(msg, p));
							d.append(ff);
							continue;
						}
						ff = StringUtils.color.colorize(ff.substring(2));
					}
					d.append(TabList.replace(ff, p, true));
				}
				return d.toString();
		}
		if (msg != null)
			s=s.replace("%message%", r(msg.replace("%", "%%"), p));
		s = TabList.replace(s, p, true);
		return s;
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
		String msg = r(e.getMessage(), p);
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
		e.setMessage(msg);
		if (setting.lock_chat && !Loader.has(p, "ChatLock", "Other")) {
			e.setCancelled(true);
			Loader.sendMessages(p, "ChatLock.IsLocked");
			Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
					.add("%playername%", p.getDisplayName()).add("%message%", msg), Loader.getPerm("ChatLock", "Other"));
			return;
		}
		Iterator<Player> a = e.getRecipients().iterator();
		while(a.hasNext())
			if(PrivateMessageManager.getIgnoreList(a.next().getName()).contains(p.getName()))a.remove();
		if(Loader.config.getBoolean("Options.ChatNotification.Enabled")) {
			Sound sound = null;
			String[] title = new String[] {Loader.config.getString("Options.ChatNotification.Title"), Loader.config.getString("Options.ChatNotification.SubTitle")};
			String actionbar = Loader.config.getString("Options.ChatNotification.ActionBar").replace("%target%", p.getName()).replace("%targetname%", ChatFormatter.displayName(p)).replace("%targetcustomname%", ChatFormatter.customName(p));
			String color = Loader.config.getString("Options.ChatNotification.Color");
			try {
			sound = Sound.valueOf(Loader.config.getString("Options.ChatNotification.Sound"));
			}catch(Exception | NoSuchFieldError err) {}
			for(Player s : e.getRecipients()) {
				if(p.canSee(s) || p==s) {
					if(msg.contains(s.getName())) {
						String[] sp = msg.split(s.getName());
						String build = "";
						for(int i = 0; i < sp.length; ++i) {
							build+=sp[i]+color+s.getName()+(sp.length<i+1?sp[++i]:"");
						}
						msg=build;
						if(sound!=null)
						s.playSound(s.getLocation(), sound, 0, 0);
						if(!(title[0].trim().isEmpty() && title[1].trim().isEmpty()))
							TheAPI.sendTitle(s, TabList.replace(title[0], s, true), TabList.replace(title[1], s, true));
						if(!actionbar.trim().isEmpty())TheAPI.sendActionBar(s, TabList.replace(actionbar, s, true));
					}
				}
			}
		}
		if(Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("per_world")
				||Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("perworld")||
				Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("world")) {
			Iterator<Player> as = e.getRecipients().iterator();
			while(as.hasNext()) {
				Player s = as.next();
				if(p!=s && !s.hasPermission("SCR.Other.ChatTypeBypass")) {
					if(!p.getWorld().equals(s.getWorld()))as.remove();
				}
			}
		}
		if(Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("per_distance")
				||Loader.config.getString("Options.Chat.Type").equalsIgnoreCase("distance")) {
			Iterator<Player> as = e.getRecipients().iterator();
			double distance = Loader.config.getDouble("Options.Chat.Distance");
			while(as.hasNext()) {
				Player s = as.next();
				if(p!=s && !s.hasPermission("SCR.Other.ChatTypeBypass")) {
					if(!p.getWorld().equals(s.getWorld()))as.remove();
					else if(p.getLocation().distance(s.getLocation())>distance)as.remove();
				}
			}
		}
		if (Loader.config.getBoolean("Chat-Groups-Options.Enabled")) {
			Object format = ChatFormatter.chat(p, msg);
			if (format != null) {
				if(format instanceof String)
					e.setFormat((String)format);
				else
				if (Loader.config.getBoolean("Chat-Groups-Options.Json")) {
					@SuppressWarnings("unchecked")
					HoverMessage text = new HoverMessage((Map<String, Object>)format);
					text.send(e.getRecipients());
					e.getRecipients().clear(); //for our custom chat
				}
			}
		}
	}
}