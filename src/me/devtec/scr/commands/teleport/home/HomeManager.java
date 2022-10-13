package me.devtec.scr.commands.teleport.home;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.functions.Tablist;
import me.devtec.shared.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.theapi.bukkit.game.Position;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class HomeManager {

	public static HomeHolder find(UUID owner, String name) {
		Config user = API.getUser(owner);
		if (!user.exists("home." + name) || user.getAs("home." + name.toLowerCase() + ".location", Position.class) == null)
			return null;
		return new HomeHolder(owner, user.getString("home." + name.toLowerCase() + ".name"), user.getAs("home." + name.toLowerCase() + ".location", Position.class),
				XMaterial.matchXMaterial(user.getString("home." + name.toLowerCase() + ".icon")).orElse(XMaterial.OAK_DOOR));
	}

	public static HomeHolder create(UUID owner, String name, Position location, XMaterial icon) {
		HomeHolder warp = new HomeHolder(owner, name, location, icon);
		Config user = API.getUser(owner);
		user.set("home." + name.toLowerCase() + ".name", name);
		user.set("home." + name.toLowerCase() + ".location", location);
		user.set("home." + name.toLowerCase() + ".icon", icon.name());
		user.save();
		return warp;
	}

	public static void delete(UUID owner, String name) {
		Config user = API.getUser(owner);
		user.remove("home." + name);
		user.save();
	}

	public static boolean existsHome(UUID owner, String name) {
		return API.getUser(owner).exists("home." + name);
	}

	public static Set<String> homesOf(UUID owner) {
		if (owner == null)
			return null;
		return API.getUser(owner).getKeys("home");
	}

	public static int getLimit(Player p) {
		if (p.hasPermission(Loader.commands.getString("sethome.permission.unlimited_homes")))
			return Integer.MAX_VALUE;
		String group = Tablist.getVaultGroup(p);
		if (group == null)
			return Loader.config.getInt("homelimit.default");
		if (Loader.config.exists("homelimit." + group))
			return Loader.config.getInt("homelimit." + group);

		return Loader.config.getInt("homelimit.default");
	}
}
