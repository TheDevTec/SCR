package ServerControlEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBlockedCommandEvent extends Event implements Cancellable {

	Player player;
	String message;
	String sd;
	boolean canceled = true;

	public PlayerBlockedCommandEvent(Player p, String m, String ww) {
		player = p;
		message = m;
		sd = ww;
	}

	public String getCommand() {
		return message;
	}

	public String getBlockCommand() {
		return sd;
	}

	public Player getPlayer() {
		return player;
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
		return canceled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		canceled = cancel;
	}
}
