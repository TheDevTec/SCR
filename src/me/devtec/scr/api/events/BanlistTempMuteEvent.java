package me.devtec.scr.api.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistTempMuteEvent extends BanlistMuteEvent implements Cancellable {
	private long duration;
	public BanlistTempMuteEvent(String name, String reason, long duration) {
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