package me.devtec.scr.listeners.fun;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.fun.Fly;

public class AntiFallDamage implements Listener {

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER && e.getCause() == DamageCause.FALL && Fly.antiFall.contains(e.getEntity().getUniqueId())) {
			e.setDamage(0);
			e.setCancelled(true);
			Fly.antiFall.remove(e.getEntity().getUniqueId());
		}
	}

	/**
	 * Re-allowing fly when change gamemode/world or respawn
	 */

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		User user = API.getUser(e.getPlayer());
		if (user.fly())
			Fly.apply(e.getPlayer(), false); // false - turning on
	}

	@EventHandler
	public void onWorldChange(PlayerRespawnEvent e) {
		User user = API.getUser(e.getPlayer());
		if (user.fly())
			Fly.apply(e.getPlayer(), false); // false - turning on
	}

	@EventHandler
	public void onWorldChange(PlayerGameModeChangeEvent e) {
		User user = API.getUser(e.getPlayer());
		if (user.fly())
			Fly.apply(e.getPlayer(), false); // false - turning on
	}
}
