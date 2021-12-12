package me.devtec.servercontrolreloaded.utils.displaymanager;

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
		bar = Bukkit.createBossBar(title, BarColor.PURPLE, BarStyle.SEGMENTED_20);
	}
	
	@Override
	public void setTitle(String title) {
		bar.setTitle(title);
	}

	@Override
	public void setProgress(double progress) {
		bar.setProgress(progress);
	}

	@Override
	public void setStyle(String styleName) {
		bar.setStyle(BarStyle.valueOf(styleName));
	}

	@Override
	public void setColor(String colorName) {
		bar.setColor(BarColor.valueOf(colorName));
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
