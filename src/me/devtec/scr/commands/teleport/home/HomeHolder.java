package me.devtec.scr.commands.teleport.home;

import java.util.UUID;

import me.devtec.theapi.bukkit.game.Position;
import me.devtec.theapi.bukkit.xseries.XMaterial;

public class HomeHolder {
	private String name;
	private Position loc;

	// gui
	XMaterial icon;

	// additional - warp owner
	private UUID owner;

	public HomeHolder(UUID owner, String name, Position loc, XMaterial icont) {
		this.owner = owner;
		this.name = name;
		this.loc = loc;
	}

	public String name() {
		return name;
	}

	public XMaterial icon() {
		return icon;
	}

	public Position location() {
		return loc;
	}

	public UUID owner() {
		return owner;
	}
}