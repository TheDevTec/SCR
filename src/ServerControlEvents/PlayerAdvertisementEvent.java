package ServerControlEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAdvertisementEvent extends Event implements Cancellable {

	Player player;
	String message;
	boolean canceled;

	public PlayerAdvertisementEvent(Player p, String m) {
		player = p;
		message = m;
	}

	public String getMessage() {
		return message;
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
