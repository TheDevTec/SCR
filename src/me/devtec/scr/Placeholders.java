package me.devtec.scr;

import org.bukkit.entity.Player;

import me.devtec.theapi.placeholderapi.ThePlaceholder;

public class Placeholders extends ThePlaceholder {

	public Placeholders(String name) {
		super(name);
	}

	@Override
	public String onRequest(Player s, String args) {
		return null;
	}
	
}
