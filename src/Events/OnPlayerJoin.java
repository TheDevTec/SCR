
package Events;

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
import Utils.AFKV2;
import Utils.Configs;
import Utils.Tasks;
import Utils.setting;
import me.Straiker123.TheAPI;

public class OnPlayerJoin implements Listener {
public Loader plugin=Loader.getInstance;
public OnPlayerJoin() {
	f=Loader.config;
	c=Loader.me;
}
FileConfiguration f,c;

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(setting.join_msg){
			e.setJoinMessage("");
			}
				TheAPI.getRunnable().runLater(new Runnable() {
					public void run() {
						Tasks.regPlayer(p);
						AFKV2 v = new AFKV2(p.getName());
						Loader.afk.put(p.getName(), v);
						v.start();
						
						if(f.getBoolean("OnJoin.SpawnTeleport") && !API.getBanSystemAPI().hasJail(p))API.teleportPlayer(p, TeleportLocation.SPAWN);
						if(API.getBanSystemAPI().hasJail(p))
							if(setting.tp_safe)
							TheAPI.getPlayerAPI(p).safeTeleport(TheAPI.getStringUtils().getLocationFromString(f.getString("Jails."+c.getString("Players."+p.getName()+".Jail.Location"))));
							else
								TheAPI.getPlayerAPI(p).teleport(TheAPI.getStringUtils().getLocationFromString(f.getString("Jails."+c.getString("Players."+p.getName()+".Jail.Location"))));
						if(!Mail.getMails(p.getName()).isEmpty())
							Loader.msg(Loader.s("Prefix")+Loader.s("Mail.Notification")
									.replace("%number%", ""+c.getStringList("Players."+p.getName()+".Mails").size()), p);
						if(Loader.SoundsChecker())
							p.playSound(p.getLocation(), TheAPI.getSoundAPI().getByName(f.getString("Options.Sounds.Sound")), 1, 1);
						c.set("Players."+p.getName()+".Joins", c.getInt("Players."+p.getName()+".Joins") + 1);
						c.set("Players."+p.getName()+".JoinTime",System.currentTimeMillis()/1000);
						Configs.chatme.save();
					}
				}, 10);

}}