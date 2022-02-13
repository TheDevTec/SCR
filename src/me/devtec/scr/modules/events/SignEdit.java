package me.devtec.scr.modules.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.devtec.scr.utils.Rule;

public class SignEdit implements Listener {
	
	public static List<Rule> rules = new ArrayList<>();

	@EventHandler
	public void onSign(SignChangeEvent event) {
		if(event.isCancelled())return;
		Player s = event.getPlayer();

		int slot = 0;
		for(String text : event.getLines()) {
			for(Rule rule : rules) {
				text=rule.apply(text, s);
				if(text==null)break;
			}
			event.setLine(slot++, text);
		}
	}
}
