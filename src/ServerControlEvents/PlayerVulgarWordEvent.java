package ServerControlEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ServerControl.API;

public class PlayerVulgarWordEvent extends Event implements Cancellable {
	Player player;
	String edited;
	String message;
	boolean canceled;

	public PlayerVulgarWordEvent(Player p, String edited, String normal) {
		player = p;
		this.edited = edited;
		message = normal;
	}

	public String getWord() {
		return API.getValueOfVulgarWord(edited);
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
