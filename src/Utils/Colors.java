package Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;

public class Colors {
	public static String remove(String string) {
		if (string != null)
			string = ChatColor.stripColor(string);
		return string;
	}

	public static String colorize(String s, boolean sign, CommandSender d) {
		String p = "Chat";
		if (sign)
			p = "Sign";
		String b = s;
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Color"))) {
			for (int i = 0; i < 9; ++i)
				b = b.replace("&" + i, "§" + i);
			b = b.replace("&a", "§a");
			b = b.replace("&b", "§b");
			b = b.replace("&c", "§c");
			b = b.replace("&d", "§d");
			b = b.replace("&e", "§e");
			b = b.replace("&f", "§f");
		}
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Format"))) {
			b = b.replace("&l", "§l");
			b = b.replace("&o", "§o");
			b = b.replace("&m", "§m");
			b = b.replace("&n", "§n");
		}
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Magic"))) {
			b = b.replace("&k", "§k");
		}
		return b;
	}
}