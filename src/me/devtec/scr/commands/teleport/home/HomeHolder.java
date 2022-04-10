package me.devtec.scr.commands.teleport.home;

import java.util.UUID;

import org.bukkit.Material;

import me.devtec.theapi.bukkit.game.Position;

public class HomeHolder {
	private String name;
	private Position loc;
	
	//gui
	Material icon;
	
	//additional - warp owner
	private UUID owner;
	
	public HomeHolder(UUID owner, String name, Position loc, Material icont) {
		this.owner=owner;
		this.name=name;
		this.loc=loc;
	}
	
	public String name() {
		return name;
	}
	
	public Material icon() {
		return icon;
	}
	
	public Position location() {
		return loc;
	}
	
	public UUID owner() {
		return owner;
	}
}