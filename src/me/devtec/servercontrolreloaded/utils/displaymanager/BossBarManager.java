package me.devtec.servercontrolreloaded.utils.displaymanager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;

public class BossBarManager {
	static Map<Player, SBossBar> bar = new HashMap<>();
	public static SBossBar get(Player player) {
		return bar.get(player);
	}
	
	public static SBossBar getOrCreate(Player player) {
		SBossBar bar = get(player);
		if(bar==null)BossBarManager.bar.put(player,bar=TheAPI.isOlderThan(9)?new LegacyBossBar(player, "", 100):new ModernBossBar(player, ""));
		return bar;
	}
	
	public static void remove(Player player) {
		SBossBar bar = BossBarManager.bar.remove(player);
		if(bar!=null)bar.remove();
	}
}
