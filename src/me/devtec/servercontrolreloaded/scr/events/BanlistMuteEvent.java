package me.devtec.servercontrolreloaded.scr.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistMuteEvent extends BanlistEvent implements Cancellable {
	private boolean cancel;
	public BanlistMuteEvent(String name, String reason) {
		super(name, reason);
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel=cancel;
	}
}
