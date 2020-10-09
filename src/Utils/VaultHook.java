package Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import ServerControl.API;
import ServerControl.Loader;
import net.milkbowl.vault.economy.Economy;

public class VaultHook {

	private Economy provider;

	public void hook() {
		if (API.existVaultPlugin()) {
			provider = new Eco();
			Bukkit.getServicesManager().register(Economy.class, provider, Loader.getInstance, ServicePriority.Normal);
			Loader.EconomyLog("Vault hooked into plugin Economy");
		}
	}

	public void unhook() {
		if (API.existVaultPlugin())
			if (provider != null) {
				Bukkit.getServicesManager().unregister(Economy.class, provider);
				Loader.EconomyLog("Vault unhooked from plugin Economy");
			}
	}
}