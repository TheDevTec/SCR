package ServerControlEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ServerControl.API;

public class PlayerSpamWordEvent extends Event implements Cancellable {

	Player player;
	String word;
	String message;
	boolean canceled;

	public PlayerSpamWordEvent(Player p, String w, String m) {
		player = p;
		word = w;
		message = m;
	}

	public String getWord() {
		return API.getValueOfSpamWord(word);
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
