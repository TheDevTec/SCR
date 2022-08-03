package me.devtec.scr.listeners.additional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.scr.functions.ScoreboardManager;

public class SbJoinQuit implements Listener {

	ScoreboardManager tablist;

	public SbJoinQuit(ScoreboardManager tablist) {
		this.tablist = tablist;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		tablist.fullyUnregister(e.getPlayer());
	}
}
