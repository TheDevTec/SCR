package me.devtec.scr.modules.bossbar;

import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;

public class BossBarManager {
	public static SBossBar create(Player player) {
		return TheAPI.isOlderThan(9)?new LegacyBossBar(player, "", 100):new ModernBossBar(player, "");
	}
}
