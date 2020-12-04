package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import me.DevTec.ServerControlReloaded.Commands.Message.PrivateMessageManager;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Item;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.Colors;
import me.DevTec.ServerControlReloaded.Utils.MultiWorldsGUI;
import me.DevTec.ServerControlReloaded.Utils.Rule;
import me.DevTec.ServerControlReloaded.Utils.TabList;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.PlaceholderAPI.PlaceholderAPI;
import me.DevTec.TheAPI.Utils.DataKeeper.User;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;


public class ChatFormat implements Listener {
	static Loader plugin = Loader.getInstance;

	public static String r(Player p, String s, String msg) {
		s=s.replace("&u", "&<U>");
		s = TabList.replace(s, p, false);
		if (msg != null)
			s=s.replace("%message%", r(msg.replaceAll("&([0-9A-Fa-fK-Ok-oRrUuXx])", "&<$1$1>").replaceAll("#([0-9A-Fa-f]{6})", "#<$1>"), p));
		s=s.replace("&<U>", "&u");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.Rainbow")))
			s=s.replaceAll("&<[Uu]{2}>", "&u");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.Color")))
			s=s.replaceAll("&<([0-9A-Fa-f]){2}>", "&$1");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.Magic")))
			s=s.replaceAll("&<[Kk]{2}>", "&k");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.Format")))
			s=s.replaceAll("&<([L-Ol-o]){2}>", "&$1");
		if(p.hasPermission(Loader.config.getString("Options.Colors.Chat.Permission.HEX")))
			s=s.replaceAll("#<([0-9A-Fa-f]{6})>", "#$1").replaceAll("&<([Xx]){2}>", "&x");
		s=TheAPI.colorize(s);
		s=s.replaceAll("&<([0-9A-Fa-fK-Ok-oRrUuXx]){2}>", "&$1").replaceAll("#<([0-9A-Fa-f]{6})>", "#$1");
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
		for (int i = 0; i < string.length(); i++)
			if (Character.isAlphabetic(string.charAt(i)) && Character.isUpperCase(string.charAt(i)))
				upperCaseCount++;
		return upperCaseCount;
	}

	private String removeDoubled(String s) {
		char prevchar = 0;
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (prevchar != c)
				sb.append(c);
			prevchar = c;
		}
		return sb.toString();
	}

	private int countDoubled(String s) {
		return s.length() - removeDoubled(s).length();
	}

	static UnsortedMap<Player, String> old = new UnsortedMap<Player, String>();

	private boolean isSim(Player p, String msg) {
		if (Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
			if (old.containsKey(p)) {
				String o = old.get(p);
				old.remove(p);
				old.put(p, msg);
				if (o.length() >= 5 && msg.length() >= o.length()) {
					String f = o.substring(1, o.length() - 1);
					return o.equalsIgnoreCase(msg) || msg.startsWith(o) || f.startsWith(msg) || f.equalsIgnoreCase(msg);
				}
			} else
				old.put(p, msg);
		}
		return false;
	}
	
	@EventHandler
	public void set(PlayerChatEvent e) {
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
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
		if (!p.hasPermission("SCR.Admin")) {
		for (Rule rule : Loader.rules) {
			if(!Loader.events.getStringList("onChat.Rules").contains(rule.getName()))continue;
			msg = rule.apply(msg);
			if (msg == null) break;
		}
		if (msg == null) {
			e.setCancelled(true);
			return;
		}
		String message = msg;
		String d = ""; // anti doubled letters
		int up = 0; // anti caps
		if (setting.spam_double) {
			if (message.split(" ").length == 0) {
				if (!is(message)) {
					up = up + count(message);
					d = d + " " + (countDoubled(message) >= 5 ? removeDoubled(message) : message);
				} else
					d = d + " " + message;
			} else
				for (String s : message.split(" ")) {
					if (!is(s)) {
						up = up + count(s);
						d = d + " " + (countDoubled(s) >= 5 ? removeDoubled(s) : s);
					} else
						d = d + " " + s;
				}
			d = d.replaceFirst(" ", "");
		} else
			d = message;
		String build = d;
		if (setting.caps_chat) {
			if (up != 0
					? up / ((double) d.length() / 100) >= 60 && !p.hasPermission("SCR.Caps") && d.length() > 5
					: false) {
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
		if (Loader.config.getBoolean("SpamWords.SimiliarMessage")) {
			if (isSim(p, e.getMessage())) {
				e.setCancelled(true);
				return;
			}
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
						.replace("%sendername%", TheAPI.getPlayerOrNull(p.getName())!=null?TheAPI.getPlayerOrNull(p.getName()).getDisplayName():p.getName()).replace("%message%", msg), Loader.cmds.exists("Message.Helpop.SubPermissions.Receive")?Loader.cmds.getString("Message.Helpop.SubPermissions.Receive"):"SCR.Command.Helpop.Receive");
				if (!Loader.has(p, "Helpop", "Message", "Receive"))
					TheAPI.msg(Loader.config.getString("Format.HelpOp").replace("%sender%", p.getName()).replace("%sendername%", TheAPI.getPlayerOrNull(p.getName())!=null?TheAPI.getPlayerOrNull(p.getName()).getDisplayName():p.getName()).replace("%message%", msg), p);
			}
			e.setCancelled(true);
			return;
		}
		e.setMessage(msg);
		if (setting.lock_chat && !p.hasPermission("ServerControl.ChatLock")) {
			e.setCancelled(true);
			Loader.sendMessages(p, "ChatLock.IsLocked");
			Loader.sendBroadcasts(p, "ChatLock.Message", Placeholder.c().add("%player%", p.getName())
					.add("%playername%", p.getDisplayName()).add("%message%", e.getMessage()), "ServerControl.ChatLock.Notify");
			return;
		}
		if (Loader.config.getBoolean("Chat-Groups-Enabled")) {
			String format = PlaceholderAPI.setPlaceholders(p, Loader.config.getString("Chat-Groups." + Loader.get(p,Item.GROUP) + ".Chat"));
			if (format != null) {
				format=(r(p, format, msg));
				e.setFormat(format.replace("%", "%%"));
			}
		}

	}
}