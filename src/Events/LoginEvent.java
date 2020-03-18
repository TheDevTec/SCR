package Events;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;

public class LoginEvent implements Listener {
public Loader plugin=Loader.getInstance;
	private void bc(Player p) {
				if(setting.vip_join) {
					TheAPI.broadcastMessage(Loader.config.getString("Options.VIPSlots.Text.BroadcastVIPJoin")
							.replace("%players_max%", String.valueOf(Bukkit.getMaxPlayers() + (setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)))
							.replace("%players_online%", String.valueOf(TheAPI.getOnlinePlayers().size()))
							.replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName())
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%time%",setting.format_time.format(new Date()))
							.replace("%date%",setting.format_date.format(new Date()))
							.replace("%date-time%",setting.format_date_time.format(new Date())));
					}}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void JoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
	 String kickString = "";
		Loader.setupChatFormat(p);
		API.getBanSystemAPI().processBanSystem(p, e);
		if(setting.lock_server && !p.hasPermission("ServerControl.Maintenance")) {
					for(String sk : Loader.config.getStringList("Options.Maintenance.KickMessages")) {
					                    sk = sk.replace("%player%", p.getName()).replace("%playername%", p.getName());
					                    kickString += sk + "\n";
					                }
					e.disallow(Result.KICK_OTHER, TheAPI.colorize(kickString));
					return;
					}
		if(setting.vip) {
			if(setting.vip_kick) {
				if(!Tasks.players.isEmpty()){
				    Player randomPlayer = TheAPI.getRandomPlayer();
				    if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0) && randomPlayer == null) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.FullServer")));
					}
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0) && randomPlayer != null) {
						if(Tasks.players.contains(randomPlayer.getName())) {
							Tasks.players.remove(randomPlayer.getName());
					    }
						randomPlayer.kickPlayer(TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
						bc(p);
						e.allow();
					}
				}else {
					if (TheAPI.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
					}}}
				if(Tasks.players.isEmpty()){
					if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (TheAPI.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.FullServer")));
					}
					if (TheAPI.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.allow();
						bc(p);
					}}else {
					if (TheAPI.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
					}}
				}
			}else {
				if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (TheAPI.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.FullServer")));
					}
					if (TheAPI.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						bc(p);
						e.allow();
					}}else {
		if (TheAPI.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			e.disallow(Result.KICK_FULL,TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
			
		}}}}
	}}