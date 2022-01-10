package me.devtec.scr.modules.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;

public class ModernBossBar implements SBossBar {

	public BossBar bar;
	private Player player;
	public ModernBossBar(Player player, String title) {
		this.player=player;
		bar = Bukkit.createBossBar(TheAPI.colorize(title), BarColor.PURPLE, BarStyle.SEGMENTED_20);
		bar.addPlayer(player);
	}
	
	@Override
	public void setTitle(String title) {
		bar.setTitle(TheAPI.colorize(title));
	}

	@Override
	public void setProgress(double progress) {
		bar.setProgress(progress);
	}

	@Override
	public void setStyle(String styleName) {
		styleName=styleName.toUpperCase();
		if(styleName.equals("RANDOM"))setRandomStyle();
		else
			try {
				bar.setStyle(BarStyle.valueOf(styleName));
			}catch(NoSuchFieldError er) {
				try {
					bar.setStyle(BarStyle.valueOf(fromLegacy(styleName)));
				}catch(NoSuchFieldError err) {}
		}
	}
	private String fromLegacy(String styleName) {
		switch(styleName) {
		case "PROGRESS":
			return "SOLID";
		case "NOTCHED_6":
			return "SEGMENTED_6";
		case "NOTCHED_10":
			return "SEGMENTED_10";
		case "NOTCHED_12":
			return "SEGMENTED_12";
		case "NOTCHED_20":
			return "SEGMENTED_20";
		}
		return null;
	}

	@Override
	public void setColor(String colorName) {
		colorName=colorName.toUpperCase();
		if(colorName.equals("RANDOM"))setRandomColor();
		else
			try {
				bar.setColor(BarColor.valueOf(colorName));
			}catch(NoSuchFieldError err) {}
	}

	@Override
	public void setRandomStyle() {
		setStyle(BarStyle.values()[TheAPI.generateRandomInt(BarStyle.values().length-1)].name());
	}

	@Override
	public void setRandomColor() {
		setColor(BarColor.values()[TheAPI.generateRandomInt(BarColor.values().length-1)].name());
	}

	@Override
	public boolean isVisible() {
		return bar.getPlayers().contains(player);
	}

	@Override
	public void hide() {
		bar.removePlayer(player);
	}

	@Override
	public void show() {
		bar.addPlayer(player);
	}

	@Override
	public void remove() {
		bar.removeAll();
	}
}
