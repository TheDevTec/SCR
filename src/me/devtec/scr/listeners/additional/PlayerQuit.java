package me.devtec.scr.listeners.additional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.fun.Fly;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PlayerQuit implements Listener {
	public static Config config;

	public PlayerQuit(Config config) {
		PlayerQuit.config = config;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		for (String command : config.getStringList("commands"))
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
		User user = API.getUser(event.getPlayer().getUniqueId());
		if (user == null)
			return;
		boolean vanish = user.isVanished();
		new Tasker() {
			@Override
			public void run() {
				if (!vanish)
					MessageUtils.msgConfig(event.getPlayer(), "broadcast", config, Placeholders.c().addPlayer("player", event.getPlayer()), BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
				MessageUtils.msgConfig(event.getPlayer(), "messages", config, Placeholders.c().addPlayer("player", event.getPlayer()), event.getPlayer());
			}
		}.runTask();
		if (Fly.isEnabled())
			Fly.antiFall.remove(event.getPlayer().getUniqueId());
		user.notifyQuit();
	}
}
