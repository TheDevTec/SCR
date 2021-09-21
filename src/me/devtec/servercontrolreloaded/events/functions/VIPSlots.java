package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.ChatFormatter;
import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.Tasks;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.utils.StringUtils;

public class VIPSlots implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void login(PlayerLoginEvent e) {
		if(e.getResult()==Result.ALLOWED)
			return;
		Player p = e.getPlayer();
		if (setting.lock_server && !Loader.has(p, "Maintenance", "Info", "Bypass")) {
			if(Loader.config.getBoolean("ChatFormat.enabled"))
			ChatFormatter.setupName(p);
			e.disallow(Result.KICK_WHITELIST, TabList.replace(StringUtils.join(Loader.config.getStringList("Options.Maintenance.KickMessages"), "\n"), p, true));
			return;
		}
		if (setting.vip && TheAPI.getMaxPlayers() <= TheAPI.getOnlineCount()-1) {
			Config f = Loader.config;
			boolean has = p.hasPermission("SCR.Other.JoinFullServer");
			int max = TheAPI.getMaxPlayers() + (setting.vip_add ? f.getInt("Options.VIPSlots.SlotsToAdd") : 0);
			Player randomPlayer = Tasks.players.isEmpty() ? null : TheAPI.getRandomPlayer();
			if (has) {
				if (TheAPI.getMaxPlayers() > max && randomPlayer == null) {
					ChatFormatter.setupName(p);
					e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.FullServer")));
					return;
				}
				if (setting.vip_kick) {
					if (TheAPI.getMaxPlayers() > max && randomPlayer != null) {
						Tasks.players.remove(randomPlayer.getName());
						randomPlayer.kickPlayer(TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
						if (setting.vip_join) {
							if(Loader.config.getBoolean("ChatFormat.enabled"))
							ChatFormatter.setupName(p);
							TheAPI.broadcastMessage(TabList.replace(Loader.config.getString("Options.VIPSlots.Text.BroadcastVIPJoin"), p, false));
						}
						e.allow();
					}
				}
			} else if (TheAPI.getOnlineCount() >= TheAPI.getMaxPlayers()) {
				e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
			}
		}
	}
}