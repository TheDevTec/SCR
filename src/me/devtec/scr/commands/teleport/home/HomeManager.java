package me.devtec.scr.commands.teleport.home;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;

import me.devtec.shared.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.theapi.bukkit.game.Position;

public class HomeManager {

	public static HomeHolder find(UUID owner, String name) {
		Config user = API.getUser(owner);
		if(!user.exists("home."+name))return null;
		return new HomeHolder(owner, user.getString("home."+name.toLowerCase()+".name"), user.getAs("home."+name.toLowerCase()+".location", Position.class), user.getAs("home."+name.toLowerCase()+".icon", Material.class));
	}
	
	public static HomeHolder create(UUID owner, String name, Position location, Material icon) {
		HomeHolder warp = new HomeHolder(owner, name, location, icon);
		Config user = API.getUser(owner);
		user.set("home."+name.toLowerCase()+".name", name);
		user.set("home."+name.toLowerCase()+".location", location);
		user.set("home."+name.toLowerCase()+".icon", icon);
		user.save();
		return warp;
	}
	
	public static void delete(UUID owner, String name) {
		Config user = API.getUser(owner);
		user.remove("home."+name);
		user.save();
	}
	
	public static boolean existsHome(UUID owner, String name) {
		return API.getUser(owner).exists("home."+name);
	}
	
	public static Set<String> homesOf(UUID owner) {
		return API.getUser(owner).getKeys("home");
	}
}
