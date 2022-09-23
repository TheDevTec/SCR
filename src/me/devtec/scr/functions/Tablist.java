package me.devtec.scr.functions;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.listeners.additional.TablistJoinQuit;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.placeholders.PlaceholderAPI;
import me.devtec.shared.scheduler.Scheduler;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.nms.NmsProvider.Action;
import me.devtec.theapi.bukkit.nms.NmsProvider.DisplayType;
import me.devtec.theapi.bukkit.tablist.NameTagAPI;
import me.devtec.theapi.bukkit.tablist.TabAPI;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.permission.Permission;

public class Tablist {
	private int task;
	private boolean sync;

	public Map<String, TabSettings> perPlayer = new HashMap<>();
	public Map<String, TabSettings> perWorld = new HashMap<>();
	public Map<String, TabSettings> perGroup = new HashMap<>();
	public TabSettings global = new TabSettings();

	public Map<UUID, TabSettings> players = new HashMap<>();
	public Map<UUID, NameTagAPI> tags = new HashMap<>();
	public Map<UUID, DisplayType> dType = new HashMap<>();

	public void loadTasks(Config config) {
		Loader.plugin.getLogger().info("[Tablist] Registering Join & Quit listener.");

		sync = config.getBoolean("settings.syncPlaceholders");

		// GLOBAL
		global.header = StringUtils.join(config.getStringList("header"), "\n");
		global.footer = StringUtils.join(config.getStringList("footer"), "\n");
		global.nametag_prefix = "";
		global.nametag_suffix = "";
		global.sorting = "0099{0}"; // Vanilla
		global.tabname = "%player%";
		global.yellowNumber = config.getString("yellowNumber.value");
		global.yellowNumberDisplay = config.getString("yellowNumber.display");

		// OTHER SETTINGS
		for (String world : config.getKeys("perWorld")) {
			TabSettings global = new TabSettings();
			global.header = StringUtils.join(config.getStringList("perWorld." + world + ".header"), "\n");
			global.footer = StringUtils.join(config.getStringList("perWorld." + world + ".footer"), "\n");
			global.nametag_prefix = config.getString("perWorld." + world + ".nametag.prefix");
			global.nametag_suffix = config.getString("perWorld." + world + ".nametag.suffix");
			global.tabname = config.getString("perWorld." + world + ".tabname");
			global.yellowNumber = config.getString("perWorld." + world + ".yellowNumber.value");
			global.yellowNumberDisplay = config.getString("perWorld." + world + ".yellowNumber.display");
			perWorld.put(world, global);
		}

		if (config.getString("sortingBy").equalsIgnoreCase("group") && (Loader.luckperms != null || Loader.vault != null)) {
			for (String group : config.getKeys("perGroup")) {
				TabSettings gglobal = new TabSettings();
				gglobal.header = StringUtils.join(config.getStringList("perGroup." + group + ".header"), "\n");
				gglobal.footer = StringUtils.join(config.getStringList("perGroup." + group + ".footer"), "\n");
				gglobal.nametag_prefix = config.getString("perGroup." + group + ".nametag.prefix");
				gglobal.nametag_suffix = config.getString("perGroup." + group + ".nametag.suffix");
				gglobal.tabname = config.getString("perGroup." + group + ".tabname");
				gglobal.yellowNumber = config.getString("perGroup." + group + ".yellowNumber.value");
				gglobal.yellowNumberDisplay = config.getString("perGroup." + group + ".yellowNumber.display");
				perGroup.put(group, gglobal);
			}
			List<String> groups = config.getStringList("sorting");
			int length = ("" + groups.size()).length() + 1;
			for (int i = 0; i < groups.size(); ++i) {
				StringBuilder s = new StringBuilder();
				int limit = length - (i + "").length();
				for (int d = 0; d < limit; ++d)
					s.append("0");
				s.append(i);
				TabSettings tab = perGroup.get(groups.get(i));
				if (tab == null)
					continue;
				tab.sorting = s.append("{0}").toString();
			}
		}

		if (config.getString("sortingBy").equalsIgnoreCase("weight") && Loader.luckperms != null) {
			for (String group : config.getKeys("perGroup")) {
				TabSettings gglobal = new TabSettings();
				gglobal.header = StringUtils.join(config.getStringList("perGroup." + group + ".header"), "\n");
				gglobal.footer = StringUtils.join(config.getStringList("perGroup." + group + ".footer"), "\n");
				gglobal.nametag_prefix = config.getString("perGroup." + group + ".nametag.prefix");
				gglobal.nametag_suffix = config.getString("perGroup." + group + ".nametag.suffix");
				gglobal.tabname = config.getString("perGroup." + group + ".tabname");
				gglobal.yellowNumber = config.getString("perGroup." + group + ".yellowNumber.value");
				gglobal.yellowNumberDisplay = config.getString("perGroup." + group + ".yellowNumber.display");
				perGroup.put(group, gglobal);
			}
			List<String> groups = config.getStringList("sorting");
			for (String group : groups) {
				TabSettings tab = perGroup.get(group);
				if (tab == null)
					continue;
				tab.sorting = "{weight}";
			}
		}

		for (String world : config.getKeys("perPlayer")) {
			TabSettings global = new TabSettings();
			global.header = StringUtils.join(config.getStringList("perPlayer." + world + ".header"), "\n");
			global.footer = StringUtils.join(config.getStringList("perPlayer." + world + ".footer"), "\n");
			global.nametag_prefix = config.getString("perPlayer." + world + ".nametag.prefix");
			global.nametag_suffix = config.getString("perPlayer." + world + ".nametag.suffix");
			global.tabname = config.getString("perPlayer." + world + ".tabname");
			global.yellowNumber = config.getString("perPlayer." + world + ".yellowNumber.value");
			global.yellowNumberDisplay = config.getString("perPlayer." + world + ".yellowNumber.display");
			perPlayer.put(world, global);
		}

		Loader.registerListener(new TablistJoinQuit(this));

		task = new Tasker() {
			@Override
			public void run() {
				for (Player player : BukkitLoader.getOnlinePlayers()) { // Use this method for 1.7.10 support
					TabSettings settings = getSettingsOf(player); // Find player's settings
					if (sync)
						BukkitLoader.getNmsProvider().postToMainThread(() -> {
							settings.replace(player); // Replace placeholders sync with server

							new Thread(() -> { // Continue async
								settings.colorize();
								TabSettings prev = players.put(player.getUniqueId(), settings);
								apply(player, BukkitLoader.getOnlinePlayers(), settings, prev);
							}).start();
						});
					else {
						settings.replace(player);
						settings.colorize();
						TabSettings prev = players.put(player.getUniqueId(), settings);
						apply(player, BukkitLoader.getOnlinePlayers(), settings, prev);
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
		NameTagAPI nametag = tags.remove(p.getUniqueId());
		if (nametag != null)
			nametag.reset(BukkitLoader.getOnlinePlayers().toArray(new Player[0]));
		for (NameTagAPI a : tags.values())
			a.reset(p);
		dType.remove(p.getUniqueId());
		players.remove(p.getUniqueId());
		BukkitLoader.getPacketHandler().send(BukkitLoader.getOnlinePlayers(), BukkitLoader.getNmsProvider().packetScoreboardScore(Action.REMOVE, "ping", p.getName(), 0));
		TabAPI.setHeaderFooter(p, "", "");
		TabAPI.setTabListName(p, p.getName());
	}

	public void apply(Player target, Collection<? extends Player> collection, TabSettings settings, TabSettings prev) {
		if (prev == null || !(settings.header.equals(prev.header) && settings.footer.equals(prev.footer)))
			TabAPI.setHeaderFooter(target, settings.header, settings.footer);
		TabAPI.setTabListName(target, settings.tabname);
		NameTagAPI tag = tags.get(target.getUniqueId());
		if (tag == null)
			tags.put(target.getUniqueId(), tag = new NameTagAPI(target, settings.sorting));
		tag.set(getColor(settings.nametag_prefix), settings.nametag_prefix, settings.nametag_suffix);
		tag.setName(settings.sorting);
		tag.send(collection.toArray(new Player[0]));

		// yellow number
		int number = StringUtils.getInt(settings.yellowNumber);
		DisplayType displayType = showType(settings.yellowNumberDisplay);
		DisplayType previous = dType.get(target.getUniqueId());
		dType.put(target.getUniqueId(), displayType);
		if (previous != null && previous != displayType) {
			dType.put(target.getUniqueId(), displayType);
			BukkitLoader.getPacketHandler().send(collection, createObjectivePacket(2, "ping", target.getName(), previous));
			BukkitLoader.getPacketHandler().send(collection, createObjectivePacket(0, "ping", target.getName(), displayType));
		}
		BukkitLoader.getPacketHandler().send(collection, BukkitLoader.getNmsProvider().packetScoreboardScore(Action.CHANGE, "ping", target.getName(), number));
	}

	public static DisplayType showType(String showType) {
		return showType != null && (showType.equalsIgnoreCase("heart") || showType.equalsIgnoreCase("hearts") || showType.equalsIgnoreCase("hp")) ? DisplayType.HEARTS : DisplayType.INTEGER;
	}

	public TabSettings getSettingsOf(Player player) {
		TabSettings generated = new TabSettings();

		// first perplayer -> perWorld -> perGroup -> Global (default)
		TabSettings settings = perPlayer.get(player.getName());
		if (settings != null)
			generated.copySettings(settings);
		settings = perWorld.get(player.getWorld().getName());
		if (settings != null)
			generated.copySettings(settings);
		settings = perGroup.getOrDefault(getVaultGroup(player), perGroup.get("default"));
		if (settings != null)
			generated.copySettings(settings);
		// Copy settings from global
		generated.copySettings(global);

		return generated;
	}

	public static String getVaultGroup(Player player) {
		if (Loader.luckperms != null)
			return ((LuckPerms) Loader.luckperms).getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrimaryGroup();
		if (Loader.vault != null)
			if (((Permission) Loader.vault).hasGroupSupport())
				return ((Permission) Loader.vault).getPrimaryGroup(player);
		return "default";
	}

	private static ChatColor getColor(String lastColors) {
		if (lastColors == null || lastColors.isEmpty())
			return null;
		lastColors = StringUtils.getLastColors(lastColors);
		if (lastColors.isEmpty())
			return null;
		return ChatColor.getByChar(lastColors.charAt(0));
	}

	public static Object createObjectivePacket(int mode, String name, String displayName, DisplayType type) {
		Object packet = BukkitLoader.getNmsProvider().packetScoreboardObjective();
		if (Ref.isNewerThan(16)) {
			Ref.set(packet, "d", name);
			Ref.set(packet, "e", BukkitLoader.getNmsProvider().chatBase("{\"text\":\"" + displayName + "\"}"));
			Ref.set(packet, "f", BukkitLoader.getNmsProvider().getEnumScoreboardHealthDisplay(type));
			Ref.set(packet, "g", mode);
		} else {
			Ref.set(packet, "a", name);
			Ref.set(packet, "b", BukkitLoader.getNmsProvider().chatBase("{\"text\":\"" + displayName + "\"}"));
			if (Ref.isNewerThan(7)) {
				Ref.set(packet, "c", BukkitLoader.getNmsProvider().getEnumScoreboardHealthDisplay(type));
				Ref.set(packet, "d", mode);
			} else
				Ref.set(packet, "c", mode);
		}
		return packet;
	}

	public static class TabSettings {
		public String header, footer, sorting, yellowNumber, yellowNumberDisplay, nametag_prefix, nametag_suffix, tabname;

		public void copySettings(TabSettings global) {
			if (yellowNumberDisplay == null && global.yellowNumberDisplay != null && !global.yellowNumberDisplay.isEmpty())
				yellowNumberDisplay = global.yellowNumberDisplay;
			if (tabname == null && global.tabname != null && !global.tabname.isEmpty())
				tabname = global.tabname;
			if (header == null && global.header != null && !global.header.isEmpty())
				header = global.header;
			if (footer == null && global.footer != null && !global.footer.isEmpty())
				footer = global.footer;
			if (nametag_prefix == null && global.nametag_prefix != null)
				nametag_prefix = global.nametag_prefix;
			if (nametag_suffix == null && global.nametag_suffix != null)
				nametag_suffix = global.nametag_suffix;
			if (sorting == null && global.sorting != null)
				sorting = global.sorting;
			if (yellowNumber == null && global.yellowNumber != null && !global.yellowNumber.isEmpty())
				yellowNumber = global.yellowNumber;
		}

		public void replace(Player player) {
			if (yellowNumberDisplay != null)
				yellowNumberDisplay = PlaceholderAPI.apply(yellowNumberDisplay, player.getUniqueId());

			tabname = PlaceholderAPI.apply(tabname, player.getUniqueId());

			header = PlaceholderAPI.apply(header, player.getUniqueId());
			footer = PlaceholderAPI.apply(footer, player.getUniqueId());
			nametag_prefix = PlaceholderAPI.apply(nametag_prefix, player.getUniqueId());
			nametag_suffix = PlaceholderAPI.apply(nametag_suffix, player.getUniqueId());
			if (sorting != null)
				sorting = PlaceholderAPI.apply(sorting, player.getUniqueId()).replace("{0}", player.getName()).replace("{weight}",
						Loader.luckperms != null ? "" + ((LuckPerms) Loader.luckperms).getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getWeight()
								: "0001" + player.getName());
			if (yellowNumber != null)
				yellowNumber = PlaceholderAPI.apply(yellowNumber, player.getUniqueId());
		}

		public void colorize() {
			tabname = StringUtils.colorize(tabname);
			header = StringUtils.colorize(header);
			footer = StringUtils.colorize(footer);
			nametag_prefix = StringUtils.colorize(nametag_prefix);
			nametag_suffix = StringUtils.colorize(nametag_suffix);
		}
	}
}
