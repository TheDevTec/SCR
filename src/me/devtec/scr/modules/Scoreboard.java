package me.devtec.scr.modules;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;

public class Scoreboard {
	public static boolean isLoaded;
	private static int task;
	public static List<Player> applied = new ArrayList<>();
	
	public static void load(List<String> disabledWorlds, long time) {
		if(isLoaded)return;
		isLoaded=true;
		task = new Tasker() {
			public void run() {
				for(Player player : TheAPI.getOnlinePlayers()) {
					if(disabledWorlds.contains(player.getWorld().getName())) {
						if(applied.contains(player)) {
							applied.remove(player);
							disable(player);
						}
						continue;
					}
					if(!applied.contains(player))
						applied.add(player);
					//TODO tab
				}
			}
		}.runRepeating(10, time);
	}
	
	public static void disable(Player player) {
		//TODO disable tab
	}

	public static void unload() {
		if(!isLoaded)return;
		isLoaded=false;
		Scheduler.cancelTask(task);
	}
}
