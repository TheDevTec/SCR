package me.devtec.scr.api.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistMuteEvent extends BanlistEvent implements Cancellable {
	private boolean cancel;
	private long time;
	public BanlistMuteEvent(String name, String reason, long duration) {
		super(name, reason);
		time=duration;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel=cancel;
	}

	public long getDuration() {
		return time;
	}

	public void setDuration(long time) {
		this.time = time < 0 ? 0 : time;
	}
	
	public boolean isTemp() {
		return time > 0;
	}
}