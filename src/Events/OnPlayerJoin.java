
package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Utils.Tasks;
import Utils.setting;

public class OnPlayerJoin implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(setting.join_msg)
			e.setJoinMessage("");
			Tasks.joined.add(p);
			if(!p.hasPlayedBefore())Tasks.playedBefore.add(p);

}}