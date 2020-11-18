package me.DevTec.ServerControlReloaded.Events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.Rule;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;

/**
 * 6.10. 2020
 * 
 * @author StraikerinaCZ
 *
 */

public class SecurityListenerV3 implements Listener {
	public static enum Security {
		Spam, Swear
	}

	public static enum ALocation {
		Chat, Command
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

	static HashMap<Player, String> old = new HashMap<Player, String>();

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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		for (Rule rule : Loader.rules) {
			msg = rule.apply(msg);
			if (msg == null) break;
		}
		if (msg == null) {
			e.setCancelled(true);
			return;
		}
		e.setMessage(msg);
		if (!p.hasPermission("ServerControl.CommandsAccess") && setting.cmdblock) {
			for (String cen : Loader.config.getStringList("Options.CommandsBlocker.List")) {
				String mes = e.getMessage().toLowerCase();
				if (mes.startsWith("/" + cen.toLowerCase()) || mes.startsWith("/bukkit:" + cen.toLowerCase())
						|| mes.startsWith("/minecraft:" + cen.toLowerCase())) {
						e.setCancelled(true);
				}
			}
		} else if (!p.hasPermission("ServerControl.Admin")) {
			String message = e.getMessage();
			String d = ""; // anti doubled letters
			int up = 0; // anti caps
			if (setting.spam_double) {
				for (String s : message.split(" ")) {
					if (!is(s)) {
						up = up + count(s);
						d = d + " " + removeDoubled(s);
					} else
						d = d + " " + s;
				}
			}
			d = d.replaceFirst(" ", "");
			String build = d;
			if (setting.caps_cmd) {
				build = "";
				if ((up / d.length()) * 100 >= 60 && !p.hasPermission("ServerControl.Caps") && d.length() > 5) {
					for (String s : d.split(" ")) {
						if (!is(s)) {
							build = build + " " + s.toLowerCase();
						} else
							build = build + " " + s;

					}

					build = build.replaceFirst(" ", "");
				}
			}
			e.setMessage(build);
		}
		
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		for (Rule rule : Loader.rules) {
			msg = rule.apply(msg);
			if (msg == null) break;
		}
		if (msg == null) {
			e.setCancelled(true);
			return;
		}
		e.setMessage(msg);
		if (!p.hasPermission("ServerControl.Admin")) {
			String message = e.getMessage();
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
						? up / ((double) d.length() / 100) >= 60 && !p.hasPermission("ServerControl.Caps")
								&& d.length() > 5
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
			e.setMessage(message);
		}
	}
}