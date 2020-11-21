package me.DevTec.ServerControlReloaded.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.TheAPI.Utils.Reflections.Ref;


public class Colors {
	private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
	
	public static String remove(String string) {
		if (string != null)
			string = ChatColor.stripColor(string);
		return string;
	}

	public static String colorize(String sr, boolean sign, CommandSender dr) {
		String p = "Chat";
		if (sign)
			p = "Sign";
		String b = sr;
		if(b.toLowerCase().contains("&u")) {
			if (dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Rainbow"))) {
				List<String> s = new ArrayList<>();
		    	StringBuffer d = new StringBuffer();
		    	int found = 0;
		    	for(char c : b.toCharArray()) {
		    		if(c=='&') {
		    			if(found==1)
		    			d.append(c);
		    			found=1;
			    		continue;
		    		}
		    		if(found==1 && Pattern.compile("[A-Fa-fUu0-9]").matcher(c+"").find()) {
			    		found=0;
				    	s.add(d.toString());
			    		d=d.delete(0, d.length());
			    		d.append("&"+c);
			    		continue;
		    		}
	    			if(found==1) {
	    	    		found=0;
			    		d.append("&"+c);
			    		continue;
	    			}
		    		found=0;
		    		d.append(c);
		    	}
		    	if(d.length()!=0)
		    		s.add(d.toString());
		    	d = d.delete(0, d.length());
		    	for(String ff : s) {
		    		if(ff.toLowerCase().startsWith("&u")) {
		    			ff=StringUtils.color.colorize(ff.substring(2));
		    		}
		    		d.append(ff);
		    	}
		    	b=d.toString();
			}
		}
		if (TheAPI.isNewerThan(15) && (b.contains("#") || b.contains("&x"))) {
			if (dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.HEX"))) {
			b = b.replace("&x", "§x");
			Matcher match = pattern.matcher(b);
            while (match.find()) {
                String color = match.group();
                StringBuilder magic = new StringBuilder("§x");
                char[] c = color.substring(1).toCharArray();
                for(int i = 0; i < c.length; ++i) {
                    magic.append(("&"+c[i]).toLowerCase());
                }
                b = b.replace(color, magic.toString() + "");
            }
			}
		}
		if (dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Color"))) {
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
		if(TheAPI.isNewerThan(15))
			if(dr.hasPermission(Loader.config.getString("Options.Colors." + p + ".Permission.Gradient"))) {
				b = gradient(b);
			}
		return b;
	}
	
	private static String gradient(String legacyMsg) {
        for (String code : Loader.colorMap.keySet()) {
            String rawCode = Loader.tagG + code;
            if (!legacyMsg.toLowerCase().contains(rawCode)) continue;
            legacyMsg = legacyMsg.replace(Loader.gradientTag + rawCode, Loader.gradientTag + Loader.colorMap.get(code));
        }
        List<String> hexes = new ArrayList<>();
        Matcher matcher = Pattern.compile(Loader.gradientTag+"#[A-Fa-f0-9]{6}").matcher(legacyMsg);
        while (matcher.find()) {
            hexes.add(matcher.group().replace(Loader.gradientTag, ""));
        }
        int hexIndex = 0;
        List<String> texts = Arrays.asList(legacyMsg.split(Loader.gradientTag+"#[A-Fa-f0-9]{6}"));
        StringBuilder finalMsg = new StringBuilder();
        for (String text : texts) {
            if (texts.get(0).equalsIgnoreCase(text)) {
                finalMsg.append(text);
                continue;
            }
            if (text.length() == 0) continue;
            if (hexIndex + 1 >= hexes.size()) {
                finalMsg.append(text);
                continue;
            }
            finalMsg.append((String)Ref.invokeNulled(StringUtils.class ,"gradient", text, hexes.get(hexIndex), hexes.get(hexIndex+1)));
            hexIndex++;
        }
        return finalMsg.toString();
    }
	
}