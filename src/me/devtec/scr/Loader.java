package me.devtec.scr;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import me.devtec.scr.api.API;
import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.api.User;
import me.devtec.scr.commands.CustomCommands;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.info.AFK;
import me.devtec.scr.functions.AutoAnnouncements;
import me.devtec.scr.functions.ScoreboardManager;
import me.devtec.scr.functions.SmartNightSkipping;
import me.devtec.scr.listeners.ServerList;
import me.devtec.scr.listeners.additional.ChatListeners;
import me.devtec.scr.listeners.additional.PlayerJoin;
import me.devtec.scr.listeners.additional.PlayerQuit;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.events.EventManager;
import me.devtec.shared.placeholders.PlaceholderExpansion;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.BukkitLoader;
import me.devtec.theapi.bukkit.events.ServerListPingEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin {

	public static Loader plugin;

	public static Config config;
	public static Config commands;
	public static Config translations, engtrans;
	public static Config data;

	public static Config chat;

	// VAULT plugin
	public static Object economy;
	public static Object vault;
	public static Object luckperms;

	public List<ScrCommand> registered_commands = new ArrayList<>();

	public static Config economyConfig;
	public static Config joinListenerConfig;
	public static Config quitListenerConfig;
	public static Config tablistConfig;
	public static Config scoreboardConfig;
	public static Config placeholders;

	public ScoreboardManager scoreboard;

	@Override
	public void onLoad() {
		plugin = this;
	}

	private PlaceholderExpansion papi_theapi;

	@Override
	public void onEnable() {
		Configs.loadConfigs(); // Loading all configs

		if (Bukkit.getPluginManager().getPlugin("Vault") != null && Ref.getClass("net.milkbowl.vault.economy.Economy") != null) {
			vaultPermissionHooking();
			if (economyConfig.getBoolean("useVaultEconomy")) {
				vaultEconomyHooking();
				economyConfig.clear();
			} else {
				getLogger().info("[Economy] Registering ScrEconomy and using as Vault economy.");
				economy = new ScrEconomy(economyConfig);
				Bukkit.getServicesManager().register(Economy.class, (ScrEconomy) economy, this, ServicePriority.Normal);
			}
		}
		// Did you reload plugin?!!?!?
		for (Player player : BukkitLoader.getOnlinePlayers())
			API.getUser(player).notifyJoin(player, false);

		if (Ref.getClass("org.spigotmc.SpigotConfig") != null) {
			if (SpigotConfig.bungee)
				Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
			Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "scr:vanish");
		}

		if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null)
			luckperms = LuckPermsProvider.get();

		if (Loader.config.getBoolean("smartNightSkipping.enabled"))
			SmartNightSkipping.load(Loader.config.getString("smartNightSkipping.mode").equalsIgnoreCase("SKIP"), Loader.config.getInt("smartNightSkipping.minimumPlayers"),
					Loader.config.getInt("smartNightSkipping.speedingUp.everySleepingPlayer"));

		// Loading TAB & Scoreboard
		loadTab();
		loadScoreboard();

		loadListeners(); // Loading Events
		loadCommands(); // Loading commands & CustomCommands

		// LOAD PLACEHOLDERS
		loadPlaceholders();

		new Tasker() {

			Set<UUID> removeCached = new HashSet<>();

			@Override
			public void run() {
				for (User cached : API.getCachedUsers())
					if (!cached.isOnline())
						removeCached.add(cached.getUUID());
				for (UUID uuid : removeCached)
					API.removeUser(uuid);
				removeCached.clear();
			}
		}.runRepeating(300, 300);
	}

	@Override
	public void onDisable() {
		for (ScrCommand cmd : registered_commands)
			cmd.disabling();
		registered_commands.clear();
		// TODO tab
		// if (tablist != null)
		// tablist.unloadTasks();
		if (scoreboard != null)
			scoreboard.unloadTasks();
		AFK.stopTask();
		AutoAnnouncements.unloadTask();
		SmartNightSkipping.unload();

		// Placeholders unload
		papi_theapi.unregister();
	}

	public void loadScoreboard() {
		// Unloading tasks
		if (scoreboard != null)
			scoreboard.unloadTasks();
		// Loading tasks
		if (scoreboardConfig.getBoolean("enabled")) {
			scoreboard = new ScoreboardManager();
			scoreboard.loadTasks(scoreboardConfig);
		}
	}

	public void loadTab() {
		// TODO tab
		// Unloading tasks
		// if (tablist != null)
		// tablist.unloadTasks();
		// Loading tasks
		// if (tablistConfig.getBoolean("enabled")) {
		// tablist = new Tablist();
		// tablist.loadTasks(tablistConfig);
		// }
	}

	public static String getVaultGroup(Player player) {
		if (Loader.luckperms != null)
			return ((LuckPerms) Loader.luckperms).getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrimaryGroup();
		if (Loader.vault != null)
			if (((Permission) Loader.vault).hasGroupSupport())
				return ((Permission) Loader.vault).getPrimaryGroup(player);
		return "default";
	}

	private void loadListeners() {
		// Join listener (messages, maintenance)
		getLogger().info("[Listener] Registering PlayerJoin listener.");
		registerListener(new PlayerJoin(joinListenerConfig));

		if (quitListenerConfig.getBoolean("enabled")) {
			getLogger().info("[Listener] Registering PlayerQuit listener.");
			registerListener(new PlayerQuit(quitListenerConfig));
		} else {
			quitListenerConfig.clear();
			quitListenerConfig = null;
		}
		if (config.getBoolean("serverlist.enabled")) { // MOTD & list of players
			EventManager.register(new ServerList()).listen(ServerListPingEvent.class);
			getLogger().info("[Listener] Registering ServerList listener.");
		}
		registerListener(new ChatListeners());

	}

	private void loadCommands() {
		int count = 0;
		int total = 0;

		getLogger().info("Loading commands..");
		try {
			File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			JarFile jar = new JarFile(file);
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.getName().endsWith(".class") || !entry.getName().startsWith("me/devtec/scr/commands/") || entry.getName().equals("me/devtec/scr/commands/ScrCommand.class")
						|| entry.getName().equals("me/devtec/scr/commands/CustomCommands.class") || entry.getName().equals("me/devtec/scr/commands/CCommand.class"))
					continue;
				Class<?> cls = Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."));
				if (ScrCommand.class.isAssignableFrom(cls)) {
					ScrCommand scrCmd = (ScrCommand) cls.newInstance();
					++total;
					if (commands.exists(scrCmd.configSection() + ".enabled") && commands.getBoolean(scrCmd.configSection() + ".enabled", true)) {
						++count;
						scrCmd.initFirst(commands.getStringList(scrCmd.configSection() + ".cmds"));
						registered_commands.add(scrCmd);
					} else if (!commands.exists(scrCmd.configSection() + ".enabled"))
						getLogger().log(Level.SEVERE, "An issue occurred while loading commands (missing configuration section for command '" + scrCmd.configSection()
								+ "', please report the error to discord https://discord.gg/5kCSrtkKGF");
				}
			}
			jar.close();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "An issue occurred while loading commands, please report the error to discord https://discord.gg/5kCSrtkKGF", e);
			return;
		}
		getLogger().info("Commands successfully loaded. (" + count + "/" + total + ")");
		CustomCommands.load();
		Loader.plugin.getLogger().info("Loading custom commands!");
	}

	public static void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}

	// VAULT HOOKING
	private void vaultEconomyHooking() {
		getLogger().info("[Economy] Looking for Vault economy service..");
		new Tasker() {
			@Override
			public void run() {
				if (getVaultEconomy()) {
					getLogger().info("[Economy] Found Vault economy service. " + ((Economy) economy).getName());
					cancel();
				}
			}
		}.runTimer(0, 20, 15);
	}

	private boolean getVaultEconomy() {
		try {
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null)
				economy = economyProvider.getProvider();
			return economy != null;
		} catch (Exception e) {
			return false;
		}
	}

	private void vaultPermissionHooking() {
		getLogger().info("[Permission] Looking for Vault permission service..");
		new Tasker() {
			@Override
			public void run() {
				if (getVaultPermission()) {
					getLogger().info("[Permission] Found Vault permission service. " + ((Permission) vault).getName());
					cancel();
				}
			}
		}.runTimer(0, 20, 15);
	}

	private boolean getVaultPermission() {
		try {
			RegisteredServiceProvider<Permission> economyProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
			if (economyProvider != null)
				vault = economyProvider.getProvider();
			return vault != null;
		} catch (Exception e) {
			return false;
		}
	}

	private void loadPlaceholders() {
		// %theapi_scr_...%
		papi_theapi = new PlaceholderExpansion("scr") {
			@Override
			public String apply(String params, UUID uuid) {
				params = '%' + params + '%';
				params = params.replace("%scr_", "%"); // Just in case :D
				String f;
				if (Bukkit.getPlayer(uuid) != null)
					f = PlaceholderAPISupport.replace(params, Bukkit.getPlayer(uuid), false, null);
				else
					f = PlaceholderAPISupport.replace(params, null, false, null);
				return params.equals(f) ? null : f;
			}

		};
		papi_theapi.register();
	}
}
