package me.devtec.scr.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.devtec.scr.Loader;
import me.devtec.scr.commands.info.AFK;

public class AFKListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) {
		if(AFK.players.containsKey(e.getPlayer().getName())) {
			AFK.afk(e.getPlayer(), false);
		}
		AFK.update(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent e) {
		if(AFK.players.containsKey(e.getPlayer().getName())) {
			AFK.afk(e.getPlayer(), false);
		}
		AFK.update(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if(AFK.players.containsKey(e.getPlayer().getName())) {
			AFK.afk(e.getPlayer(), false);
		}
		AFK.update(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(InventoryClickEvent e) {
		if(AFK.players.containsKey(e.getWhoClicked().getName())) {
			AFK.afk(e.getWhoClicked(), false);
		}
		AFK.update(e.getWhoClicked());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled()) return;
		if(Loader.commands.getBoolean("afk.enabled")) {
			String cmd = e.getMessage().substring(1);
			for(String afk : Loader.commands.getStringList("afk.cmds"))
				if(cmd.toLowerCase().startsWith( afk.toLowerCase()) )
					return;
		}
		if(AFK.players.containsKey(e.getPlayer().getName())) {
			AFK.afk(e.getPlayer(), false);
		}
		AFK.update(e.getPlayer());
	}
}
