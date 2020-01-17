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
import me.Straiker123.TheAPI;

public class LoginEvent implements Listener {
public Loader plugin=Loader.getInstance;
	
	private void bc(Player p) {
				if(Loader.config.getBoolean("VIPSlots.Enable-VIPPlayerJoinBroadcast")==true) {
					
					TheAPI.broadcastMessage(Loader.config.getString("VIPSlots.VIPPlayerJoinBroadcast")
							.replace("%players_max%".toLowerCase(), String.valueOf(Bukkit.getMaxPlayers() + Loader.config.getInt("VIPSlots.MaxSlots")))
							.replace("%players_online%".toLowerCase(), String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("%player%".toLowerCase(), p.getName())
							.replace("%playername%".toLowerCase(), p.getDisplayName())
							.replace("%prefix%".toLowerCase(), Loader.s("Prefix"))
							.replace("%time%".toLowerCase(),new SimpleDateFormat(Loader.config.getString("Format.Time")).format(new Date()))
							.replace("%date%".toLowerCase(),new SimpleDateFormat(Loader.config.getString("Format.Date")).format(new Date()))
							.replace("%date-time%".toLowerCase(), new SimpleDateFormat(Loader.config.getString("Format.DateWithTime")).format(new Date()))
							.replace("%web%".toLowerCase(), Loader.s("OnJoin.Web")));
					}}
	@EventHandler(priority = EventPriority.LOWEST)
	public void JoinEvent(PlayerLoginEvent e) {
	 String kickString = "";
		Player p = e.getPlayer();
		Loader.setupChatFormat(p);
		API.getBanSystemAPI().processBanSystem(p, e);
		if(Loader.config.getBoolean("MaintenanceMode.Enabled") && !p.hasPermission("ServerControl.Maintenance")) {
					for(String sk : Loader.config.getStringList("MaintenanceMode.KickMessages")) {
					                    sk = sk.replace("%player%", p.getName()).replace("%playername%", p.getName());
					                    kickString += sk + "\n";
					                }
					e.disallow(Result.KICK_OTHER, TheAPI.colorize(kickString));
					return;
					}
		if(Loader.config.getBoolean("VIPSlots.Enabled")==true) {
			if(Loader.config.getBoolean("VIPSlots.KickPlayerIfServerIsFull")==true) {
				if(!Tasks.players.isEmpty()){
				    Player randomPlayer = Tasks.players.get(new Random().nextInt(Tasks.players.size()));
				    if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots") && randomPlayer == null) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("VIPSlots.KickVIPServerNeedMoreVIPSlots")));
					}
					if (Bukkit.getMaxPlayers() > Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots") && randomPlayer != null) {
						if(Tasks.players.contains(randomPlayer)) {
							Tasks.players.remove(randomPlayer);
					    }
						randomPlayer.kickPlayer(TheAPI.colorize(Loader.config.getString("VIPSlots.KickMessage")));
						bc(p);
						e.allow();
					}
				}else {
					if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("VIPSlots.KickMessage")));
					}}}
				if(Tasks.players.isEmpty()){
					if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots")) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("VIPSlots.KickVIPServerNeedMoreVIPSlots")));
					}
					if (Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots")) {
						e.allow();
						bc(p);
					}}else {
					if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("VIPSlots.KickMessage")));
					}}
				}
			}else {
				if(p.hasPermission("ServerControl.JoinFullServer")) {
					if (Bukkit.getOnlinePlayers().size() > Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots")) {
						e.disallow(Result.KICK_FULL, TheAPI.colorize(Loader.config.getString("VIPSlots.KickVIPServerNeedMoreVIPSlots")));
					}
					if (Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers()+Loader.config.getInt("VIPSlots.MaxSlots")) {
						bc(p);
						e.allow();
					}}else {
		if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
			e.disallow(Result.KICK_FULL,TheAPI.colorize(Loader.config.getString("VIPSlots.KickMessage")));
			
		}
					}}}}}