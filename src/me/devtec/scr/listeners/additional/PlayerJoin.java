package me.devtec.scr.listeners.additional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PlayerJoin implements Listener {
	Config joinconfig;

	public PlayerJoin(Config config) {
		this.joinconfig = config;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		
		if(Loader.data.getBoolean("maintenance")) {
			
		}
		
		if(joinconfig.getBoolean("enabled")) {
			if (!event.getPlayer().hasPlayedBefore()) {
				for (String command : joinconfig.getStringList("firstTime.commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
				new Tasker() {
					@Override
					public void run() {
						MessageUtils.msgConfig(event.getPlayer(), "firstTime.broadcast", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), BukkitLoader.getOnlinePlayers().toArray(new CommandSender[0]) );
						MessageUtils.msgConfig(event.getPlayer(), "firstTime.messages", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), event.getPlayer());
						}
				}.runTask();
			} else {
				for (String command : joinconfig.getStringList("commands"))
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getPlayer().getName()));
				new Tasker() {
					@Override
					public void run() {
						MessageUtils.msgConfig(event.getPlayer(), "broadcast", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), BukkitLoader.getOnlinePlayers().toArray(new Player[0]) );
						MessageUtils.msgConfig(event.getPlayer(), "messages", joinconfig, Placeholders.c().addPlayer("player", event.getPlayer()), event.getPlayer());
					}
				}.runTask();
			}
		}
		API.getUser(event.getPlayer()).joinTime();
	}
}
