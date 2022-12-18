package me.devtec.scr.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.listeners.additional.SbJoinQuit;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.scoreboard.ScoreboardAPI;
import me.devtec.theapi.bukkit.scoreboard.SimpleScore;
import net.milkbowl.vault.permission.Permission;

public class ScoreboardManager {
	private int task;
	private boolean sync;

	public Map<String, SbSettings> perPlayer = new HashMap<>();
	public Map<String, SbSettings> perWorld = new HashMap<>();
	public Map<String, SbSettings> perGroup = new HashMap<>();
	public SbSettings global = new SbSettings();

	public Map<UUID, SimpleScore> players = new HashMap<>();
	public List<UUID> hidden = new ArrayList<>();

	public void loadTasks(Config config) {
		Loader.plugin.getLogger().info("[Scoreboard] Registering Join & Quit listener.");

		sync = config.getBoolean("settings.syncPlaceholders");

		// GLOBAL
		global.title = config.getString("title");
		global.lines = config.getStringList("lines");

		// OTHER SETTINGS
		for (String world : config.getKeys("perWorld")) {
			SbSettings global = new SbSettings();
			global.copySettings(this.global);
			global.title = config.getString("perWorld." + world + ".title");
			global.lines = config.getStringList("perWorld." + world + ".lines");
			perWorld.put(world, global);
		}

		if (Loader.vault != null)
			for (String world : config.getKeys("perGroup")) {
				SbSettings global = new SbSettings();
				global.copySettings(this.global);
				global.title = config.getString("perGroup." + world + ".title");
				global.lines = config.getStringList("perGroup." + world + ".lines");
				perGroup.put(world, global);
			}

		for (String world : config.getKeys("perPlayer")) {
			SbSettings global = new SbSettings();
			global.copySettings(this.global);
			global.title = config.getString("perPlayer." + world + ".title");
			global.lines = config.getStringList("perPlayer." + world + ".lines");
			perPlayer.put(world, global);
		}

		Loader.registerListener(new SbJoinQuit(this));

		task = new Tasker() {
			@Override
			public void run() {
				for (Player player : BukkitLoader.getOnlinePlayers()) { // Use this method for 1.7.10 support
					if (!(player.isOnline() && !player.isDead()) || hidden.contains(player.getUniqueId()))
						continue;
					SbSettings settings = getSettingsOf(player); // Find player's settings
					if (sync)
						BukkitLoader.getNmsProvider().postToMainThread(() -> {
							settings.replace(player); // Replace placeholders sync with server

							new Thread(() -> { // Continue async
								settings.colorize();
								SimpleScore score = players.get(player.getUniqueId());
								if (score == null)
									players.put(player.getUniqueId(), score = new SimpleScore());
								apply(player, settings, score);
							}).start();
						});
					else {
						settings.replace(player);
						settings.colorize();
						SimpleScore score = players.get(player.getUniqueId());
						if (score == null)
							players.put(player.getUniqueId(), score = new SimpleScore());
						apply(player, settings, score);
					}
				}
			}
		}.runRepeating(20, config.getLong("settings.reflesh")); // Async task
	}

	public void unloadTasks() {
		Scheduler.cancelTask(task);
		for (Player p : BukkitLoader.getOnlinePlayers())
			fullyUnregister(p);
	}

	public void fullyUnregister(Player p) {
		players.remove(p.getUniqueId());
		ScoreboardAPI sb = SimpleScore.scores.remove(p.getUniqueId());
		if (sb != null)
			sb.destroy();
	}

	public void apply(Player target, SbSettings settings, SimpleScore score) {
		score.setTitle(settings.title);
		score.addLines(settings.lines);
		score.send(target);
	}

	public SbSettings getSettingsOf(Player player) {
		SbSettings generated = new SbSettings();
		// Copy settings from global
		generated.copySettings(global);

		SbSettings settings = perPlayer.get(player.getName());
		if (settings != null)
			generated.copySettings(settings);
		settings = perWorld.get(player.getWorld().getName());
		if (settings != null)
			generated.copySettings(settings);
		settings = perGroup.get(getVaultGroup(player));
		if (settings != null)
			generated.copySettings(settings);
		return generated;
	}

	private String getVaultGroup(Player player) {
		if (Loader.vault != null)
			if (((Permission) Loader.vault).hasGroupSupport())
				return ((Permission) Loader.vault).getPrimaryGroup(player);
		return null;
	}

	public static class SbSettings {
		public String title;
		public List<String> lines;

		public void copySettings(SbSettings global) {
			if (title == null && global.title != null)
				title = global.title;
			if ((lines == null || lines.isEmpty()) && global.lines != null && !global.lines.isEmpty())
				lines = global.lines;
		}

		public void replace(Player player) {
			title = PlaceholderAPISupport.replace(title, player);
			List<String> new_lines = new ArrayList<>();
			for (String text : lines)
				new_lines.add(PlaceholderAPISupport.replace(text, player));
			lines = new_lines;
		}

		public void replaceOLD(Player player) {
			Placeholders plac = Placeholders.c().addPlayer("player", player);
			title = PlaceholderAPI.apply(MessageUtils.placeholder(player, title, plac), player.getUniqueId());
			lines = PlaceholderAPI.apply(MessageUtils.placeholder(player, lines, plac), player.getUniqueId());
		}

		public void colorize() {
			title = StringUtils.colorize(title);
			lines = StringUtils.colorize(lines);
		}
	}
}
