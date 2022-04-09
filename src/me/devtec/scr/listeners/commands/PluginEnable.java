package me.devtec.scr.listeners.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;

public class PluginEnable implements Listener {
	
	public static Map<ScrCommand, List<String>[]> waiting = new ConcurrentHashMap<>();
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		List<ScrCommand> remove = new ArrayList<>();
		for(Entry<ScrCommand, List<String>[]> entry : waiting.entrySet()) {
			if(entry.getValue()[0].contains(event.getPlugin().getName())) {
				entry.getValue()[0].remove(event.getPlugin().getName());
			}
			if(entry.getValue()[0].isEmpty()) {
				remove.add(entry.getKey());
			}
		}
		for(ScrCommand cmd : remove) {
			new Tasker() {
				public void run() {
					BukkitLoader.getNmsProvider().postToMainThread(() -> cmd.init(waiting.remove(cmd)[1]));
				}
			}.runTask();
		}
	}
}
