package me.devtec.scr.commands.tpsystem;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TpSystem {

	
	public static void teleport(Player who, Player to) {
		//TODO - safe TP
	}
	
	public static void teleport(Player who, Location to) {
		//TODO - safe TP ?
		setBack(who);
		who.teleport(to);
	}
	
	public static void setBack(Player player) {
		
	}

	public static void askTpaHere(Player who, Player to) {
		
		
	}
	
}
