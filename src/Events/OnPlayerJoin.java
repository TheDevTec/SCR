
package Events;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import Commands.Mail;
import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.AFKV2;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.StringUtils;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;
import me.Straiker123.User;

public class OnPlayerJoin implements Listener {
	public OnPlayerJoin() {
		f=Loader.config;
		music = Loader.SoundsChecker();
		join=setting.join_msg;
		firsttime = f.getInt("Options.Join.FirstJoin.Wait") > 0 ? 20*f.getInt("Options.Join.FirstJoin.Wait"):1;
		
	}
	FileConfiguration f;
	boolean music,join;
	int firsttime;
    StringUtils sd = TheAPI.getStringUtils();
    
    public static String replaceAll(String s, Player p) {
		String name = p.getDisplayName();
		return TheAPI.getPlaceholderAPI().setPlaceholders(p,s.replace("%players_max%", TheAPI.getMaxPlayers()+"")
		  .replace("%online%", TheAPI.getOnlinePlayers().size()-1+"")
		  .replace("%player%", name) 
		  .replace("%playername%", name) 
		  .replace("%customname%", p.getCustomName()!=null ? p.getCustomName():name) 
		  .replace("%prefix%", Loader.s("Prefix"))
		  .replace("%time%",setting.format_time.format(new Date()))
		  .replace("%date%",setting.format_date.format(new Date()))
		  .replace("%date-time%",setting.format_date_time.format(new Date()))
		  .replace("%server_support%", Loader.getInstance.ver())
		  .replace("%version%", "V"+Loader.getInstance.getDescription().getVersion())
		  .replace("%server_time%", setting.format_time.format(new Date()))
		  .replace("%server_name%", API.getServerName())
		  .replace("%server_ip%", p.getServer().getIp()+":"+p.getServer().getPort()));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Tasks.regPlayer(p);
		User d = TheAPI.getUser(p);
		if(setting.join_msg) {
			if(TheAPI.isVanished(p))
			e.setJoinMessage("");
			else
			e.setJoinMessage(TheAPI.colorize(replaceAll(Loader.s("OnJoin.Join"),p)));
		}

		AFKV2 v = new AFKV2(p.getName());
		v.start();
		Loader.afk.put(p.getName(), v);

		if(!Mail.getMails(p.getName()).isEmpty())
		Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
				.replace("%number%", ""+d.getStringList("Mails").size()), p);
		if(music)
			TheAPI.getSoundAPI().playSound(p, f.getString("Options.Sounds.Sound"));

		if(API.getBanSystemAPI().hasJail(p)) {
			if(setting.tp_safe)
			TheAPI.getPlayerAPI(p).safeTeleport(sd.getLocationFromString(f.getString("Jails."+d.getString("Jail.Location"))));
			else
				TheAPI.getPlayerAPI(p).teleport(sd.getLocationFromString(f.getString("Jails."+d.getString("Jail.Location"))));
		}else if(f.getBoolean("OnJoin.SpawnTeleport"))API.teleportPlayer(p, TeleportLocation.SPAWN);
		
		d.set("JoinTime",System.currentTimeMillis()/1000);
		if(!d.exist("FirstJoin"))
			d.set("FirstJoin", setting.format_date_time.format(new Date()));
		
		if(!p.hasPlayedBefore() && setting.join_first) {
			for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
						  Loader.msg(replaceAll(ss,p),p);
		}
		if(!TheAPI.isVanished(p))
			TheAPI.broadcastMessage(replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
						 Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {
					  		public void run() {
					  			if(setting.join_first_percmd) 
					  				for(String cmds: f.getStringList("Options.Join.FirstJoin.PerformCommands.List"))
					  					TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(replaceAll(cmds,p)));
									if(setting.join_first_give &&f.getString("Options.Join.FirstJoin.Kit")!=null)
										API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false);
					  }},firsttime);
					API.teleportPlayer(p, TeleportLocation.SPAWN);
	}else {
		if(setting.join_motd){
	    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
	    		Loader.msg(replaceAll(ss,p),p);
		}}}
		if(!TheAPI.getEconomyAPI().hasAccount(p))
			TheAPI.getEconomyAPI().createAccount(p);
		SPlayer s = new SPlayer(p);
		if(s.hasPermission("ServerControl.FlySpeedOnJoin"))s.setFlySpeed();
		if(s.hasPermission("ServerControl.WalkSpeedOnJoin"))s.setWalkSpeed();
		if(s.hasTempFlyEnabled())
			s.enableTempFly();
		else{
			if(s.hasFlyEnabled() && s.hasPermission("servercontrol.flyonjoin") &&
					s.hasPermission("servercontrol.fly"))s.enableFly();
		}
		if(s.hasGodEnabled() && s.hasPermission("servercontrol.godonjoin") &&
				s.hasPermission("servercontrol.god"))s.enableGod();
	}
		@EventHandler(priority = EventPriority.LOWEST)
		public void playerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if(setting.leave) {
				if(TheAPI.isVanished(p))
				e.setQuitMessage(null);
				else
					e.setQuitMessage(TheAPI.colorize(replaceAll(Loader.s("OnLeave.Leave"),p)));
			}
			SPlayer s = new SPlayer(p);
			User d = TheAPI.getUser(p);
			d.set("Joins", s.getPlayer().getStatistic(Statistic.LEAVE_GAME));
		    d.set("LastLeave", setting.format_date_time.format(new Date()));
		    d.set("DisconnectWorld", p.getWorld().getName());
			s.disableFly();
			s.disableGod();
			}
}