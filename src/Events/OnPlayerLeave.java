package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import ServerControl.Loader;
import Utils.Tasks;
import Utils.setting;

public class OnPlayerLeave implements Listener {
public Loader plugin=Loader.getInstance;


	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerLeaveEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(setting.leave) {
			e.setQuitMessage(null);
			Tasks.quit.add(p);
		}}}
