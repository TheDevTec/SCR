package me.devtec.scr.modules.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.datakeeper.User;

public class JoinQuit implements Listener {
	Map<UUID, User> cache = new HashMap<>();
	
	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent event) {
		if(event.getLoginResult()!=Result.ALLOWED)return;
		cache.put(event.getUniqueId(), TheAPI.getUser(event.getName(), event.getUniqueId())); //load or create user data
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		//TODO join message
		User user = cache.remove(event.getPlayer().getUniqueId());
		user.set("informations.lastOnline", System.currentTimeMillis()/1000);
		if(user.getBoolean("informations.flying") && event.getPlayer().hasPermission("scr.command.fly.onjoin")) {
			event.getPlayer().setAllowFlight(true);
			event.getPlayer().setFlying(true);
		}
		event.getPlayer().setWalkSpeed(user.getFloat("informations.walkspeed"));
		event.getPlayer().setFlySpeed(user.getFloat("informations.flyspeed"));
	}

	@EventHandler
	public void onJoin(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		//TODO quit message
		User user = TheAPI.getUser(event.getPlayer());
		user.set("informations.lastOnline", System.currentTimeMillis()/1000);
		user.set("informations.position", new Position(event.getPlayer()).toString());
		user.set("informations.flying", event.getPlayer().isFlying());
		user.set("informations.flyspeed", event.getPlayer().getFlySpeed());
		user.set("informations.walkspeed", event.getPlayer().getWalkSpeed());
	}
}
