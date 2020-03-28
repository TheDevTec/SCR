package Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Utils.Tasks;
import Utils.setting;

public class OnPlayerLeave implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerLeaveEvent(PlayerQuitEvent e) {
		if(setting.leave)
			e.setQuitMessage(null);
		Tasks.quit.add(e.getPlayer());
		}}
