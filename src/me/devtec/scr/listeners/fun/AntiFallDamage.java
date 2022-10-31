package me.devtec.scr.listeners.fun;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) {
			User user = API.getUser(e.getPlayer());
			user.flyAfterWorldChange(e.getPlayer().getAllowFlight());
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		User user = API.getUser(e.getPlayer());
		if (user.isFlyAfterWorldChange())
			e.getPlayer().setAllowFlight(true);
	}
}
