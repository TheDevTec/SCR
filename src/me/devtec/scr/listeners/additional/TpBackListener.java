package me.devtec.scr.listeners.additional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.devtec.scr.commands.tpsystem.TpSystem;

public class TpBackListener implements Listener {

	@EventHandler
	public void onTp(PlayerTeleportEvent e) {
		if(!e.isCancelled())
		TpSystem.setBack(e.getPlayer());
	}
}
