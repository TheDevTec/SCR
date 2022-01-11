package me.devtec.scr.api.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistTempBanEvent extends BanlistBanEvent implements Cancellable {
	private long duration;
	public BanlistTempBanEvent(String name, String reason, long duration) {
		super(name, reason);
		this.duration=duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration=duration;
	}
}