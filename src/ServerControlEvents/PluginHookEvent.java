package ServerControlEvents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import ServerControl.Loader;

public class PluginHookEvent extends Event implements Cancellable {

	Plugin plugin;
	boolean c;
	
	public PluginHookEvent(Plugin p) {
		plugin=p;
	}
	public Plugin getPlugin() {
		return plugin;
	}
	
	public boolean isHooked() {
		 return Loader.addons.contains(plugin);
	}
	
	public void Hook() {
		if(!isHooked())Loader.addons.add(plugin);
	}

	private static final HandlerList handler = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handler;
	}
	public static HandlerList getHandlerList() {
		return handler;
	}
	@Override
	public boolean isCancelled() {
		return c;
	}
	@Override
	public void setCancelled(boolean cancel) {
		c=cancel;
		
	}}
