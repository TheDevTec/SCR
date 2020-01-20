package Utils;

import org.bukkit.ChatColor;
public class Colors{
public static String remove(String string) {
	if(string!=null)string=ChatColor.stripColor(string);
    return string;
}}