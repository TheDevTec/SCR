package me.devtec.scr.listeners.additional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.devtec.scr.MessageUtils;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PlayerJoin implements Listener {
	Config config;
	public PlayerJoin(Config config) {
		this.config=config;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if(!event.getPlayer().hasPlayedBefore()) {
			for(String command : config.getStringList("firstTime.commands"))Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{0}", event.getPlayer().getName()));
			new Tasker() {
				public void run() {
					MessageUtils.msgConfig(event.getPlayer(), config, "firstTime.broadcast", new Object[] {event.getPlayer().getName()}, BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
					MessageUtils.msgConfig(event.getPlayer(), config, "firstTime.messages", new Object[] {event.getPlayer().getName()}, new Player[] {event.getPlayer()});
				}
			}.runTask();
		}else {
			for(String command : config.getStringList("commands"))Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{0}", event.getPlayer().getName()));
			new Tasker() {
				public void run() {
					MessageUtils.msgConfig(event.getPlayer(), config, "broadcast", new Object[] {event.getPlayer().getName()}, BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
					MessageUtils.msgConfig(event.getPlayer(), config, "messages", new Object[] {event.getPlayer().getName()}, new Player[] {event.getPlayer()});
				}
			}.runTask();
		}
	}
}
