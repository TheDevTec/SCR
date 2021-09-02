package me.devtec.servercontrolreloaded.events.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.devtec.servercontrolreloaded.scr.Loader;

//1.7 - 1.12.2
public class MWSettingsLegacy implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		if(e.isCancelled())return;
		if (e.getEntity() instanceof Player) {
			String w = e.getEntity().getWorld().getName();
			if (e.getCause() == DamageCause.FALL && !Loader.mw.getBoolean("settings." + w + ".fallDamage"))
				e.setCancelled(true);
			if ((e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE_TICK) && !Loader.mw.getBoolean("settings." + w + ".fireDamage"))
				e.setCancelled(true);
			if (e.getCause() == DamageCause.DROWNING && !Loader.mw.getBoolean("settings." + w + ".drownDamage"))
				e.setCancelled(true);
		}
	}
}