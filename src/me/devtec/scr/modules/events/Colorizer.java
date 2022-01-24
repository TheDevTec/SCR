package me.devtec.scr.modules.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;


public class Colorizer {
	private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
	
	public static String remove(String string) {
		if (string != null)
			string = string.replace("§", "&");
		return string;
	}
	
	private static final boolean neww = TheAPI.isNewerThan(15);
	private static final Pattern fixedSplit = Pattern.compile("(#[A-Fa-f0-9]{6}|[&§][Xx]([&§][A-Fa-f0-9]){6}|[&§][A-Fa-f0-9UuXx])");
	
	public static String process(CommandSender sender, String text) {
		if (text.contains("&u") && sender.hasPermission("scr.color.chat.rainbow")) {
			StringBuilder d = new StringBuilder(text.length());
			String[] split = fixedSplit.split(text);
			//atempt to add colors to split
			Matcher m = fixedSplit.matcher(text);
			int id = 1;
			while(m.find()) {
				try {
				split[id]=m.group(1)+split[id++];
				}catch(Exception err) {
				}
			}
			//colors
			for (String ff : split) {
				String lower = ff.toLowerCase();
				if (lower.contains("§u")||lower.contains("&u"))
					ff = StringUtils.color.colorize(ff.replaceAll("[§&][Uu]",""));
				d.append(ff);
			}
			text=d.toString();
		}
		if(neww) {
			if(sender.hasPermission("scr.color.chat.gradient"))
				text = StringUtils.gradient(text);
			if ((text.contains("#") || text.contains("&x")) && sender.hasPermission("scr.color.chat.hex")) {
				text = text.replaceAll("&[xX]", "§x");
				Matcher match = pattern.matcher(text);
	            while (match.find()) {
	                String color = match.group();
	                StringBuilder magic = new StringBuilder("§x");
	                char[] c = color.substring(1).toCharArray();
					for (char value : c) magic.append("§" + Character.toLowerCase(value));
					text = text.replace(color, magic.toString());
	            }
			}
		}
		if (sender.hasPermission("scr.color.chat.color")) {
				for (int i = 0; i < 10; ++i)
					text = text.replace("&" + i, "§" + i);
				text = text.replaceAll("&([aAbBcCdDeEfF])", "§$1");
		}
		if (sender.hasPermission("scr.color.chat.format")) {
			text = text.replaceAll("&([oOlLmMnNrR])", "§$1");
		}
		if (sender.hasPermission("scr.color.chat.magic")) {
			text = text.replaceAll("&[kK]", "§k");
		}
		return text;
	}
}