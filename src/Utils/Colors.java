package Utils;

import org.bukkit.ChatColor;
public class Colors{
public static String remove(String string) {
	String color = string;
	if(color!=null)color=ChatColor.stripColor(string);
    return color;
}}