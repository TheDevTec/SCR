package me.devtec.scr.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import me.devtec.scr.Loader;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;

public class SmartNightSkipping implements Listener {
	static boolean skipNight;
	static int minimumPlayers, skipTicksPerTick;

	static int threadId;
	static SmartNightSkipping instance;

	public static void load(boolean skipNight, int minimumPlayers, int skipTicksPerTick) {
		if (threadId != 0)
			return;
		SmartNightSkipping.skipNight = skipNight;
		SmartNightSkipping.minimumPlayers = minimumPlayers;
		SmartNightSkipping.skipTicksPerTick = skipTicksPerTick;
		threadId = new Tasker() {
			@Override
			public void run() {
				for (World world : Bukkit.getWorlds())
					if (shouldSkipNight(world.getUID()))
						BukkitLoader.getNmsProvider().postToMainThread(() -> {
							if (skipNight) {
								world.setThundering(false);
								world.setTime(0);
							} else
								world.setTime(world.getTime() + skipTicksPerTick * sleepingPlayers.getOrDefault(world.getUID(), 1));
						});
			}
		}.runRepeating(50, 5);
		Bukkit.getPluginManager().registerEvents(instance = new SmartNightSkipping(), Loader.plugin);
	}

	public static boolean shouldSkipNight(UUID worldId) {
		int inTheBed = sleepingPlayers.getOrDefault(worldId, 0);
		return inTheBed != 0 && (minimumPlayers <= inTheBed || inTheBed > BukkitLoader.getOnlinePlayers().size());
	}

	public static void unload() {
		if (threadId == 0)
			return;
		Scheduler.cancelTask(threadId);
		threadId = 0;
		HandlerList.unregisterAll(instance);
	}

	public static Map<UUID, Integer> sleepingPlayers = new HashMap<>();

	@EventHandler
	public void onEnterBed(PlayerBedEnterEvent e) {
		if (e.getBedEnterResult() == BedEnterResult.OK)
			sleepingPlayers.put(e.getPlayer().getWorld().getUID(), sleepingPlayers.getOrDefault(e.getPlayer().getWorld().getUID(), 0) + 1);
	}

	@EventHandler
	public void onLeaveBed(PlayerBedLeaveEvent e) {
		sleepingPlayers.put(e.getPlayer().getWorld().getUID(), sleepingPlayers.getOrDefault(e.getPlayer().getWorld().getUID(), 1) - 1);
	}
}
