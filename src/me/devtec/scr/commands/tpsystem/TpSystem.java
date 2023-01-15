package me.devtec.scr.commands.tpsystem;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.devtec.shared.dataholder.Config;
import me.devtec.theapi.bukkit.game.Position;

public class TpSystem {

	public static void teleport(Player who, Player to) {
		// TODO - safe TP ?
		who.teleport(to);
	}

	public static void teleport(Player who, Location to) {
		// TODO - safe TP ?
		who.teleport(to);
	}

	public static void setBack(Player player) {
		Config c = me.devtec.shared.API.getUser(player.getUniqueId());
		c.set("back", new Position(player.getLocation()));
		c.save();
	}

	public static void teleportBack(Player player) {
		Position pos = me.devtec.shared.API.getUser(player.getUniqueId()).getAs("back", Position.class);
		player.teleport(pos.toLocation());
	}
}
