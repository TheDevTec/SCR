package Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import ServerControl.Loader;
import ServerControl.SPlayer;
import Utils.ScoreboardStats;
import Utils.setting;

public class WorldChange implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSleep(PlayerBedEnterEvent e) {
		if(setting.singeplayersleep) {
			e.getPlayer().getWorld().setTime(1000);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerWorldChangeEvent(PlayerChangedWorldEvent e) {
		SPlayer a = new SPlayer(e.getPlayer());
		a.createEconomyAccount();
		if(a.hasPermission("servercontrol.fly") && a.hasFlyEnabled())
			e.getPlayer().setAllowFlight(true);
		if(a.hasPermission("servercontrol.god") && a.hasGodEnabled())
		a.enableGod();
		a.setGamamode();
		if(Loader.scFile.getBoolean("Scoreboard-PerWorld"))
			ScoreboardStats.createScoreboard(e.getPlayer());
		
		}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChangeGamamode(PlayerGameModeChangeEvent e) {
		SPlayer a = new SPlayer(e.getPlayer());
		if(a.hasPermission("servercontrol.fly") && a.hasFlyEnabled())
			e.getPlayer().setAllowFlight(true);
		if(a.hasPermission("servercontrol.god") && a.hasGodEnabled())
		a.enableGod();
		}}