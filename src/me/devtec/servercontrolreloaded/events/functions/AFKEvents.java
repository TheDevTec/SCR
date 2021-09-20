package me.devtec.servercontrolreloaded.events.functions;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.Loader;

public class AFKEvents implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(PlayerInteractEvent e) {
		Loader.getInstance.save(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(PlayerCommandPreprocessEvent e) {
		if(e.isCancelled())return;
		if(CommandsManager.isLoaded("Other", "AFK")) {
			String c = e.getMessage().substring(1);
			if(c.toLowerCase().startsWith(Loader.cmds.getString("Other.AFK.Name").toLowerCase()))return;
			Object dd = Loader.cmds.get("Other.AFK.Aliases");
			if(dd instanceof Collection) {
			for(Object cmd : (Collection<?>)dd)
				if(c.toLowerCase().startsWith((cmd+"").toLowerCase()))return;
			}else
			if(c.toLowerCase().startsWith((dd+"").toLowerCase()))return;
		}
		Loader.getInstance.save(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(AsyncPlayerChatEvent e) {
		Loader.getInstance.save(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreakBlock(InventoryClickEvent e) {
		Loader.getInstance.save((Player)e.getWhoClicked());
	}
}
