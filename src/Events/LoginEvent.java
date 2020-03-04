package Events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
							.replace("%players_online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName())
							.replace("%prefix%", Loader.s("Prefix"))
							.replace("%time%",new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
							.replace("%date%",new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
							.replace("%date-time%", new SimpleDateFormat(Loader.config.getString("Format.DateWithTime")).format(new Date())));
					}}
	@EventHandler(priority = EventPriority.LOWEST)
	public void JoinEvent(PlayerLoginEvent e) {
	 String kickString = "";
		Player p = e.getPlayer();
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
				    Player randomPlayer = Tasks.players.get(new Random().nextInt(Tasks.players.size()));
				    if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0) && randomPlayer == null) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.FullServer")));
					}
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0) && randomPlayer != null) {
						if(Tasks.players.contains(randomPlayer)) {
							Tasks.players.remove(randomPlayer);
					    }
						randomPlayer.kickPlayer(TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
						bc(p);
						e.allow();
					}
				}else {
					if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
					}}}
				if(Tasks.players.isEmpty()){
					if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.FullServer")));
					}
					if (Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.allow();
						bc(p);
					}}else {
					if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
					}}
				}
			}else {
				if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.FullServer")));
					}
					if (Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+(setting.vip_add ? Loader.config.getInt("Options.VIPSlots.SlotsToAdd") : 0)) {
						bc(p);
						e.allow();
					}}else {
		if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			e.disallow(Result.KICK_FULL,TheAPI.colorize(Loader.config.getString("Options.VIPSlots.Text.Kick")));
			
		}
					}}}}}