package me.DevTec.ServerControlReloaded.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.apis.PluginManagerAPI;
import net.milkbowl.vault.economy.Economy;

public class VaultHook {

	private Economy provider;

	public void hook() {
		if (PluginManagerAPI.getPlugin("Vault") != null) {
			provider = new Eco();
			Bukkit.getServicesManager().register(Economy.class, provider, Loader.getInstance, ServicePriority.Normal);
			Loader.EconomyLog("Vault hooked into plugin Economy");
		}
	}

	public void unhook() {
		if (PluginManagerAPI.getPlugin("Vault") != null)
			if (provider != null) {
				Bukkit.getServicesManager().unregister(Economy.class, provider);
				Loader.EconomyLog("Vault unhooked from plugin Economy");
			}
	}
}