package Events;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import ServerControl.Loader;
import Utils.Tasks;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Utils.StringUtils;

public class LoginEvent implements Listener {
	public Loader plugin = Loader.getInstance;

	private void bc(Player p) {
		if (setting.vip_join) {
			TheAPI.broadcastMessage(
					Loader.config.getString("Options.VIPSlots.Text.BroadcastVIPJoin")
							.replace("%players_max%", String.valueOf(TheAPI.getMaxPlayers()
									+ (setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)))
							.replace("%online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
							.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
							.replace("%prefix%", setting.prefix)
							.replace("%time%", setting.format_time.format(new Date()))
							.replace("%date%", setting.format_date.format(new Date()))
							.replace("%date-time%", setting.format_date_time.format(new Date())));
		}
	}

	private static String kickString= StringUtils.join(Loader.config.getStringList("Options.Maintenance.KickMessages"), "\n");
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void JoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
		if (setting.lock_server && !p.hasPermission("ServerControl.Maintenance")) {
			e.disallow(Result.KICK_OTHER, TheAPI.colorize(kickString.replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())));
			return;
		}
		if (setting.vip && TheAPI.getMaxPlayers() == TheAPI.getOnlinePlayers().size() - 1) {
			Config f = Loader.config;
			boolean has = p.hasPermission("ServerControl.JoinFullServer");
			int max = TheAPI.getMaxPlayers() + (setting.vip_add ? f.getInt("Options.VIPSlots.SlotsToAdd") : 0);
			Player randomPlayer = Tasks.players.isEmpty() ? null : TheAPI.getRandomPlayer();
			if (has) {
				if (TheAPI.getMaxPlayers() > max && randomPlayer == null) {
					e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.FullServer")));
					return;
				}
				if (setting.vip_kick) {
					if (TheAPI.getMaxPlayers() > max && randomPlayer != null) {
						Tasks.players.remove(randomPlayer.getName());
						randomPlayer.kickPlayer(TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
						bc(p);
						e.allow();
						return;
					}
				}
			} else if (TheAPI.getOnlinePlayers().size() >= TheAPI.getMaxPlayers()) {
				e.disallow(Result.KICK_FULL, TheAPI.colorize(f.getString("Options.VIPSlots.Text.Kick")));
				return;
			}
		}
	}
}