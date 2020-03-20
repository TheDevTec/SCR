
package Events;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Commands.Mail;
import ServerControl.API;
import ServerControl.API.TeleportLocation;
import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.AFKV2;
import Utils.Configs;
import Utils.ScoreboardStats;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

public class OnPlayerJoin implements Listener {
public Loader plugin=Loader.getInstance;
public OnPlayerJoin() {
	f=Loader.config;
	c=Loader.me;
}
FileConfiguration f,c;
	private void setFlyWalk(Player p) {
		SPlayer s = new SPlayer(p);
		if(p.hasPermission("ServerControl.FlySpeedOnJoin"))s.setFlySpeed();
		if(p.hasPermission("ServerControl.WalkSpeedOnJoin"))s.setWalkSpeed();
	}
	
	private void broadcast(Player p) {
		
		if(!p.hasPlayedBefore() || c.getString("Players."+p.getName()+".FirstJoin")==null){
		c.set("Players."+p.getName()+".FirstJoin", setting.format_date_time.format(new Date()));
		}
		if(!p.hasPlayedBefore() && setting.join_first) {
				for(String ss: Loader.TranslationsFile.getStringList("OnJoin.FirstJoin.Messages")) {
							  Loader.msg(OnPlayerLeave.replaceAll(ss,p),p);
			}
				if(!TheAPI.isVanished(p))
				TheAPI.broadcastMessage(OnPlayerLeave.replaceAll(Loader.s("OnJoin.FirstJoin.BroadCast"),p));
							  if(f.getInt("Options.Join.FirstJoin.Wait") > 0) {
							  		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						  				public void run() {
								  if(setting.join_first_percmd) {
					  			for(String cmds:f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
					  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(OnPlayerLeave.replaceAll(cmds,p)));
					  			}}
					  		if(setting.join_first_give && f.getString("Options.Join.FirstJoin.Kit")!=null)
					  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false); 
						  }},20*f.getInt("Options.Join.FirstJoin.Wait"));
							  		}else {
							  if(setting.join_first_percmd) {
						  			for(String cmds: f.getStringList("Options.Join.FirstJoin.PerformCommands.List")) {
						  				TheAPI.sudoConsole(SudoType.COMMAND,TheAPI.colorize(OnPlayerLeave.replaceAll(cmds,p)));
						  			}}
						  		if(setting.join_first_give &&f.getString("Options.Join.FirstJoin.Kit")!=null)
						  			API.giveKit(p.getName(),f.getString("Options.Join.FirstJoin.Kit"),false,false);
						  }
					  		API.teleportPlayer(p, TeleportLocation.SPAWN);
		}else {
			if(setting.join_motd){
		    	for(String ss: Loader.TranslationsFile.getStringList("OnJoin.Messages")) {
		    		Loader.msg(OnPlayerLeave.replaceAll(ss,p),p);
			}
		}}}
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(setting.join_msg){
			e.setJoinMessage("");
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					if(!TheAPI.isVanished(p))
					TheAPI.broadcastMessage(OnPlayerLeave.replaceAll(Loader.s("OnJoin.Join"),p));
				}}, 11);
			}
				TheAPI.getRunnable().runLater(new Runnable() {
					public void run() {
						Tasks.regPlayer(p);
						AFKV2 v = new AFKV2(p.getName());
						Loader.afk.put(p.getName(), v);
						v.start();
						broadcast(p);
						if(Loader.econ!=null && !Loader.econ.hasAccount(p))
							Loader.econ.createPlayerAccount(p);
							if(setting.sb)
								ScoreboardStats.createScoreboard(p);
						SPlayer s = new SPlayer(p);
						if(s.hasTempFlyEnabled())
							s.enableTempFly();
						else{
							if(s.hasFlyEnabled() && s.hasPermission("servercontrol.flyonjoin") && s.hasPermission("servercontrol.fly"))s.enableFly();
							if(s.hasTempFlyEnabled())s.enableTempFly();
						if(s.hasGodEnabled() && s.hasPermission("servercontrol.godonjoin") && s.hasPermission("servercontrol.god"))s.enableGod();
						}
						if(f.getBoolean("OnJoin.SpawnTeleport") && !API.getBanSystemAPI().hasJail(p))API.teleportPlayer(p, TeleportLocation.SPAWN);
						if(API.getBanSystemAPI().hasJail(p))
							if(setting.tp_safe)
							TheAPI.getPlayerAPI(p).safeTeleport((Location) f.get("Jails."+c.getString("Players."+p.getName()+".Jail.Location")));
							else
								TheAPI.getPlayerAPI(p).teleport((Location) f.get("Jails."+c.getString("Players."+p.getName()+".Jail.Location")));
						setFlyWalk(p);
						if(!Mail.getMails(s.getName()).isEmpty())
							Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
									.replace("%number%", ""+c.getStringList("Players."+p.getName()+".Mails").size()), p);
						if(Loader.SoundsChecker())
							p.playSound(p.getLocation(), TheAPI.getSoundAPI().getByName(f.getString("Options.Sounds.Sound")), 1, 1);
						c.set("Players."+p.getName()+".Joins", c.getInt("Players."+p.getName()+".Joins") + 1);
						c.set("Players."+p.getName()+".JoinTime",System.currentTimeMillis()/1000);
						Configs.chatme.save();
					}
				}, 11);

}}