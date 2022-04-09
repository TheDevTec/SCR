package me.devtec.scr.commands.teleport.warp;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.theapi.bukkit.game.Position;
import net.milkbowl.vault.economy.Economy;

public class WarpHolder {
	private String name;
	private Position loc;
	
	//economy
	private double cost;
	//perms
	private String perm;
	//gui
	Material icon;
	
	//additional - warp owner
	private UUID owner;
	
	public WarpHolder(UUID owner, String name, Position loc, Material icon, String perm, double cost) {
		this.owner=owner;
		this.name=name;
		this.perm=perm;
		this.cost=cost;
		this.loc=loc;
	}
	
	public int canTeleport(Player target) {
		if(owner == null || !owner.equals(target.getUniqueId())) {
			if(perm!=null && !target.hasPermission(perm))return 1;
			if(Loader.economy != null && cost > 0 && !((Economy)Loader.economy).has(target, cost))return 2;
		}
		return 0;
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
	
	public String permission() {
		return perm;
	}
	
	public double cost() {
		return cost;
	}
}
