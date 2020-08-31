package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ServerControl.Loader;
import me.DevTec.TheAPI.TheAPI;


public class Colors {
	
	private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
	
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

		if (TheAPI.isNewerThan(15) && b.contains("#")) {
			if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Hex"))) {
				b=b.replace("&x", "§x");
				Matcher match = pattern.matcher(b);
			    while (match.find()) {
			        String color = match.group();
			        StringBuilder magic = new StringBuilder("§x");
			        char[] c = color.substring(1).toCharArray();
			        for(int i = 0; i < c.length; ++i)magic.append(("&"+c[i]).toLowerCase());
			        b = b.replace(color, magic.toString() + "");
			    }
			}
		}
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Color"))) {
			for (int i = 0; i < 10; ++i)
				b = b.replace("&" + i, ChatColor.getByChar(i+"")+"");
			b = b.replace("&a", ChatColor.getByChar("a")+"");
			b = b.replace("&b", ChatColor.getByChar("b")+"");
			b = b.replace("&c", ChatColor.getByChar("c")+"");
			b = b.replace("&d", ChatColor.getByChar("d")+"");
			b = b.replace("&e", ChatColor.getByChar("e")+"");
			b = b.replace("&f", ChatColor.getByChar("f")+"");
			b = b.replace("&r", ChatColor.getByChar("r")+"");
		}
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Format"))) {
			b = b.replace("&l", ChatColor.getByChar("l")+"");
			b = b.replace("&o", ChatColor.getByChar("o")+"");
			b = b.replace("&m", ChatColor.getByChar("m")+"");
			b = b.replace("&n", ChatColor.getByChar("n")+"");
		}
		if (d.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Magic"))) {
			b = b.replace("&k", ChatColor.getByChar("k")+"");
		}
		
		return b;
	}
	
}