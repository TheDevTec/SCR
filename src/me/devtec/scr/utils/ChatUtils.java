package me.devtec.scr.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.functions.Tablist;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;

public class ChatUtils {

	// LOCK
	public static void lockChat() { // TODO
		if (Loader.data.getBoolean("chatlock", false))
			Loader.data.set("chatlock", false);
		else
			Loader.data.set("chatlock", true);
		Loader.data.save();
	}

	public static boolean isChatLocked() {
		return Loader.data.getBoolean("chatlock");
	}

	// CHATFORMAT
	public static class ChatFormat {

		private static Config c = Loader.chat;

		public static boolean isEnabled() {
			return Loader.chat.getBoolean("format.enabled");
		}

		/*
		 * format: global: group: <group> user: <user> world: <world>: repeat...
		 */
		public static String getPath(Player p) {
			String world = p.getLocation().getWorld().getName();

			if (c.exists("format.world." + world)) { // World formats:
				if (c.exists("format.world." + world + ".user." + p.getName()))
					return "format.world." + world + ".user." + p.getName();
				if (c.exists("format.world." + world + ".group." + Tablist.getVaultGroup(p)))
					return "format.world." + world + ".group." + Tablist.getVaultGroup(p);
				if (c.exists("format.world." + world + ".global"))
					return "format.world." + world + ".global";
			}

			if (c.exists("format.user." + p.getName()))
				return "format.user." + p.getName();
			if (c.exists("format.group." + Tablist.getVaultGroup(p)))
				return "format.group." + Tablist.getVaultGroup(p);
			if (c.exists("format.global")) {
			}

			return "format.global";
		}
	}

	// NOTIFICATION
	public static class Notification {

		public static boolean isEnabled() {
			return Loader.chat.getBoolean("chatNotification.enabled");
		}
	}

	public static class Colors {
		private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

		public static String remove(String string) {
			if (string != null)
				string = string.replace("§", "&");
			return string;
		}

		private static final Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9UuXx])");

		public static String colorize(String b, boolean sign, CommandSender dr) {
			String p = sign ? "sign" : "chat";
			if (Loader.config.getString("options.colors." + p + ".rainbow") != null && !Loader.config.getString("options.colors." + p + ".rainbow").equals("") && b.toLowerCase().contains("&u")
					&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".rainbow"))) {
				StringBuilder d = new StringBuilder(b.length());
				String[] split = fixedSplit.split(b);
				// atempt to add colors to split
				Matcher m = fixedSplit.matcher(b);
				int id = 1;
				while (m.find())
					try {
						split[id] = m.group(1) + split[id++];
					} catch (Exception err) {
					}
				// colors
				for (String ff : split) {
					String lower = ff.toLowerCase();
					if (lower.contains("§u") || lower.contains("&u"))
						ff = StringUtils.colorize(ff.replaceAll("[§&][Uu]", ""));
					d.append(ff);
				}
				b = d.toString();
			}
			if (Ref.isNewerThan(15)) {
				if (Loader.config.getString("options.colors." + p + ".gradient") != null && !Loader.config.getString("options.colors." + p + ".gradient").equals("")
						&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".gradient")))
					b = StringUtils.gradient(b);
				if (b.contains("#") || b.contains("&x"))
					if (Loader.config.getString("options.colors." + p + ".hex") != null && !Loader.config.getString("options.colors." + p + ".hex").equals("")
							&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".hex"))) {
						b = b.replaceAll("&[xX]", "§x");
						Matcher match = pattern.matcher(b);
						while (match.find()) {
							String color = match.group();
							StringBuilder magic = new StringBuilder("§x");
							char[] c = color.substring(1).toCharArray();
							for (char value : c)
								magic.append("§" + Character.toLowerCase(value));
							b = b.replace(color, magic.toString());
						}
					}
			}
			if (Loader.config.getString("options.colors." + p + ".color") != null && !Loader.config.getString("options.colors." + p + ".color").equals("")
					&& dr.hasPermission(Loader.config.getString("options.colors." + p + ".color"))) {
				for (int i = 0; i < 10; ++i)
					b = b.replace("&" + i, "§" + i);
				b = b.replaceAll("&([aAbBcCdDeEfF])", "§$1");
			}
			if (dr.hasPermission(Loader.config.getString("options.colors." + p + ".format")))
				b = b.replaceAll("&([oOlLmMnNrR])", "§$1");
			if (dr.hasPermission(Loader.config.getString("options.colors." + p + ".magic")))
				b = b.replaceAll("&[kK]", "§k");
			return b;
		}
	}
}
