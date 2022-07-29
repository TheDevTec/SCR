package me.devtec.scr;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.devtec.scr.api.ScrEconomy;
import me.devtec.scr.utils.Messages;
import me.devtec.shared.Ref;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.scheduler.Tasker;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Loader extends JavaPlugin {

	public static Loader plugin;
	
	public static Config config, translation, commands, economyConfig;
	
	//VAULT plugin
	public static Object economy, vault;
	
	public String prefix;
	
	public static Random random = new Random();
	
	public void onLoad() {
		
		//check nové verze
	
		//1 řádková zpráva o loadu
		Configs.loadConfigs();
		this.prefix=config.getString("Options.Prefix");
		
		if(Bukkit.getPluginManager().getPlugin("Vault")!=null && Ref.getClass("net.milkbowl.vault.economy.Economy") != null) {
			setupVault();
		} else {
			Messages.msgConsole("%prefix%  &8*********************************************");
			Messages.msgConsole("%prefix%  &eINFO: &7Missing Vault plugin for Economy.");
			Messages.msgConsole("%prefix%  &8*********************************************");
		}
		
		
	}
	
	public void onEnable() {
		//loading eventů a commandů

		//load placeholderů
		//load tablistu, scoreboardy
		
	}
	
	/*	FUNKCE?
	 * Chat
	 * 	Fromáty
	 * 	Zmínění v chatu
	 * 	Nějaká bezpečností kontrola (spam, sprostá slova, flood)
	 * 
	 * CustomPříkazy
	 * CustomGuička
	 * 
	 * Tablist
	 * Scoreboard
	 * 
	 * Portály
	 * Bungee podpora
	 * Basic NameTagy, ale asi každý používá TAB :D
	 * 
	 */
	public void onDisable() {
		
	}
	
	
	
	//VAULT HOOKING
	private void setupVault() {
		vaultPermissionHooking();
		if(economyConfig.getBoolean("useVaultEconomy")) {
			vaultEconomyHooking();
			economyConfig.clear();
			economyConfig = null;
		}else {
			getLogger().info("[Economy] Registering ScrEconomy and using as Vault economy.");
			economy = new ScrEconomy(economyConfig);
			Bukkit.getServicesManager().register(Economy.class, (ScrEconomy)economy, this, ServicePriority.Normal);
		}
	}
	private void vaultEconomyHooking() {
		getLogger().info("[Economy] Looking for Vault economy service..");
		new Tasker() {
			@Override
			public void run() {
				if (getVaultEconomy()) {
					getLogger().info("[Economy] Found Vault economy service. "+((Economy)economy).getName());
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
					getLogger().info("[Permission] Found Vault permission service. "+((Permission)vault).getName());
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
}
