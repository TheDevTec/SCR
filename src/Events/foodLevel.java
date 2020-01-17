package Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import ServerControl.Loader;

public class foodLevel implements Listener {
	private boolean w(Player p) {
		boolean wa = false;
		if(w.get(p)!=null)wa=w.get(p);
		if(!wa) {
			
			w.put(p, true);
			Bukkit.getScheduler().runTaskLater(Loader.getInstance, new Runnable() {

				@Override
				public void run() {
					w.put(p, false);
				}

			}, 30);
			return true;
		}
		return false;
	}

	HashMap<Player, Boolean> w = new HashMap<Player, Boolean>();
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
	if(Loader.me.getBoolean("Players."+e.getEntity().getName()+".God")==true && !w((Player) e.getEntity())) {
		((Player) e.getEntity()).setFoodLevel(20);
		}}}
	
}
