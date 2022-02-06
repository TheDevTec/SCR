package me.devtec.scr.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.devtec.theapi.utils.StringUtils;

public class Formatters {

	public static String formatDouble(double health) {
		//TODO - custom
		return StringUtils.fixedFormatDouble(health);
	}

	public static Location locFromString(String string) {
		String[] split = string.split(",");
		return new Location(Bukkit.getWorld(split[0]), StringUtils.getDouble(split[1]), StringUtils.getDouble(split[2]), StringUtils.getDouble(split[3]), StringUtils.getFloat(split[4]), StringUtils.getFloat(split[5]));
	}

	public static String locToString(Location loc) {
		return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getYaw()+","+loc.getPitch();
	}

}
