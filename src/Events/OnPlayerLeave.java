package Events;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import ServerControl.API;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.AFK;
import Utils.Configs;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;

public class OnPlayerLeave implements Listener {
public Loader plugin=Loader.getInstance;
	private void set(Player p) {
		if(Tasks.players.contains(p)) {
			Tasks.players.remove(p);
	    }
		String date_time = Loader.config.getString("Format.DateWithTime");
	    SimpleDateFormat format_date_time = new SimpleDateFormat(date_time);
	    Loader.me.set("Players."+p.getName()+".LastLeave", format_date_time.format(new Date()));
	    Loader.me.set("Players."+p.getName()+".DisconnectWorld", p.getWorld().getName());
	    Loader.me.set("Players."+p.getName()+".Leaves", Loader.me.getInt("Players."+p.getName()+".Leaves") + 1);
		Loader.me.set("Players."+p.getName()+".LeaveTime",System.currentTimeMillis()/1000);
		   AFK.time.remove(p);
		   AFK.w.remove(p);
			Loader.me.set("Players."+p.getName()+".AFK-Manual",null);
			Loader.me.set("Players."+p.getName()+".AFK-Broadcast",null);
		Configs.chatme.save();
		}
	private String replaceAll(String s, Player p) {
	    SimpleDateFormat format_date_time = new SimpleDateFormat(Loader.config.getString("Format.DateWithTime"));
	    SimpleDateFormat format_time = new SimpleDateFormat(Loader.config.getString("Format.Time"));
	    SimpleDateFormat format_date = new SimpleDateFormat(Loader.config.getString("Format.Date"));
		return TheAPI.getPlaceholderAPI().setPlaceholders(p,s.replace("%players_max%", TheAPI.getCountingAPI().getMaxPlayers()+"")
		  .replace("%players_online%", TheAPI.getCountingAPI().getOnlinePlayers().size()-1+"")
		  .replace("%player%", p.getDisplayName()) 
		  .replace("%playername%", p.getDisplayName()) 
		  .replace("%prefix%", Loader.s("Prefix"))
		  .replace("%time%",format_time.format(new Date()))
		  .replace("%date%",format_date.format(new Date()))
		  .replace("%date-time%",format_date_time.format(new Date()))
		  .replace("%server_support%", plugin.ver())
		  .replace("%version%", "V"+plugin.getDescription().getVersion())
		  .replace("%server_time%", format_time.format(new Date()))
		  .replace("%server_name%", API.getServerName())
		  .replace("%server_ip%", API.getServerIP()+":"+API.getServerPort()));
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerLeaveEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
	    set(p);
		if(setting.leave) {
			e.setQuitMessage(null);
			TheAPI.broadcastMessage(replaceAll(Loader.s("OnLeave.Leave"),p));
		}
		SPlayer s = new SPlayer(p);
		s.disableFly();
		s.disableGod();
		}}
