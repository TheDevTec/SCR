package me.devtec.scr.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devtec.shared.placeholders.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {

	public Placeholders(String name) {
		super(name);
	}

	@Override
	public String apply(String args, UUID uuid) {
		Player s = Bukkit.getPlayer(uuid);
		if(s!=null) {
			
		}
		return null;
	}
	
}
