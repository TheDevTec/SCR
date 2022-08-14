package me.devtec.scr.utils;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.functions.Tablist;
import me.devtec.shared.dataholder.Config;

public class ChatUtils {

	// LOCK
	public static void lockChat() { // TODO
		if (Loader.data.getBoolean("chatlock", false))
			Loader.data.set("chatlock", false);
		else
			Loader.data.set("chatlock", true);
		Loader.data.save();

	}

	public static boolean isChatLocked() { // TODO
		return Loader.data.getBoolean("chatlock", false);
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
}
