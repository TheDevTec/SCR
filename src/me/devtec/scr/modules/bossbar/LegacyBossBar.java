package me.devtec.scr.modules.bossbar;

import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.bossbar.BossBar;

public class LegacyBossBar implements SBossBar {

	public BossBar bar;
	public LegacyBossBar(Player player, String title, double progress) {
		bar = new BossBar(player, title, progress);
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
		
	}

	@Override
	public void setColor(String colorName) {
		
	}

	@Override
	public void setRandomStyle() {
		
	}

	@Override
	public void setRandomColor() {
		
	}

	@Override
	public boolean isVisible() {
		return !bar.isHidden();
	}

	@Override
	public void hide() {
		bar.hide();;
	}

	@Override
	public void show() {
		bar.show();
	}

	@Override
	public void remove() {
		bar.remove();
	}
}
