package me.devtec.scr;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.CustomCommands;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.functions.ScoreboardManager;
import me.devtec.scr.functions.Tablist;
import me.devtec.scr.listeners.ServerList;
import me.devtec.scr.listeners.additional.PlayerJoin;
import me.devtec.scr.listeners.additional.PlayerQuit;
import me.devtec.scr.utils.PlaceholderAPISupport;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.events.EventManager;
import me.devtec.shared.placeholders.PlaceholderExpansion;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.theapi.bukkit.events.ServerListPingEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin {

	public static Loader plugin;

	public static Config config;
	public static Config commands;
	public static Config translations;
	public static Config data;

	public static Config chat;

	// VAULT plugin
	public static Object economy;
	public static Object vault;

	public List<ScrCommand> registered_commands = new ArrayList<>();

	public static Config economyConfig;
	public static Config joinListenerConfig;
	public static Config quitListenerConfig;
	public static Config tablistConfig;
	public static Config scoreboardConfig;
	public static Tablist tablist;
	public ScoreboardManager scoreboard;

	@Override
	public void onLoad() {
		plugin = this;
		Configs.loadConfigs();
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
	}

	private Object papi_theapi;
	private me.clip.placeholderapi.expansion.PlaceholderExpansion papi;

	@Override
	public void onEnable() {
		loadListeners();
		loadCommands();
		if (tablistConfig.getBoolean("enabled")) {
			tablist = new Tablist();
			tablist.loadTasks(tablistConfig);
		}
		if (scoreboardConfig.getBoolean("enabled")) {
			scoreboard = new ScoreboardManager();
			scoreboard.loadTasks(scoreboardConfig);
		}

		// LOAD PLACEHOLDERS
		loadPlaceholders();
	}

	@Override
	public void onDisable() {
		for (ScrCommand cmd : registered_commands)
			cmd.disabling();
		registered_commands.clear();
		if (tablist != null)
			tablist.unloadTasks();
		if (scoreboard != null)
			scoreboard.unloadTasks();
		//Placeholders
		((PlaceholderExpansion) papi_theapi).unregister();
		papi.unregister();
	}

	private void loadListeners() {
		if (joinListenerConfig.getBoolean("enabled")) {
			getLogger().info("[Listener] Registering PlayerJoin listener.");
			registerListener(new PlayerJoin(joinListenerConfig));
		} else {
			joinListenerConfig.clear();
			joinListenerConfig = null;
		}
		if (quitListenerConfig.getBoolean("enabled")) {
			getLogger().info("[Listener] Registering PlayerQuit listener.");
			registerListener(new PlayerQuit(quitListenerConfig));
		} else {
			quitListenerConfig.clear();
			quitListenerConfig = null;
		}
		if(config.getBoolean("serverlist.enabled")) { //MOTD & list of players
			EventManager.register(new ServerList()).listen(ServerListPingEvent.class);
			getLogger().info("[Listener] Registering ServerList listener.");
			//registerListener(new ServerList());
		}
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
						|| entry.getName().equals("me/devtec/scr/commands/CustomCommands.class")|| entry.getName().equals("me/devtec/scr/commands/CCommand.class"))
					continue;
				Class<?> cls = Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."));
				if (ScrCommand.class.isAssignableFrom(cls)) {
					ScrCommand scrCmd = (ScrCommand) cls.newInstance();
					++total;
					if (commands.exists(scrCmd.configSection() + ".enabled") && commands.getBoolean(scrCmd.configSection() + ".enabled", true)) {
						++count;
						scrCmd.initFirst(commands.getStringList(scrCmd.configSection() + ".cmds"));
						registered_commands.add(scrCmd);
					}
				}
			}
			jar.close();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "An issue occurred while loading commands, please report the error to discord https://discord.gg/5kCSrtkKGF", e);
			return;
		}
		getLogger().info("Commands successfully loaded. (" + count + "/" + total + ")");
		getLogger().info("Loading custom commands!");
		CustomCommands.load();
	}

	/*
	 * private void loadConfigs() { //MOVED TO Configs class config =
	 * loadAndMerge("config.yml", "config.yml"); commands =
	 * loadAndMerge("commands.yml", "commands.yml"); economyConfig =
	 * loadAndMerge("economy.yml", "economy.yml"); joinListenerConfig =
	 * loadAndMerge("events/join-listener.yml", "events/join-listener.yml");
	 * quitListenerConfig = loadAndMerge("events/quit-listener.yml",
	 * "events/quit-listener.yml"); tablistConfig = loadAndMerge("tablist.yml",
	 * "tablist.yml"); scoreboardConfig = loadAndMerge("scoreboard.yml",
	 * "scoreboard.yml"); data = loadAndMerge("data.yml", "data.yml");
	 * 
	 * loadAndMerge("translations/Translation-en.yml",
	 * "translations/Translation-en.yml");
	 * loadAndMerge("translations/Translation-cz.yml",
	 * "translations/Translation-cz.yml"); String type = "en"; if
	 * (Loader.config.exists("Options.Language")) type =
	 * Loader.config.getString("Options.Language"); if (!new
	 * File("plugins/SCR/translations/Translation-" + type + ".yml").exists()) type
	 * = "en"; translations = loadAndMerge("translations/Translation-" + type +
	 * ".yml", "translations/Translation-" + type + ".yml");
	 * 
	 * temp_data.clear(); temp_data = null; // clear cache }
	 * 
	 * private Config loadAndMerge(String sourcePath, String filePath) {
	 * temp_data.reload(StreamUtils.fromStream(getResource("files/" + sourcePath)));
	 * 
	 * Config result = new Config("plugins/SCR/" + filePath); if
	 * (result.merge(temp_data)) result.save(DataType.YAML); return result; }
	 */

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
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			// %theapi_scr_...%
			papi_theapi = new PlaceholderExpansion("") {
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
			((PlaceholderExpansion) papi_theapi).register();
			// %scr_...%
			papi = new me.clip.placeholderapi.expansion.PlaceholderExpansion() {

				@Override
				public String onRequest(OfflinePlayer player, String params) {
					params = '%' + params + '%';
					String f;
					if (Bukkit.getPlayer(player.getUniqueId()) != null)
						f = PlaceholderAPISupport.replace(params, Bukkit.getPlayer(player.getUniqueId()), false, null);
					else
						f = PlaceholderAPISupport.replace(params, null, false, null);
					return params.equals(f) ? null : f;
				}

				@Override
				public String getVersion() {
					return Loader.this.getDescription().getVersion();
				}

				@Override
				public String getIdentifier() {
					return "scr";
				}

				@Override
				public String getAuthor() {
					return "DevTec";
				}
			};
			papi.register();
		}
	}
}
