package me.devtec.scr;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.dataholder.DataType;
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StreamUtils;
import net.milkbowl.vault.economy.Economy;

public class Loader extends JavaPlugin {
	
	public static Loader plugin;
	
	public static Config config;
	public static Config commands;
	public static Config translations;
	public static Economy economy;
	
	private Config economyConfig;
	
	public void onLoad() {
		plugin=this;
		loadConfigs();
		if(Bukkit.getPluginManager().getPlugin("Vault") != null && Ref.getClass("net.milkbowl.vault.economy.Economy") != null) {
			if(economyConfig.getBoolean("useVaultEconomy"))
				vaultHooking();
			else {
				getLogger().info("[Economy] Registering ScrEconomy and using as Vault economy.");
				economy = new ScrEconomy(economyConfig);
				Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.Normal);
			}
		}
	}
	
	public void onEnable() {
		loadListeners();
		loadCommands();
	}

	private void loadListeners() {
		//TODO listeners
	}
	
	private void loadCommands() {
		int count = 0;
		int total = 0;
		
		getLogger().info("Loading commands..");
		try {
			File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			JarFile jar = new JarFile(file);
			Enumeration<JarEntry> entries = jar.entries();
			while(entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if(!entry.getName().endsWith(".class") || !entry.getName().startsWith("me/devtec/scr/commands/") || entry.getName().equals("me/devtec/scr/commands/ScrCommand.class"))continue;
				Class<?> cls = Class.forName(entry.getName().substring(0, entry.getName().length()-6).replace("/", "."));
				if (ScrCommand.class.isAssignableFrom(cls)) {
					ScrCommand scrCmd = (ScrCommand) cls.newInstance();
					++total;
					if(commands.getBoolean(scrCmd.configSection()+".enabled")) {
						++count;
						scrCmd.init(commands.getStringList(scrCmd.configSection()+".cmds"));
					}
				}
			}
			jar.close();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "An issue occurred while loading commands, please report the error to discord https://discord.gg/5kCSrtkKGF", e);
			return;
		}
		getLogger().info("Commands successfully loaded. ("+count+"/"+total+")");
	}

	private void loadConfigs() {
		Config data = new Config();
		
		data.reload(StreamUtils.fromStream(getResource("files/config.yml")));
		config=new Config("plugins/SCR/config.yml");
		if(config.merge(data, true, true))
			config.save(DataType.YAML);
		
		data.reload(StreamUtils.fromStream(getResource("files/commands.yml")));
		
		commands=new Config("plugins/SCR/commands.yml");
		if(commands.merge(data, true, true))
			commands.save(DataType.YAML);
		
		data.reload(StreamUtils.fromStream(getResource("files/translations.yml")));
		
		translations=new Config("plugins/SCR/translations.yml");
		if(translations.merge(data, true, true))
			translations.save(DataType.YAML);
		
		data.reload(StreamUtils.fromStream(getResource("files/economy.yml")));
		
		economyConfig=new Config("plugins/SCR/economy.yml");
		if(economyConfig.merge(data, true, true))
			economyConfig.save(DataType.YAML);
	}

	public static void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}
	
	//VAULT HOOKING
	private void vaultHooking() {
		getLogger().info("[Economy] Looking for Vault economy service..");
		new Tasker() {
			@Override
			public void run() {
				if (getVaultEconomy()) {
					getLogger().info("[Economy] Found Vault economy service. "+economy.getName());
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
}
