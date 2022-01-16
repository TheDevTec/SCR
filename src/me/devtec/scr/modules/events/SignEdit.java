package me.devtec.scr.modules.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignEdit implements Listener {

	@EventHandler
	public void onSign(SignChangeEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();

		int slot = 0;
		for(String line : event.getLines()) {
			//TODO rules
			event.setLine(slot++, line);
		}
	}
}
