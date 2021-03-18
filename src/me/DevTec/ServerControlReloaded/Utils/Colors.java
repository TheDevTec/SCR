package me.DevTec.ServerControlReloaded.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;


public class Colors {
	private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
	
	public static String remove(String string) {
		if (string != null)
			string = ChatColor.stripColor(string);
		return string;
	}
	
	private static boolean neww = TheAPI.isNewerThan(15);
	private static Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9UuXx])");
	
	public static String colorize(String b, boolean sign, CommandSender dr) {
		String p = sign?"Sign":"Chat";
		if ((Loader.config.getString("Options.Colors." + p + ".Permission.Rainbow")!=null && !Loader.config.getString("Options.Colors." + p + ".Permission.Rainbow").equals("")) && b.toLowerCase().contains("&u") && dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Rainbow"))) {
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
				if (ff.toLowerCase().contains("§u")||ff.toLowerCase().contains("&u"))
					ff = StringUtils.colorize(StringUtils.color.colorize(ff.replaceAll("[§&][Uu]","")));
				d.append(ff);
			}
			b=d.toString();
		}
		if(neww) {
			if((Loader.config.getString("Options.Colors." + p + ".Permission.Gradient")!=null && !Loader.config.getString("Options.Colors." + p + ".Permission.Gradient").equals("")) && dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Gradient")))
				b = StringUtils.gradient(b);
			if (b.contains("#") || b.contains("&x")) {
				if ((Loader.config.getString("Options.Colors." + p + ".Permission.HEX")!=null && !Loader.config.getString("Options.Colors." + p + ".Permission.HEX").equals("")) && dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.HEX"))) {
					b = b.replace("&x", "§x").replace("&X", "§x");
					Matcher match = pattern.matcher(b);
		            while (match.find()) {
		                String color = match.group();
		                StringBuilder magic = new StringBuilder("§x");
		                char[] c = color.substring(1).toCharArray();
		                for(int i = 0; i < c.length; ++i) {
		                    magic.append(("§"+c[i]).toLowerCase());
		                }
		                b = b.replace(color, magic.toString() + "");
		            }
				}
			}
		}
		if ((Loader.config.getString("Options.Colors." + p + ".Permission.Color")!=null && !Loader.config.getString("Options.Colors." + p + ".Permission.Color").equals("")) && dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Color"))) {
			for (int i = 0; i < 10; ++i)
				b = b.replace("&" + i, ChatColor.getByChar(i+"")+"");
			b = b.replace("&a", ChatColor.getByChar("a")+"");
			b = b.replace("&b", ChatColor.getByChar("b")+"");
			b = b.replace("&c", ChatColor.getByChar("c")+"");
			b = b.replace("&d", ChatColor.getByChar("d")+"");
			b = b.replace("&e", ChatColor.getByChar("e")+"");
			b = b.replace("&f", ChatColor.getByChar("f")+"");
		}
		if (dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Format"))) {
			b = b.replace("&l", ChatColor.getByChar("l")+"");
			b = b.replace("&o", ChatColor.getByChar("o")+"");
			b = b.replace("&m", ChatColor.getByChar("m")+"");
			b = b.replace("&n", ChatColor.getByChar("n")+"");
			b = b.replace("&r", ChatColor.getByChar("r")+"");
		}
		if (dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Magic"))) {
			b = b.replace("&k", ChatColor.getByChar("k")+"");
		}
		return b;
	}
}