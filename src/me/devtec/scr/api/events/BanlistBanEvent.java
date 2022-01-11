package me.devtec.scr.api.events;


import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistBanEvent extends BanlistEvent implements Cancellable {
	private boolean cancel;
	public BanlistBanEvent(String name, String reason) {
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