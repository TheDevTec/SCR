package me.devtec.scr.commands.teleport.warp;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;

public class WarpHolder {
	private String name;
	private Location loc;
	
	//economy
	private double cost;
	//perms
	private String perm;
	
	//additional - warp owner
	private UUID owner;
	
	public WarpHolder(UUID owner, String name, Location loc, String perm, double cost) {
		this.owner=owner;
		this.name=name;
		this.perm=perm;
		this.cost=cost;
		this.loc=loc;
	}
	
	public int canTeleport(Player target) {
		if(!owner.equals(target.getUniqueId())) {
			if(perm!=null && !target.hasPermission(perm))return 1;
			if(Loader.economy != null && cost > 0 && !Loader.economy.has(target, cost))return 2;
		}
		return 0;
	}

	public String name() {
		return name;
	}
	
	public Location location() {
		return loc;
	}
	
	public UUID owner() {
		return owner;
	}
	
	public String permission() {
		return perm;
	}
	
	public double cost() {
		return cost;
	}
}
